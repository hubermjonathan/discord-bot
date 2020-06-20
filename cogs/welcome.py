from discord.ext import commands
import globals


def setup(bot):
    bot.add_cog(Welcome(bot))


class Welcome(commands.Cog):
    def __init__(self, bot):
        self.bot = bot

    @commands.Cog.listener()
    async def on_member_join(self, member):
        if member.bot:
            return

        await member.edit(nick='???\'s Friend', roles=[member.guild.get_role(globals.DEFAULT_ROLE_ID)])
