import discord
from discord.ext import commands


class Poll(commands.Cog):
    def __init__(self, bot, default_role_id, promoted_role_id):
        self.bot = bot
        self.default_role_id = default_role_id
        self.promoted_role_id = promoted_role_id

    @commands.command()
    @commands.guild_only()
    async def poll(self, ctx):
        # get the members
        voice_channel = None
        for vc in ctx.guild.voice_channels:
            if ctx.author in vc.members:
                voice_channel = vc
                break
        if voice_channel is None:
            raise commands.CommandError
        members = voice_channel.members

        # get the games
        roles = ctx.guild.roles[1:ctx.guild.roles.index(ctx.guild.get_role(self.promoted_role_id))]
        for m in members:
            if ctx.guild.get_role(self.promoted_role_id) in m.roles:
                member_roles = m.roles[1:m.roles.index(ctx.guild.get_role(self.promoted_role_id))]
                roles = list(set(roles) & set(member_roles))
        if len(roles) < 2:
            raise commands.CommandError

        # add the reactions
        for r in roles:
            await ctx.message.add_reaction(discord.utils.get(ctx.guild.emojis, name=r.name))

    @commands.command()
    @commands.guild_only()
    async def poll_all(self, ctx):
        # add the reactions
        for r in reversed(ctx.guild.roles[1:ctx.guild.roles.index(ctx.guild.get_role(self.default_role_id))]):
            await ctx.message.add_reaction(discord.utils.get(ctx.guild.emojis, name=r.name))
