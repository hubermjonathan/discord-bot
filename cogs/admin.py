from discord.ext import commands


def setup(bot):
    bot.add_cog(Admin(bot))


class Admin(commands.Cog):
    def __init__(self, bot):
        self.bot = bot

    @commands.command(aliases=['l'])
    @commands.is_owner()
    async def load(self, ctx, cog):
        try:
            self.bot.load_extension(f'cogs.{cog}')
        except Exception as e:
            await ctx.message.add_reaction('👎')
            return
        await ctx.message.add_reaction('👍')

    @commands.command(aliases=['ul'])
    @commands.is_owner()
    async def unload(self, ctx, cog):
        try:
            self.bot.unload_extension(f'cogs.{cog}')
        except Exception as e:
            await ctx.message.add_reaction('👎')
            return
        await ctx.message.add_reaction('👍')

    @commands.command(aliases=['rl'])
    @commands.is_owner()
    async def reload(self, ctx, cog):
        try:
            self.bot.reload_extension(f'cogs.{cog}')
        except Exception as e:
            await ctx.message.add_reaction('👎')
            return
        await ctx.message.add_reaction('👍')
