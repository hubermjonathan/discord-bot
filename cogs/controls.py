from datetime import datetime, timedelta
import discord
from discord.ext import commands


class Controls(commands.Cog):
    def __init__(self, bot, controls_channel_id, chat_channel_id, default_role_id):
        self.bot = bot
        self.controls_channel_id = controls_channel_id
        self.chat_channel_id = chat_channel_id
        self.default_role_id = default_role_id
        self.last_poll = datetime.now()

    async def is_new_reaction(self, payload):
        # unwrap the payload
        guild = self.bot.get_guild(payload.guild_id)
        channel = self.bot.get_channel(self.controls_channel_id)
        message = await channel.fetch_message(payload.message_id)

        # find the reaction
        for r in message.reactions:
            if isinstance(r.emoji, str) and r.emoji == payload.emoji.name:
                reaction = r
            elif not isinstance(r.emoji, str) and r.emoji.name == payload.emoji.name:
                reaction = r

        # determine if it is new
        members = await reaction.users().flatten()
        if guild.me not in members:
            return True
        else:
            return False

    async def is_not_owner(self, payload):
        return not await self.bot.is_owner(payload.member)

    async def remove_reaction(self, payload):
        message = await self.bot.get_channel(self.controls_channel_id).fetch_message(payload.message_id)
        for r in message.reactions:
            if isinstance(r.emoji, str) and r.emoji == payload.emoji.name:
                reaction_to_remove = r
            elif not isinstance(r.emoji, str) and r.emoji.name == payload.emoji.name:
                reaction_to_remove = r
        await reaction_to_remove.remove(payload.member)

    async def change_server_region(self, payload):
        # check if admin
        if await self.is_not_owner(payload):
            return

        # get the guild
        guild = self.bot.get_guild(payload.guild_id)

        # change the server region
        region = discord.VoiceRegion.us_west if payload.emoji.name == '🌴' else discord.VoiceRegion.us_central
        await guild.edit(region=region)

    async def toggle_priority_speaker(self, payload):
        # check if admin
        if await self.is_not_owner(payload):
            return

        # get the guild
        guild = self.bot.get_guild(payload.guild_id)

        # get the members
        voice_channel = None
        for vc in guild.voice_channels:
            if guild.owner in vc.members:
                voice_channel = vc
                break
        if voice_channel is None:
            return
        members = voice_channel.members

        # mute or unmute all the members
        for m in members:
            if m == guild.owner:
                continue
            await m.edit(mute=not m.voice.mute)

    async def start_game_poll(self, payload):
        # check the cooldown
        if datetime.now() - self.last_poll < timedelta(minutes=1):
            return
        self.last_poll = datetime.now()

        # get the guild
        guild = self.bot.get_guild(payload.guild_id)

        # send the message
        message = await self.bot.get_channel(self.chat_channel_id).send('what game do you want to play?')

        # add the reactions
        for r in reversed(guild.roles[1:guild.roles.index(guild.get_role(self.default_role_id))]):
            await message.add_reaction(discord.utils.get(guild.emojis, name=r.name))

    @commands.Cog.listener()
    async def on_raw_reaction_add(self, payload):
        # ignore the event
        if (payload.channel_id != self.controls_channel_id or
                await self.is_new_reaction(payload) or
                payload.member.bot):
            if (payload.channel_id == self.controls_channel_id and
                    not payload.member.bot and
                    await self.is_new_reaction(payload)):
                await self.remove_reaction(payload)
            return

        await self.remove_reaction(payload)
        if payload.emoji.name == '🌴' or payload.emoji.name == '🌽':
            await self.change_server_region(payload)
        elif payload.emoji.name == '🎙':
            await self.toggle_priority_speaker(payload)
        elif payload.emoji.name == '🗳':
            await self.start_game_poll(payload)

    @commands.command()
    @commands.guild_only()
    @commands.is_owner()
    async def controls_create(self, ctx):
        # ignore the command
        if ctx.channel.id != self.controls_channel_id:
            raise commands.CommandError

        # clear the channel
        messages = await ctx.channel.history().flatten()
        await ctx.channel.delete_messages(messages)

        # send the admin controls
        await ctx.send('**-----------------------------------\nADMIN CONTROLS**')
        message = await ctx.send('change server region')
        await message.add_reaction('🌴')
        await message.add_reaction('🌽')
        message = await ctx.send('toggle priority speaker')
        await message.add_reaction('🎙')

        # send the general controls
        await ctx.send('**-----------------------------------\nGENERAL CONTROLS**')
        message = await ctx.send('start game poll')
        await message.add_reaction('🗳')
        message = await ctx.send('add or remove games from your roles')
        for r in reversed(ctx.guild.roles[1:ctx.guild.roles.index(ctx.guild.get_role(self.default_role_id))]):
            await message.add_reaction(discord.utils.get(ctx.guild.emojis, name=r.name))
