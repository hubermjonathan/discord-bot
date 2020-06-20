import os
from time import time
import redis
from discord.ext import tasks, commands


def setup(bot):
    economy = Economy(bot)
    bot.add_cog(economy)
    economy.count_points.start()


class Economy(commands.Cog):
    def __init__(self, bot):
        self.bot = bot
        self.redis = redis.from_url(os.environ.get('REDIS_URL'))
        self.guild_id = int(os.getenv('GUILD_ID'))
        self.default_role_id = int(os.getenv('DEFAULT_ROLE_ID'))

        self.redis.delete(self.bot.owner_id)

    @tasks.loop(minutes=10)
    async def count_points(self):
        guild = self.bot.get_guild(self.guild_id)
        for vc in guild.voice_channels:
            if len(vc.members) < 2:
                continue

            for m in vc.members:
                if m.bot or guild.get_role(self.default_role_id) in m.roles or m.id != self.bot.owner_id:
                    continue

                if self.redis.hget(m.id, 'points') is None:
                    self.redis.hset(m.id, 'points', 0)
                    self.redis.hset(m.id, 'last_mute', time())
                    self.redis.hset(m.id, 'last_rmute', time())
                    self.redis.hset(m.id, 'last_muted', time())

                old_points = float(self.redis.hget(m.id, 'points').decode('utf-8'))
                new_points = old_points + (82.5/(60/10))
                self.redis.hset(m.id, 'points', new_points)

                if self.redis.hget(m.id, 'points_message_id') is None:
                    await m.create_dm()
                    message = await m.send(f'your current balance is **{new_points} dining dollars** 💵')
                    self.redis.hset(m.id, 'points_message_id', message.id)

                points_message_id = int(self.redis.hget(m.id, 'points_message_id').decode('utf-8'))
                points_message = await m.dm_channel.fetch_message(points_message_id)
                await points_message.edit(content=f'your current balance is **{new_points} dining dollars** 💵', suppress=True)

    @count_points.before_loop
    async def before_loop(self):
        await self.bot.wait_until_ready()
