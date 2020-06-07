import os
from datetime import datetime, timedelta
import discord
from discord.ext import commands


def setup(bot):
    bot.add_cog(Controls(bot))


class Controls(commands.Cog):
    def __init__(self, bot):
        self.bot = bot
        self.controls_channel_id = int(os.getenv('CONTROLS_CHANNEL_ID'))
        self.chat_channel_id = int(os.getenv('CHAT_CHANNEL_ID'))
        self.default_role_id = int(os.getenv('DEFAULT_ROLE_ID'))
        self.promoted_role_id = int(os.getenv('PROMOTED_ROLE_ID'))
        self.payload = None
        self.last_poll = datetime.now()

    async def is_new_reaction(self):
        # unwrap the payload
        guild = self.bot.get_guild(self.payload.guild_id)
        channel = self.bot.get_channel(self.controls_channel_id)
        message = await channel.fetch_message(self.payload.message_id)

        # find the reaction
        for r in message.reactions:
            if isinstance(r.emoji, str) and r.emoji == self.payload.emoji.name:
                reaction = r
            elif not isinstance(r.emoji, str) and r.emoji.name == self.payload.emoji.name:
                reaction = r

        # determine if it is new
        members = await reaction.users().flatten()
        if guild.me not in members:
            return True
        else:
            return False

    async def is_not_owner(self):
        return not await self.bot.is_owner(self.payload.member)

    async def is_game_emoji(self):
        role = discord.utils.get(self.payload.member.guild.roles, name=self.payload.emoji.name)
        if role is None:
            return False
        return True

    async def remove_reaction(self):
        message = await self.bot.get_channel(self.controls_channel_id).fetch_message(self.payload.message_id)
        for r in message.reactions:
            if isinstance(r.emoji, str) and r.emoji == self.payload.emoji.name:
                reaction_to_remove = r
            elif not isinstance(r.emoji, str) and r.emoji.name == self.payload.emoji.name:
                reaction_to_remove = r
        await reaction_to_remove.remove(self.payload.member)

    async def change_server_region(self):
        # check if admin
        if await self.is_not_owner():
            return

        # change the server region
        region = discord.VoiceRegion.us_west if self.payload.emoji.name == '🌴' else discord.VoiceRegion.us_central
        await self.payload.member.guild.edit(region=region)

    async def toggle_priority_speaker(self):
        # check if admin
        if await self.is_not_owner():
            return

        # get the members
        voice_channel = None
        for vc in self.payload.member.guild.voice_channels:
            if self.payload.member.guild.owner in vc.members:
                voice_channel = vc
                break
        if voice_channel is None:
            return
        members = voice_channel.members

        # mute or unmute all the members
        for m in members:
            if m == self.payload.member.guild.owner:
                continue
            await m.edit(mute=not m.voice.mute)

    async def toggle_control_panel(self):
        # get the channel
        channel = self.bot.get_channel(self.controls_channel_id)

        # change the permissions
        for o in channel.overwrites_for(self.payload.member.guild.default_role):
            if o[0] == 'read_messages':
                await channel.set_permissions(self.payload.member.guild.default_role, read_messages=not o[1])

    async def start_common_games_poll(self):
        # check the cooldown
        if datetime.now() - self.last_poll < timedelta(minutes=1):
            return
        self.last_poll = datetime.now()

        # get the guild
        guild = self.payload.member.guild

        # get the members
        voice_channel = None
        for vc in guild.voice_channels:
            if self.payload.member in vc.members:
                voice_channel = vc
                break
        if voice_channel is None:
            raise commands.CommandError
        members = voice_channel.members

        # get the roles
        roles = guild.roles[1:guild.roles.index(guild.get_role(self.promoted_role_id))]
        for m in members:
            if guild.get_role(self.promoted_role_id) in m.roles:
                member_roles = m.roles[1:m.roles.index(guild.get_role(self.promoted_role_id))]
            else:
                member_roles = m.roles[1:m.roles.index(guild.get_role(self.default_role_id))]
            roles = list(set(roles) & set(member_roles))
        if len(roles) < 2:
            raise commands.CommandError('not enough games in common')

        # send the message
        message = await self.bot.get_channel(self.chat_channel_id).send('what game do you want to play?')

        # add the reactions
        for r in roles:
            await message.add_reaction(discord.utils.get(guild.emojis, name=r.name))

    async def start_game_poll(self):
        # check the cooldown
        if datetime.now() - self.last_poll < timedelta(minutes=1):
            return
        self.last_poll = datetime.now()

        # get the guild
        guild = self.payload.member.guild

        # send the message
        message = await self.bot.get_channel(self.chat_channel_id).send('what game do you want to play?')

        # add the reactions
        for r in reversed(guild.roles[1:guild.roles.index(guild.get_role(self.default_role_id))]):
            await message.add_reaction(discord.utils.get(guild.emojis, name=r.name))

    async def update_roles(self):
        # find the role
        role = discord.utils.get(self.payload.member.guild.roles, name=self.payload.emoji.name)

        # add or remove the role
        if role in self.payload.member.roles:
            await self.payload.member.remove_roles(role)
        else:
            await self.payload.member.add_roles(role)

    @commands.Cog.listener()
    async def on_raw_reaction_add(self, payload):
        # deliver the payload
        self.payload = payload

        # ignore the event
        if (payload.channel_id != self.controls_channel_id or
                payload.member.bot or
                await self.is_new_reaction()):
            if (payload.channel_id == self.controls_channel_id and
                    not payload.member.bot and
                    await self.is_new_reaction()):
                await self.remove_reaction()
            return

        await self.remove_reaction()
        if payload.emoji.name == '🌴' or payload.emoji.name == '🌽':
            await self.change_server_region()
        elif payload.emoji.name == '🎙':
            await self.toggle_priority_speaker()
        elif payload.emoji.name == '👁':
            await self.toggle_control_panel()
        elif payload.emoji.name == '👋':
            await self.bot.get_cog('Hello').toggle()
        elif payload.emoji.name == '🗳':
            await self.start_game_poll()
        elif payload.emoji.name == '🔀':
            await self.start_common_games_poll()
        elif await self.is_game_emoji():
            await self.update_roles()

    @commands.command(aliases=['c'])
    @commands.guild_only()
    @commands.is_owner()
    async def controls(self, ctx):
        # ignore the command
        if ctx.channel.id != self.controls_channel_id:
            raise commands.CommandError('cannot use this command in this channel')

        # clear the channel
        messages = await ctx.channel.history().flatten()
        await ctx.channel.delete_messages(messages)

        # send the header
        await ctx.send(file=discord.File('assets/header.png'))
        await ctx.send('**PURDOODOO GLOBAL\n* now with social distancing! ***\n\n'
                       ':small_orange_diamond:   there are no rules I don\'t give a shit')

        # send the admin controls
        await ctx.send('**-----------------------------------\nADMIN CONTROLS**')
        message = await ctx.send('change server region')
        await message.add_reaction('🌴')
        await message.add_reaction('🌽')
        message = await ctx.send('admin toggles')
        await message.add_reaction('🎙')
        await message.add_reaction('👁')
        await message.add_reaction('👋')

        # send the general controls
        await ctx.send('**-----------------------------------\nGENERAL CONTROLS**')
        message = await ctx.send('start game poll')
        await message.add_reaction('🗳')
        await message.add_reaction('🔀')
        message = await ctx.send('add or remove games from your roles')
        for r in reversed(ctx.guild.roles[1:ctx.guild.roles.index(ctx.guild.get_role(self.default_role_id))]):
            await message.add_reaction(discord.utils.get(ctx.guild.emojis, name=r.name))
