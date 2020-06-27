import discord
from discord.ext import commands


def setup(bot):
    bot.add_cog(Help(bot))


class Help(commands.Cog):
    def __init__(self, bot):
        self.bot = bot

    @commands.group()
    async def help(self, ctx):
        if ctx.invoked_subcommand is None:
            embed = discord.Embed(title='commands',
                description='**ex: command (alias) - **`command_name [required_argument] {optional_argument}`\n'
                    'check your balance - `balance`\n'
                    'send someone money - `send [person] [amount]`\n'
                    'show the shop - `shop`\n',
            )

            await ctx.send(embed=embed)
