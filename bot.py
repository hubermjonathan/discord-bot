import os
import discord
from discord.ext import commands
from cogs.globals import Globals
from cogs.welcome import Welcome
from cogs.poll import Poll

# create the bot
bot = commands.Bot(owner_id=196141424318611457,
                   command_prefix=commands.when_mentioned,
                   help_command=None)

# add all the cogs
bot.add_cog(Globals(bot))
bot.add_cog(Welcome(bot, welcome_channel_id=701164802990407690, default_role_id=701162007981981771))
bot.add_cog(Poll(bot, default_role_id=701162007981981771, promoted_role_id=701161990919422002))

# run the bot
bot.run(os.getenv('TOKEN'))
