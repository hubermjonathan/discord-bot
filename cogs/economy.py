from time import time
import discord
from discord.ext import tasks, commands
import constants


def setup(bot):
    economy = Economy(bot)
    bot.add_cog(economy)
    economy.distribute_points.start()


class Economy(commands.Cog):
    def __init__(self, bot):
        self.bot = bot
        self.double_enabled = False

    @tasks.loop(minutes=10)
    async def distribute_points(self):
        guild = self.bot.get_guild(constants.GUILD_ID)
        for vc in guild.voice_channels:
            if len(vc.members) < 2:
                continue

            for m in vc.members:
                if m.bot or guild.get_role(constants.DEFAULT_ROLE_ID) in m.roles:
                    continue

                old_mapping = constants.REDIS.hgetall(m.id)
                if b'points' not in old_mapping:
                    defaults = {
                        'points': 0,
                        'total_points': 0,
                        'last_rmute': time(),
                        'last_mute': time(),
                        'last_shield': time(),
                        'last_muted': time()
                    }
                    constants.REDIS.hset(m.id, mapping=defaults)

                old_points = float(old_mapping[b'points'].decode('utf-8'))
                old_total_points = float(old_mapping[b'total_points'].decode('utf-8'))
                multiplier = 2 if self.double_enabled else 1
                new_mapping = {
                    'points': old_points + (82.5*multiplier/(60/10)),
                    'total_points': old_total_points + (82.5*multiplier/(60/10))
                }
                constants.REDIS.hset(m.id, mapping=new_mapping)

    @distribute_points.before_loop
    async def before_loop(self):
        await self.bot.wait_until_ready()

    @commands.group(aliases=['e'])
    async def economy(self, ctx):
        if ctx.invoked_subcommand is None:
            await ctx.message.add_reaction(constants.NO_COMMAND)

    @economy.command()
    @commands.is_owner()
    async def double(self, ctx):
        self.double_enabled = not self.double_enabled
        await ctx.message.add_reaction('🟢' if self.double_enabled else '🔴')

    @economy.command()
    @commands.is_owner()
    async def reset(self, ctx):
        constants.REDIS.flushdb()

        guild = self.bot.get_guild(constants.GUILD_ID)
        for m in guild.get_role(constants.PROMOTED_ROLE_ID).members:
            defaults = {
                'points': 0,
                'total_points': 0,
                'last_rmute': time(),
                'last_mute': time(),
                'last_shield': time(),
                'last_muted': time()
            }
            constants.REDIS.hset(m.id, mapping=defaults)

        await ctx.message.add_reaction(constants.CONFIRM)

    @economy.command()
    @commands.is_owner()
    async def repair(self, ctx):
        guild = self.bot.get_guild(constants.GUILD_ID)
        for m in guild.get_role(constants.PROMOTED_ROLE_ID).members:
            old_mapping = constants.REDIS.hgetall(m.id)
            new_mapping = {}

            if b'points' not in old_mapping:
                new_mapping['points'] = 0

            if b'total_points' not in old_mapping:
                new_mapping['total_points'] = 0

            if b'last_rmute' not in old_mapping:
                new_mapping['last_rmute'] = time()

            if b'last_mute' not in old_mapping:
                new_mapping['last_mute'] = time()

            if b'last_shield' not in old_mapping:
                new_mapping['last_shield'] = time()

            if b'last_muted' not in old_mapping:
                new_mapping['last_muted'] = time()

            if new_mapping:
                constants.REDIS.hset(m.id, mapping=new_mapping)

        await ctx.message.add_reaction(constants.CONFIRM)

    @economy.command()
    @commands.is_owner()
    async def set(self, ctx, member: discord.Member, amount):
        mapping = {
            'points': float(amount),
            'total_points': float(amount)
        }
        constants.REDIS.hset(member.id, mapping=mapping)

        embed = discord.Embed(title='balance', description=f'your balance has been manually set, it is now **{float(amount)} dining dollars** 💵')
        await member.send(embed=embed)

        await ctx.message.add_reaction(constants.CONFIRM)

    @commands.command(aliases=['b', 'bal'])
    async def balance(self, ctx):
        balance = float(constants.REDIS.hget(ctx.author.id, 'points').decode('utf-8'))
        embed = discord.Embed(title='balance', description=f'your current balance is **{balance} dining dollars** 💵')
        await ctx.author.send(embed=embed)
        await ctx.message.add_reaction(constants.CONFIRM)

    @commands.command()
    async def send(self, ctx, member: discord.Member, amount):
        balance = float(constants.REDIS.hget(ctx.author.id, 'points').decode('utf-8'))
        if ctx.author.id == member.id:
            await ctx.message.add_reaction(constants.DENY)
            return
        if balance < float(amount):
            await ctx.message.add_reaction(constants.NOT_ENOUGH_POINTS)
            return

        constants.REDIS.hset(ctx.author.id, 'points', balance - float(amount))

        old_mapping = constants.REDIS.hgetall(member.id)
        old_points = float(old_mapping[b'points'].decode('utf-8'))
        old_total_points = float(old_mapping[b'total_points'].decode('utf-8'))
        new_mapping = {
            'points': old_points + float(amount),
            'total_points': old_total_points + float(amount)
        }
        constants.REDIS.hset(member.id, mapping=new_mapping)

        embed = discord.Embed(title='balance', description=f'{ctx.author.display_name} sent you dining dollars! your current balance is now **{old_points + float(amount)} dining dollars** 💵')
        await member.send(embed=embed)

        await ctx.message.add_reaction(constants.CONFIRM)
