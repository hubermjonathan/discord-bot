from time import time
import random
import asyncio
import discord
from discord.ext import tasks, commands
import constants


def setup(bot):
    bot.add_cog(Shop(bot))


class Shop(commands.Cog):
    def __init__(self, bot):
        self.bot = bot

    @commands.Cog.listener()
    async def on_voice_state_update(self, member, before, after):
        if not member.bot and before.channel is None and member.voice.mute:
            last_muted = float(constants.REDIS.hget(member.id, 'last_muted').decode('utf-8'))
            if time() - last_muted > 60:
                await member.edit(mute=False)

    @commands.command()
    async def shop(self, ctx):
        embed = discord.Embed(title='shop items',
            description='**🎲 random mute (350 dining dollars)** - `rmute` - server mute someone random for a minute\n'
                '**🔇 mute (700 dining dollars)** - `mute [person]` - server mute someone for a minute\n'
                '**✏ rename (300 dining dollars)** - `rename [person] [name]` - rename someone\n'
                '**🛡 mute shield (1000 dining dollars)** - `shield` - block mutes for 30 minutes\n',
        )
        await ctx.send(embed=embed)

    @commands.command()
    @commands.guild_only()
    async def rmute(self, ctx):
        old_mapping = constants.REDIS.hgetall(ctx.author.id)
        balance = float(old_mapping[b'points'].decode('utf-8'))
        last_rmute = float(old_mapping[b'last_rmute'].decode('utf-8'))
        if balance < 350:
            await ctx.message.add_reaction(constants.NOT_ENOUGH_POINTS)
            return
        elif time() - last_rmute < 60:
            await ctx.message.add_reaction(constants.ON_COOLDOWN)
            return

        voice_channel = None
        for vc in ctx.guild.voice_channels:
            if ctx.author in vc.members:
                voice_channel = vc
                if len(vc.members) < 3:
                    await ctx.message.add_reaction(constants.DENY)
                    return
                break
        if voice_channel is None:
            await ctx.message.add_reaction(constants.NOT_IN_VC)
            return

        new_mapping = {
            'points': balance - 350,
            'last_rmute': time()
        }
        constants.REDIS.hset(ctx.author.id, mapping=new_mapping)

        member = random.choice(voice_channel.members)
        if member.voice.mute:
            await ctx.message.add_reaction(constants.DENY)
            return

        last_shield = float(constants.REDIS.hget(member.id, 'last_shield').decode('utf-8'))
        if time() - last_shield < 1800:
            await ctx.message.add_reaction(constants.SHIELD)
            return

        await ctx.message.add_reaction(constants.CONFIRM)
        constants.REDIS.hset(member.id, 'last_muted', time())
        await member.edit(mute=True)
        await asyncio.sleep(60)
        await member.edit(mute=False)

    @commands.command()
    @commands.guild_only()
    async def mute(self, ctx, member: discord.Member):
        old_mapping = constants.REDIS.hgetall(ctx.author.id)
        balance = float(old_mapping[b'points'].decode('utf-8'))
        last_mute = float(old_mapping[b'last_mute'].decode('utf-8'))
        if balance < 700:
            await ctx.message.add_reaction(constants.NOT_ENOUGH_POINTS)
            return
        elif time() - last_mute < 300:
            await ctx.message.add_reaction(constants.ON_COOLDOWN)
            return
        elif member.voice is None:
            await ctx.message.add_reaction(constants.NOT_IN_VC)
            return
        elif member.voice.mute:
            await ctx.message.add_reaction(constants.DENY)
            return

        new_mapping = {
            'points': balance - 700,
            'last_mute': time()
        }
        constants.REDIS.hset(ctx.author.id, mapping=new_mapping)

        last_shield = float(constants.REDIS.hget(member.id, 'last_shield').decode('utf-8'))
        if time() - last_shield < 1800:
            await ctx.message.add_reaction(constants.SHIELD)
            return

        await ctx.message.add_reaction(constants.CONFIRM)
        constants.REDIS.hset(member.id, 'last_muted', time())
        await member.edit(mute=True)
        await asyncio.sleep(60)
        await member.edit(mute=False)

    @commands.command()
    @commands.guild_only()
    async def rename(self, ctx, member: discord.Member, name):
        balance = float(constants.REDIS.hget(ctx.author.id, 'points').decode('utf-8'))
        if balance < 300:
            await ctx.message.add_reaction(constants.NOT_ENOUGH_POINTS)
            return
        elif guild.get_role(constants.DEFAULT_ROLE_ID) in m.roles:
            await ctx.message.add_reaction(constants.DENY)
            return

        constants.REDIS.hset(ctx.author.id, 'points', balance - 300)

        await ctx.message.add_reaction(constants.CONFIRM)
        await member.edit(nick=name)

    @commands.command()
    @commands.dm_only()
    async def shield(self, ctx):
        old_mapping = constants.REDIS.hgetall(ctx.author.id)
        balance = float(old_mapping[b'points'].decode('utf-8'))
        last_shield = float(old_mapping[b'last_shield'].decode('utf-8'))
        if balance < 1000:
            await ctx.message.add_reaction(constants.NOT_ENOUGH_POINTS)
            return
        elif time() - last_shield < 1800:
            await ctx.message.add_reaction(constants.ON_COOLDOWN)
            return

        new_mapping = {
            'points': balance - 1000,
            'last_shield': time()
        }
        constants.REDIS.hset(ctx.author.id, mapping=new_mapping)

        await ctx.message.add_reaction(constants.CONFIRM)
