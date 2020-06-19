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
        if ctx.invoked_subcommand is None:
            await ctx.send('**🎲 random mute (1080 dining dollars)** - `rmute` - server mute someone random for a minute\n'
                '**🔇 mute (2160 dining dollars)** - `mute <person>` - server mute someone for a minute\n'
                '**✏ rename (1080 dining dollars)** - `rename <person> <name>` - rename someone\n')

    @shop.command()
    async def mute(self, ctx, member: discord.Member):
        balance = float(self.redis.hget(ctx.author.id, 'points').decode('utf-8'))
        last_mute = float(self.redis.hget(ctx.author.id, 'last_mute').decode('utf-8'))
        if balance < 2160:
            await ctx.message.add_reaction('💵')
            return
        elif time() - last_mute < 60:
            await ctx.message.add_reaction('⏲')
            return
        elif member.voice.mute:
            await ctx.message.add_reaction('👎')
            return

        old_points = float(self.redis.hget(member.id, 'points').decode('utf-8'))
        self.redis.hset(ctx.author.id, 'points', old_points - 2160)
        self.redis.hset(ctx.author.id, 'last_mute', time())
        self.redis.hset(member.id, 'last_muted', time())

        await member.edit(mute=True)
        await ctx.message.add_reaction('👍')
        await asyncio.sleep(60)
        await member.edit(mute=False)
