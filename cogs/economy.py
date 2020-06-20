from time import time
from discord.ext import tasks, commands
import globals


def setup(bot):
    economy = Economy(bot)
    bot.add_cog(economy)
    economy.count_points.start()


class Economy(commands.Cog):
    def __init__(self, bot):
        self.bot = bot

    @tasks.loop(minutes=10)
    async def count_points(self):
        guild = self.bot.get_guild(globals.guild_id)
        for vc in guild.voice_channels:
            if len(vc.members) < 2:
                continue

            for m in vc.members:
                if m.bot or guild.get_role(globals.default_role_id) in m.roles:
                    continue

                if globals.redis.hget(m.id, 'points') is None:
                    globals.redis.hset(m.id, 'points', 0)
                    globals.redis.hset(m.id, 'total_points', 0)
                    globals.redis.hset(m.id, 'last_mute', time())
                    globals.redis.hset(m.id, 'last_rmute', time())
                    globals.redis.hset(m.id, 'last_muted', time())

                old_points = float(globals.redis.hget(m.id, 'points').decode('utf-8'))
                new_points = old_points + (82.5/(60/10))
                globals.redis.hset(m.id, 'points', new_points)
                old_total_points = float(globals.redis.hget(m.id, 'total_points').decode('utf-8'))
                new_total_points = old_total_points + (82.5/(60/10))
                globals.redis.hset(m.id, 'total_points', new_total_points)

                if globals.redis.hget(m.id, 'points_message_id') is None:
                    await m.create_dm()
                    message = await m.send(f'your current balance is **{new_points} dining dollars** 💵')
                    globals.redis.hset(m.id, 'points_message_id', message.id)

                points_message_id = int(globals.redis.hget(m.id, 'points_message_id').decode('utf-8'))
                points_message = await m.dm_channel.fetch_message(points_message_id)
                await points_message.edit(content=f'your current balance is **{new_points} dining dollars** 💵', suppress=True)

    @count_points.before_loop
    async def before_loop(self):
        await self.bot.wait_until_ready()

    @commands.group(aliases=['e'])
    async def economy(self, ctx):
        if ctx.invoked_subcommand is None:
            await ctx.message.add_reaction('❔')

    @economy.command()
    async def reset(self, ctx):
        guild = self.bot.get_guild(globals.guild_id)
        for m in guild.get_role(globals.promoted_role_id).members:
            globals.redis.delete(m.id)

        await ctx.message.add_reaction('👍')
