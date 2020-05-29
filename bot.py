import os
import discord
from discord.ext import commands
from cogs.globals import Globals
from cogs.welcome import Welcome
from cogs.roles import Roles
from cogs.controls import Controls

# create the bot
bot = commands.Bot(owner_id=196141424318611457,
                   command_prefix=commands.when_mentioned,
                   help_command=None)

# add all the cogs
bot.add_cog(Globals(bot))
bot.add_cog(Welcome(bot, default_role_id=701162007981981771))
bot.add_cog(Roles(bot, welcome_channel_id=701164802990407690))
bot.add_cog(Controls(bot,
                     controls_channel_id=715838633277653063,
                     chat_channel_id=464695932957622292,
                     default_role_id=701162007981981771))

# run the bot
bot.run(os.getenv('TOKEN'))
