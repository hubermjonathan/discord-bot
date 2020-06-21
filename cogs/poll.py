from datetime import datetime, timedelta
import discord
from discord.ext import commands
import constants


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
                await ctx.message.add_reaction(constants.ON_COOLDOWN)
                return
            self.last_poll = datetime.now()

            embed = discord.Embed(title='game poll', description='what game do you want to play?')
            message = await self.bot.get_channel(constants.CHAT_CHANNEL_ID).send(embed=embed)

            for r in reversed(ctx.guild.roles[1:ctx.guild.roles.index(ctx.guild.get_role(constants.DEFAULT_ROLE_ID))]):
                await message.add_reaction(discord.utils.get(ctx.guild.emojis, name=r.name))

            await ctx.message.add_reaction(constants.CONFIRM)

    @poll.command(aliases=['c'])
    async def common(self, ctx):
        if datetime.now() - self.last_poll < timedelta(minutes=1):
            await ctx.message.add_reaction(constants.ON_COOLDOWN)
            return
        self.last_poll = datetime.now()

        voice_channel = None
        for vc in ctx.guild.voice_channels:
            if ctx.author in vc.members:
                voice_channel = vc
                break
        if voice_channel is None:
            await ctx.message.add_reaction(constants.NOT_IN_VC)
            return
        members = voice_channel.members

        roles = ctx.guild.roles[1:ctx.guild.roles.index(ctx.guild.get_role(constants.DEFAULT_ROLE_ID))]
        for m in members:
            if ctx.guild.get_role(constants.PROMOTED_ROLE_ID) in m.roles:
                member_roles = m.roles[1:m.roles.index(ctx.guild.get_role(constants.PROMOTED_ROLE_ID))]
            else:
                member_roles = m.roles[1:m.roles.index(ctx.guild.get_role(constants.DEFAULT_ROLE_ID))]
            if len(member_roles) < 2:
                continue
            roles = list(set(roles) & set(member_roles))
        if len(roles) < 2:
            await ctx.message.add_reaction(constants.DENY)
            return

        embed = discord.Embed(title='game poll', description='what game do you want to play?')
        message = await self.bot.get_channel(constants.CHAT_CHANNEL_ID).send(embed=embed)

        for r in roles:
            await message.add_reaction(discord.utils.get(ctx.guild.emojis, name=r.name))

        await ctx.message.add_reaction(constants.CONFIRM)
