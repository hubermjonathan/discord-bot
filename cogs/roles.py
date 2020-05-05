import discord
from discord.ext import commands


class Roles(commands.Cog):
    def __init__(self, bot, welcome_channel_id, default_role_id):
        self.bot = bot
        self.welcome_channel_id = welcome_channel_id
        self.default_role_id = default_role_id

    @commands.Cog.listener()
    async def on_member_join(self, member):
        # ignore the event
        if member.bot:
            return

        # change nickname and add the default role
        await member.edit(nick='???\'s Friend', roles=[member.guild.get_role(self.default_role_id)])

    @commands.Cog.listener()
    async def on_raw_reaction_add(self, payload):
        # ignore the event
        if (payload.message_id != self.bot.get_channel(self.welcome_channel_id).last_message_id or
                payload.emoji.is_unicode_emoji()):
            if payload.message_id == self.bot.get_channel(self.welcome_channel_id).last_message_id:
                welcome_channel_messages = await self.bot.get_channel(self.welcome_channel_id).history(limit=1).flatten()
                reaction_to_remove = discord.utils.find(lambda r: r.emoji == payload.emoji.name,
                                                        welcome_channel_messages[0].reactions)
                await reaction_to_remove.remove(payload.member)
            return

        # find the role
        role_to_add = discord.utils.get(payload.member.guild.roles, name=payload.emoji.name)
        if role_to_add is None:
            welcome_channel_messages = await self.bot.get_channel(self.welcome_channel_id).history(limit=1).flatten()
            reaction_to_remove = discord.utils.find(lambda r: r.emoji.name == payload.emoji.name,
                                                    welcome_channel_messages[0].reactions)
            await reaction_to_remove.remove(payload.member)
            return

        # add the role
        await payload.member.add_roles(role_to_add)

    @commands.Cog.listener()
    async def on_raw_reaction_remove(self, payload):
        # ignore the event
        if (payload.message_id != self.bot.get_channel(self.welcome_channel_id).last_message_id or
                payload.emoji.is_unicode_emoji()):
            return

        # get the member
        member = self.bot.get_guild(payload.guild_id).get_member(payload.user_id)

        # find the role
        role_to_remove = discord.utils.get(member.guild.roles, name=payload.emoji.name)
        if role_to_remove is None:
            return

        # remove the role
        await member.remove_roles(role_to_remove)

    @commands.command()
    @commands.is_owner()
    @commands.dm_only()
    async def refresh(self, ctx):
        # acknowledge the command
        await ctx.message.add_reaction('👍')

        # add roles
        welcome_channel_messages = await self.bot.get_channel(self.welcome_channel_id).history(limit=1).flatten()
        for reaction in welcome_channel_messages[0].reactions:
            async for member in reaction.users():
                if discord.utils.get(member.roles, name=reaction.emoji.name) is None:
                    await member.add_roles(discord.utils.get(member.guild.roles, name=reaction.emoji.name))
