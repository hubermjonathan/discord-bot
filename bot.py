import os
import discord
from discord.ext import commands
from cogs.globals import Globals
from cogs.welcome import Welcome
from cogs.controls import Controls

# create the bot
bot = commands.Bot(owner_id=196141424318611457,
                   command_prefix=commands.when_mentioned,
                   help_command=None)

# add all the cogs
bot.add_cog(Globals(bot))
bot.add_cog(Welcome(bot, default_role_id=int(os.getenv('DEFAULT_ROLE_ID'))))
bot.add_cog(Controls(bot,
                     controls_channel_id=int(os.getenv('CONTROLS_CHANNEL_ID')),
                     chat_channel_id=int(os.getenv('CHAT_CHANNEL_ID')),
                     default_role_id=int(os.getenv('DEFAULT_ROLE_ID'))))

# run the bot
bot.run(os.getenv('TOKEN'))
