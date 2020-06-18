import discord
from discord.ext import commands


def setup(bot):
    bot.add_cog(Region(bot))


class Region(commands.Cog):
    def __init__(self, bot):
        self.bot = bot

    async def change(self, payload):
        if payload.emoji.name == '🌴':
            region = discord.VoiceRegion.us_west
        elif payload.emoji.name == '🌽':
            region = discord.VoiceRegion.us_central
        elif payload.emoji.name == '🐊':
            region = discord.VoiceRegion.us_south
        elif payload.emoji.name == '🗽':
            region = discord.VoiceRegion.us_east
        await payload.member.guild.edit(region=region)
