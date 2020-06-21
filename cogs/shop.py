from time import time
import random
import asyncio
import discord
from discord.ext import tasks, commands
import globals


def setup(bot):
    bot.add_cog(Shop(bot))


class Shop(commands.Cog):
    def __init__(self, bot):
        self.bot = bot

    @commands.Cog.listener()
    async def on_voice_state_update(self, member, before, after):
        if not member.bot and before.channel is None and member.voice.mute:
            last_muted = float(globals.REDIS.hget(member.id, 'last_muted').decode('utf-8'))
            if time() - last_muted > 60:
                await member.edit(mute=False)

    @commands.group(aliases=['s'])
    async def shop(self, ctx):
        if ctx.invoked_subcommand is None:
            embed = discord.Embed(title='shop items',
                description='**🎲 random mute (175 dining dollars)** - `rmute` - server mute someone random for a minute\n'
                    '**🔇 mute (350 dining dollars)** - `mute [person]` - server mute someone for a minute\n'
                    '**✏ rename (150 dining dollars)** - `rename [person] [name]` - rename someone\n'
                    '**🛡 mute shield (500 dining dollars)** - `shield` - block mutes for 30 minutes\n',
            )
            await ctx.send(embed=embed)

    @shop.command()
    @commands.guild_only()
    async def rmute(self, ctx):
        balance = float(globals.REDIS.hget(ctx.author.id, 'points').decode('utf-8'))
        last_mute = float(globals.REDIS.hget(ctx.author.id, 'last_rmute').decode('utf-8'))
        if balance < 175:
            await ctx.message.add_reaction('💵')
            return
        elif time() - last_rmute < 60:
            await ctx.message.add_reaction('⏲')
            return

        voice_channel = None
        for vc in ctx.guild.voice_channels:
            if ctx.author in vc.members:
                voice_channel = vc
                if len(vc.members) < 3:
                    await ctx.message.add_reaction('👥')
                    return
                break
        if voice_channel is None:
            await ctx.message.add_reaction('🔇')
            return

        member = random.choice(voice_channel.members)
        if member.voice.mute:
            await ctx.message.add_reaction('👎')
            return

        globals.REDIS.hset(ctx.author.id, 'points', balance - 175)
        globals.REDIS.hset(ctx.author.id, 'last_rmute', time())

        last_shield = float(globals.REDIS.hget(member.id, 'last_shield').decode('utf-8'))
        if time() - last_shield < 1800:
            await ctx.message.add_reaction('🛡')
            return

        await ctx.message.add_reaction('👍')
        globals.REDIS.hset(member.id, 'last_muted', time())
        await member.edit(mute=True)
        await asyncio.sleep(60)
        await member.edit(mute=False)

    @shop.command()
    @commands.guild_only()
    async def mute(self, ctx, member: discord.Member):
        balance = float(globals.REDIS.hget(ctx.author.id, 'points').decode('utf-8'))
        last_mute = float(globals.REDIS.hget(ctx.author.id, 'last_mute').decode('utf-8'))
        if balance < 350:
            await ctx.message.add_reaction('💵')
            return
        elif time() - last_mute < 300:
            await ctx.message.add_reaction('⏲')
            return
        elif member.voice.mute:
            await ctx.message.add_reaction('👎')
            return

        globals.REDIS.hset(ctx.author.id, 'points', balance - 350)
        globals.REDIS.hset(ctx.author.id, 'last_mute', time())

        last_shield = float(globals.REDIS.hget(member.id, 'last_shield').decode('utf-8'))
        if time() - last_shield < 1800:
            await ctx.message.add_reaction('🛡')
            return

        await ctx.message.add_reaction('👍')
        globals.REDIS.hset(member.id, 'last_muted', time())
        await member.edit(mute=True)
        await asyncio.sleep(60)
        await member.edit(mute=False)

    @shop.command()
    @commands.dm_only()
    async def shield(self, ctx):
        balance = float(globals.REDIS.hget(ctx.author.id, 'points').decode('utf-8'))
        last_shield = float(globals.REDIS.hget(ctx.author.id, 'last_shield').decode('utf-8'))
        if balance < 500:
            await ctx.message.add_reaction('💵')
            return
        elif time() - last_shield < 1800:
            await ctx.message.add_reaction('⏲')
            return

        await ctx.message.add_reaction('👍')
        globals.REDIS.hset(ctx.author.id, 'points', balance - 1800)
        globals.REDIS.hset(ctx.author.id, 'last_shield', time())

    @shop.command()
    @commands.guild_only()
    async def rename(self, ctx, member: discord.Member, name):
        balance = float(globals.REDIS.hget(ctx.author.id, 'points').decode('utf-8'))
        if balance < 150:
            await ctx.message.add_reaction('💵')
            return

        await ctx.message.add_reaction('👍')
        globals.REDIS.hset(ctx.author.id, 'points', balance - 150)

        await member.edit(nick=name)
