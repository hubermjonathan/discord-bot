import random
from datetime import datetime, timedelta
import discord
from discord.ext import commands


def setup(bot):
    bot.add_cog(Status(bot))


class Status(commands.Cog):
    def __init__(self, bot):
        self.bot = bot
        self.last_change = datetime.now()
        self.activities = [
            discord.Activity(type=discord.ActivityType.listening, name='Daniel\'s flame'),
            discord.Activity(type=discord.ActivityType.watching, name='Sammie\'s stream'),
            discord.Activity(type=discord.ActivityType.listening, name='Collin simp'),
        ]

    @commands.Cog.listener()
    async def on_voice_state_update(self, member, before, after):
        if datetime.now() - self.last_change < timedelta(minutes=5):
            return
        self.last_change = datetime.now()

        await self.bot.change_presence(activity=random.choice(self.activities))
