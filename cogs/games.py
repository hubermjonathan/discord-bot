import discord
from discord.ext import commands


def setup(bot):
    bot.add_cog(Games(bot))


class Games(commands.Cog):
    def __init__(self, bot):
        self.bot = bot

    async def update(self, payload):
        # find the role
        role = discord.utils.get(payload.member.guild.roles, name=payload.emoji.name)

        # add or remove the role
        if role in payload.member.roles:
            await payload.member.remove_roles(role)
        else:
            await payload.member.add_roles(role)
