import discord
from discord.ext import commands


def setup(bot):
    bot.add_cog(Help(bot))


class Help(commands.Cog):
    def __init__(self, bot):
        self.bot = bot

    @commands.group(aliases=['h'])
    async def help(self, ctx):
        if ctx.invoked_subcommand is None:
            embed = discord.Embed(title='commands',
                description='economy (e) - `economy [command]`\n'
                    'poll (p) - `poll {command}`\n'
                    'shop (s) - `shop {command}`\n',
            )
            embed.add_field(name='**economy commands**',
                value='check balance (b) - `economy balance`\n',
                inline=True
            )
            embed.add_field(name='**poll commands**',
                value='all games - `poll`\n'
                    'games in common (c) - `poll common`\n',
                inline=True
            )
            embed.add_field(name='**shop commands**',
                value='view items - `shop`\n'
                    'buy something - `shop [item]`\n',
                inline=True
            )

            await ctx.send(embed=embed)
