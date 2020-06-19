from discord.ext import commands


def setup(bot):
    bot.add_cog(Errors(bot))


class Errors(commands.Cog):
    def __init__(self, bot):
        self.bot = bot

    @commands.Cog.listener()
    async def on_command_error(self, ctx, error):
        if isinstance(error, commands.PrivateMessageOnly):
            return
        elif isinstance(error, commands.NoPrivateMessage):
            return
        else:
            print(f'BOT ERROR: {error}')
