import os
from datetime import datetime, timedelta
import discord
from discord.ext import commands


def setup(bot):
    bot.add_cog(Poll(bot))


class Poll(commands.Cog):
    def __init__(self, bot):
        self.bot = bot
        self.last_poll = datetime.now()
        self.chat_channel_id = int(os.getenv('CHAT_CHANNEL_ID'))
        self.default_role_id = int(os.getenv('DEFAULT_ROLE_ID'))
        self.promoted_role_id = int(os.getenv('PROMOTED_ROLE_ID'))

    async def start_common(self, payload):
        # check the cooldown
        if datetime.now() - self.last_poll < timedelta(minutes=1):
            return
        self.last_poll = datetime.now()

        # get the guild
        guild = payload.member.guild

        # get the members
        voice_channel = None
        for vc in guild.voice_channels:
            if payload.member in vc.members:
                voice_channel = vc
                break
        if voice_channel is None:
            raise commands.CommandError
        members = voice_channel.members

        # get the roles
        roles = guild.roles[1:guild.roles.index(guild.get_role(self.default_role_id))]
        for m in members:
            if guild.get_role(self.promoted_role_id) in m.roles:
                member_roles = m.roles[1:m.roles.index(guild.get_role(self.promoted_role_id))]
            else:
                member_roles = m.roles[1:m.roles.index(guild.get_role(self.default_role_id))]
            if len(member_roles) < 2:
                continue
            roles = list(set(roles) & set(member_roles))
        if len(roles) < 2:
            raise commands.CommandError('not enough games in common')

        # send the message
        message = await self.bot.get_channel(self.chat_channel_id).send('what game do you want to play?')

        # add the reactions
        for r in roles:
            await message.add_reaction(discord.utils.get(guild.emojis, name=r.name))

    async def start(self, payload):
        # check the cooldown
        if datetime.now() - self.last_poll < timedelta(minutes=1):
            return
        self.last_poll = datetime.now()

        # get the guild
        guild = payload.member.guild

        # send the message
        message = await self.bot.get_channel(self.chat_channel_id).send('what game do you want to play?')

        # add the reactions
        for r in reversed(guild.roles[1:guild.roles.index(guild.get_role(self.default_role_id))]):
            await message.add_reaction(discord.utils.get(guild.emojis, name=r.name))
