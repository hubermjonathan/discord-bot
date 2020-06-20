from datetime import datetime, timedelta
import discord
from discord.ext import commands


def setup(bot):
    bot.add_cog(Poll(bot))


class Poll(commands.Cog):
    def __init__(self, bot):
        self.bot = bot
        self.last_poll = datetime.now()

    @commands.group(aliases=['p'])
    async def poll(self, ctx):
        if ctx.invoked_subcommand is None:
            if datetime.now() - self.last_poll < timedelta(minutes=1):
                await ctx.message.add_reaction('⏲')
                return
            self.last_poll = datetime.now()

            message = await self.bot.get_channel(globals.chat_channel_id).send('what game do you want to play?')
            for r in reversed(ctx.guild.roles[1:ctx.guild.roles.index(ctx.guild.get_role(self.default_role_id))]):
                await message.add_reaction(discord.utils.get(ctx.guild.emojis, name=r.name))

            await ctx.message.add_reaction('👍')

    @poll.command(aliases=['c'])
    async def common(self, ctx):
        if datetime.now() - self.last_poll < timedelta(minutes=1):
            await ctx.message.add_reaction('⏲')
            return
        self.last_poll = datetime.now()

        voice_channel = None
        for vc in ctx.guild.voice_channels:
            if ctx.author in vc.members:
                voice_channel = vc
                break
        if voice_channel is None:
            await ctx.message.add_reaction('🔇')
            return
        members = voice_channel.members

        roles = ctx.guild.roles[1:ctx.guild.roles.index(ctx.guild.get_role(self.default_role_id))]
        for m in members:
            if ctx.guild.get_role(globals.promoted_role_id) in m.roles:
                member_roles = m.roles[1:m.roles.index(ctx.guild.get_role(globals.promoted_role_id))]
            else:
                member_roles = m.roles[1:m.roles.index(ctx.guild.get_role(self.default_role_id))]
            if len(member_roles) < 2:
                continue
            roles = list(set(roles) & set(member_roles))
        if len(roles) < 2:
            await ctx.message.add_reaction('🤏')
            return

        message = await self.bot.get_channel(globals.chat_channel_id).send('what game do you want to play?')

        for r in roles:
            await message.add_reaction(discord.utils.get(ctx.guild.emojis, name=r.name))

        await ctx.message.add_reaction('👍')
