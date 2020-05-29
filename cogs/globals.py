import discord
from discord.ext import commands


class Globals(commands.Cog):
    def __init__(self, bot):
        self.bot = bot

    @commands.Cog.listener()
    async def on_connect(self):
        print('connected to discord')

    @commands.Cog.listener()
    async def on_disconnect(self):
        print('disconnected from discord')

    @commands.Cog.listener()
    async def on_resumed(self):
        print('reconnected to discord')

    @commands.Cog.listener()
    async def on_ready(self):
        print('ready')

    @commands.Cog.listener()
    async def on_command_error(self, ctx, error):
        if isinstance(error, commands.NotOwner):
            await ctx.message.add_reaction('🔒')
        elif isinstance(error, commands.CommandNotFound):
            await ctx.message.add_reaction('❔')
        elif isinstance(error, commands.MissingRequiredArgument):
            await ctx.message.add_reaction('❔')
        elif isinstance(error, commands.PrivateMessageOnly):
            return
        elif isinstance(error, commands.NoPrivateMessage):
            return
        else:
            print(error)
            await ctx.message.add_reaction('👎')
