from discord.ext import commands


def setup(bot):
    bot.add_cog(Admin(bot))


class Admin(commands.Cog):
    def __init__(self, bot):
        self.bot = bot

    @commands.command(aliases=['e'])
    @commands.is_owner()
    async def enable(self, ctx, cog):
        self.bot.load_extension(f'cogs.{cog}')
        await ctx.message.add_reaction('👍')

    @commands.command(aliases=['d'])
    @commands.is_owner()
    async def disable(self, ctx, cog):
        self.bot.unload_extension(f'cogs.{cog}')
        await ctx.message.add_reaction('👍')

    @commands.command(aliases=['rl'])
    @commands.is_owner()
    async def reload(self, ctx, cog):
        self.bot.reload_extension(f'cogs.{cog}')
        await ctx.message.add_reaction('👍')
