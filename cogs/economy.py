from time import time
import discord
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
        guild = self.bot.get_guild(globals.GUILD_ID)
        for vc in guild.voice_channels:
            if len(vc.members) < 2:
                continue

            for m in vc.members:
                if m.bot or guild.get_role(globals.DEFAULT_ROLE_ID) in m.roles:
                    continue

                if globals.REDIS.hget(m.id, 'points') is None:
                    globals.REDIS.hset(m.id, 'points', 0)
                    globals.REDIS.hset(m.id, 'total_points', 0)
                    globals.REDIS.hset(m.id, 'last_rmute', time())
                    globals.REDIS.hset(m.id, 'last_mute', time())
                    globals.REDIS.hset(m.id, 'last_shield', time())
                    globals.REDIS.hset(m.id, 'last_muted', time())

                old_points = float(globals.REDIS.hget(m.id, 'points').decode('utf-8'))
                new_points = old_points + (82.5/(60/10))
                globals.REDIS.hset(m.id, 'points', new_points)
                old_total_points = float(globals.REDIS.hget(m.id, 'total_points').decode('utf-8'))
                new_total_points = old_total_points + (82.5/(60/10))
                globals.REDIS.hset(m.id, 'total_points', new_total_points)

    @count_points.before_loop
    async def before_loop(self):
        await self.bot.wait_until_ready()

    @commands.group(aliases=['e'])
    async def economy(self, ctx):
        if ctx.invoked_subcommand is None:
            await ctx.message.add_reaction('❔')

    @economy.command(aliases=['b'])
    async def balance(self, ctx):
        balance = float(globals.REDIS.hget(ctx.author.id, 'points').decode('utf-8'))
        embed = discord.Embed(title='balance', description=f'your current balance is **{balance} dining dollars** 💵')
        await ctx.author.send(embed=embed)
        await ctx.message.add_reaction('👍')

    @economy.command()
    async def send(self, ctx, member: discord.Member, amount):
        balance = float(globals.REDIS.hget(ctx.author.id, 'points').decode('utf-8'))
        if ctx.author.id == member.id:
            await ctx.message.add_reaction('👎')
            return
        if balance < float(amount):
            await ctx.message.add_reaction('💵')
            return

        globals.REDIS.hset(ctx.author.id, 'points', balance - float(amount))

        old_points = float(globals.REDIS.hget(member.id, 'points').decode('utf-8'))
        new_points = old_points + float(amount)
        globals.REDIS.hset(member.id, 'points', new_points)
        old_total_points = float(globals.REDIS.hget(member.id, 'total_points').decode('utf-8'))
        globals.REDIS.hset(member.id, 'total_points', old_total_points + float(amount))

        embed = discord.Embed(title='balance', description=f'{ctx.author.display_name} sent you dining dollars! your current balance is now **{new_points} dining dollars** 💵')
        await member.send(embed=embed)

        await ctx.message.add_reaction('👍')

    @economy.command()
    @commands.is_owner()
    async def reset(self, ctx):
        guild = self.bot.get_guild(globals.GUILD_ID)
        for m in guild.get_role(globals.PROMOTED_ROLE_ID).members:
            globals.REDIS.delete(m.id)
            globals.REDIS.hset(m.id, 'points', 0)
            globals.REDIS.hset(m.id, 'total_points', 0)
            globals.REDIS.hset(m.id, 'last_rmute', time())
            globals.REDIS.hset(m.id, 'last_mute', time())
            globals.REDIS.hset(m.id, 'last_shield', time())
            globals.REDIS.hset(m.id, 'last_muted', time())

        await ctx.message.add_reaction('👍')

    @economy.command()
    @commands.is_owner()
    async def repair(self, ctx):
        guild = self.bot.get_guild(globals.GUILD_ID)
        for m in guild.get_role(globals.PROMOTED_ROLE_ID).members:
            if globals.REDIS.hget(m.id, 'points') is None:
                globals.REDIS.hset(m.id, 'points', 0)

            if globals.REDIS.hget(m.id, 'total_points') is None:
                globals.REDIS.hset(m.id, 'total_points', 0)

            if globals.REDIS.hget(m.id, 'last_rmute') is None:
                globals.REDIS.hset(m.id, 'last_rmute', time())

            if globals.REDIS.hget(m.id, 'last_mute') is None:
                globals.REDIS.hset(m.id, 'last_mute', time())

            if globals.REDIS.hget(m.id, 'last_shield') is None:
                globals.REDIS.hset(m.id, 'last_shield', time())

            if globals.REDIS.hget(m.id, 'last_muted') is None:
                globals.REDIS.hset(m.id, 'last_muted', time())

        await ctx.message.add_reaction('👍')

    @economy.command()
    @commands.is_owner()
    async def set(self, ctx, member: discord.Member, amount):
        globals.REDIS.hset(member.id, 'points', float(amount))

        embed = discord.Embed(title='balance', description=f'your balance has been manually set, it is now **{float(amount)} dining dollars** 💵')
        await member.send(embed=embed)

        await ctx.message.add_reaction('👍')
