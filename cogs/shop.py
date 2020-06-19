import os
from time import time
import asyncio
import redis
import discord
from discord.ext import tasks, commands


def setup(bot):
    bot.add_cog(Shop(bot))


class Shop(commands.Cog):
    def __init__(self, bot):
        self.bot = bot
        self.redis = redis.from_url(os.environ.get('REDIS_URL'))

    @commands.Cog.listener()
    async def on_voice_state_update(self, member, before, after):
        if not member.bot and before.channel is None and member.voice.mute:
            last_muted = float(self.redis.hget(member.id, 'last_muted').decode('utf-8'))
            if time() - last_muted > 60:
                await member.edit(mute=False)

    @commands.group(aliases=['s'])
    async def shop(self, ctx):
        pass

    @shop.command()
    async def mute(self, ctx, member: discord.Member):
        balance = float(self.redis.hget(ctx.author.id, 'points').decode('utf-8'))
        last_mute = float(self.redis.hget(ctx.author.id, 'last_mute').decode('utf-8'))
        if balance < 2160 or time() - last_mute < 60 or member.voice.mute:
            await ctx.message.add_reaction('👎')
            return
        await ctx.message.add_reaction('👍')

        old_points = float(self.redis.hget(member.id, 'points').decode('utf-8'))
        self.redis.hset(ctx.author.id, 'points', old_points - 2160)
        self.redis.hset(ctx.author.id, 'last_mute', time())
        self.redis.hset(member.id, 'last_muted', time())

        await member.edit(mute=True)
        await asyncio.sleep(60)
        await member.edit(mute=False)
