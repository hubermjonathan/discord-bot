import discord
from discord.ext import commands


def setup(bot):
    bot.add_cog(Games(bot))


class Games(commands.Cog):
    def __init__(self, bot):
        self.bot = bot

    async def update(self, payload):
        game = discord.utils.get(payload.member.guild.roles, name=payload.emoji.name)
        if game in payload.member.roles:
            await payload.member.remove_roles(game)
        else:
            await payload.member.add_roles(game)
