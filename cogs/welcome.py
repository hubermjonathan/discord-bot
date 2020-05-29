import discord
from discord.ext import commands


class Welcome(commands.Cog):
    def __init__(self, bot, default_role_id):
        self.bot = bot
        self.default_role_id = default_role_id

    @commands.Cog.listener()
    async def on_member_join(self, member):
        # ignore the event
        if member.bot:
            return

        # change nickname and add the default role
        await member.edit(nick='???\'s Friend', roles=[member.guild.get_role(self.default_role_id)])
