import os
import discord
from discord.ext import commands


def setup(bot):
    bot.add_cog(Controls(bot))


class Controls(commands.Cog):
    def __init__(self, bot):
        self.bot = bot
        self.controls_channel_id = int(os.getenv('CONTROLS_CHANNEL_ID'))
        self.default_role_id = int(os.getenv('DEFAULT_ROLE_ID'))
        self.payload = None

    async def is_new_reaction(self):
        guild = self.bot.get_guild(self.payload.guild_id)
        channel = self.bot.get_channel(self.controls_channel_id)
        message = await channel.fetch_message(self.payload.message_id)

        for r in message.reactions:
            if isinstance(r.emoji, str) and r.emoji == self.payload.emoji.name:
                reaction = r
            elif not isinstance(r.emoji, str) and r.emoji.name == self.payload.emoji.name:
                reaction = r

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

    async def toggle_control_panel(self):
        channel = self.bot.get_channel(self.controls_channel_id)
        for o in channel.overwrites_for(self.payload.member.guild.default_role):
            if o[0] == 'read_messages':
                await channel.set_permissions(self.payload.member.guild.default_role, read_messages=not o[1])

    @commands.Cog.listener()
    async def on_raw_reaction_add(self, payload):
        self.payload = payload

        if (payload.channel_id != self.controls_channel_id or
                payload.member.bot or
                await self.is_new_reaction()):
            if (payload.channel_id == self.controls_channel_id and
                    not payload.member.bot and
                    await self.is_new_reaction()):
                await self.remove_reaction()
            return

        await self.remove_reaction()
        if payload.emoji.name == '🗳':
            await self.bot.get_cog('Poll').start(payload)
        elif payload.emoji.name == '🔀':
            await self.bot.get_cog('Poll').start_common(payload)
        elif await self.is_game_emoji():
            await self.bot.get_cog('Games').update(payload)

        if await self.is_not_owner():
            return
        elif (payload.emoji.name == '🌴' or
                payload.emoji.name == '🌽' or
                payload.emoji.name == '🐊' or
                payload.emoji.name == '🗽'):
            await self.bot.get_cog('Region').change(payload)
        elif payload.emoji.name == '📢':
            await self.bot.get_cog('Priority').toggle(payload)
        elif payload.emoji.name == '👁':
            await self.toggle_control_panel()
        elif payload.emoji.name == '👋':
            await self.bot.get_cog('Hello').toggle()

    @commands.command(aliases=['c'])
    @commands.guild_only()
    @commands.is_owner()
    async def controls(self, ctx):
        if ctx.channel.id != self.controls_channel_id:
            return

        messages = await ctx.channel.history().flatten()
        await ctx.channel.delete_messages(messages)

        await ctx.send('**-----------------------------------**')
        await ctx.send(file=discord.File('assets/header.png'))
        await ctx.send('**PURDOODOO GLOBAL\n* now with social distancing! ***\n\n'
                       ':small_orange_diamond:   there are no rules I don\'t give a shit')

        await ctx.send('**-----------------------------------\nADMIN CONTROLS**')
        message = await ctx.send('server region')
        await message.add_reaction('🌴')
        await message.add_reaction('🌽')
        await message.add_reaction('🐊')
        await message.add_reaction('🗽')
        message = await ctx.send('toggles')
        await message.add_reaction('📢')
        await message.add_reaction('👁')
        await message.add_reaction('👋')

        await ctx.send('**-----------------------------------\nGENERAL CONTROLS**')
        message = await ctx.send('start a poll of all games or ones you have in common')
        await message.add_reaction('🗳')
        await message.add_reaction('🔀')
        message = await ctx.send('add or remove games from your roles')
        for r in reversed(ctx.guild.roles[1:ctx.guild.roles.index(ctx.guild.get_role(self.default_role_id))]):
            await message.add_reaction(discord.utils.get(ctx.guild.emojis, name=r.name))

        await ctx.send('**-----------------------------------\nSHOP INFO**\n'
                       'buy an item by typing `hey mitch <item>`\n\n'
                       '**🔇 mute (2160 dining dollars)** - server mute a user for a minute\n'
                       '**🔇 mute random (1080 dining dollars)** - server mute a random user for a minute\n'
                       '**✏ rename (1080 dining dollars)** - rename a user\n')
        await ctx.send('**-----------------------------------**')

