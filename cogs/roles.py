import discord
from discord.ext import commands


class Roles(commands.Cog):
    def __init__(self, bot, welcome_channel_id):
        self.bot = bot
        self.welcome_channel_id = welcome_channel_id

    async def remove_reaction(self, payload):
        message = await self.bot.get_channel(self.welcome_channel_id).fetch_message(payload.message_id)
        for r in message.reactions:
            if isinstance(r.emoji, str) and r.emoji == payload.emoji.name:
                reaction_to_remove = r
            elif not isinstance(r.emoji, str) and r.emoji.name == payload.emoji.name:
                reaction_to_remove = r
        await reaction_to_remove.remove(payload.member)

    @commands.Cog.listener()
    async def on_raw_reaction_add(self, payload):
        # ignore the event
        if (payload.message_id != self.bot.get_channel(self.welcome_channel_id).last_message_id or
                payload.emoji.is_unicode_emoji() or
                payload.member.bot):
            if (not payload.member.bot and
                    payload.message_id == self.bot.get_channel(self.welcome_channel_id).last_message_id):
                await self.remove_reaction(payload)
            return

        # find the role
        role = discord.utils.get(payload.member.guild.roles, name=payload.emoji.name)
        if role is None:
            await self.remove_reaction(payload)
            return

        # add or remove the role
        if role in payload.member.roles:
            await payload.member.remove_roles(role)
        else:
            await payload.member.add_roles(role)

        # remove the reaction
        await self.remove_reaction(payload)
