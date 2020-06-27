import random
import discord
from discord.ext import tasks, commands


def setup(bot):
    status = Status(bot)
    bot.add_cog(status)
    status.change_status.start()


class Status(commands.Cog):
    def __init__(self, bot):
        self.bot = bot
        self.activities = [
            discord.Activity(type=discord.ActivityType.listening, name='Daniel\'s flame'),
            discord.Activity(type=discord.ActivityType.watching, name='Sammie\'s stream'),
            discord.Activity(type=discord.ActivityType.listening, name='Collin simp'),
            discord.Activity(type=discord.ActivityType.listening, name='Aulos Reloaded'),
            discord.Activity(type=discord.ActivityType.listening, name='Andy give bad clues'),
            discord.Activity(type=discord.ActivityType.playing, name='with my Amazon friends'),
            discord.Activity(type=discord.ActivityType.listening, name='the fight song'),
            discord.Activity(type=discord.ActivityType.playing, name='against IU'),
            discord.Activity(type=discord.ActivityType.listening, name='Jay\'s sass'),
        ]

    @tasks.loop(hours=1)
    async def change_status(self):
        await self.bot.change_presence(activity=random.choice(self.activities))

    @change_status.before_loop
    async def before_loop(self):
        await self.bot.wait_until_ready()
