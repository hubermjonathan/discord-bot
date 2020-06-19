import discord
from discord.ext import commands


def setup(bot):
    bot.add_cog(Admin(bot))


class Admin(commands.Cog):
    def __init__(self, bot):
        self.bot = bot

    @commands.group(aliases=['a'])
    @commands.is_owner()
    async def admin(self, ctx):
        if ctx.invoked_subcommand is None:
            await ctx.message.add_reaction('❔')

    @admin.command(aliases=['e'])
    async def enable(self, ctx, cog):
        self.bot.load_extension(f'cogs.{cog}')
        await ctx.message.add_reaction('👍')

    @admin.command(aliases=['d'])
    async def disable(self, ctx, cog):
        self.bot.unload_extension(f'cogs.{cog}')
        await ctx.message.add_reaction('👍')

    @admin.command(aliases=['rl'])
    async def reload(self, ctx, cog):
        self.bot.reload_extension(f'cogs.{cog}')
        await ctx.message.add_reaction('👍')

    @admin.command(aliases=['r'])
    async def region(self, ctx, region):
        if region == 'west':
            await ctx.guild.edit(region=discord.VoiceRegion.us_west)
        elif region == 'central':
            await ctx.guild.edit(region=discord.VoiceRegion.us_central)
        elif region == 'south':
            await ctx.guild.edit(region=discord.VoiceRegion.us_south)
        elif region == 'east':
            await ctx.guild.edit(region=discord.VoiceRegion.us_east)
        else:
            await ctx.guild.edit(region=discord.VoiceRegion.us_central)

        await ctx.message.add_reaction('👍')

    @admin.command(aliases=['p'])
    async def priority(self, ctx):
        voice_channel = None
        for vc in ctx.guild.voice_channels:
            if ctx.guild.owner in vc.members:
                voice_channel = vc
                break
        if voice_channel is None:
            await ctx.message.add_reaction('🔇')
            return
        members = voice_channel.members

        for m in members:
            if m == ctx.guild.owner:
                continue
            await m.edit(mute=not m.voice.mute)

        await ctx.message.add_reaction('👍')
