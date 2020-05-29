import discord
from discord.ext import commands


class Roles(commands.Cog):
    def __init__(self, bot, welcome_channel_id, default_role_id):
        self.bot = bot
        self.welcome_channel_id = welcome_channel_id
        self.default_role_id = default_role_id

    async def remove_reaction(self, payload, is_custom):
        welcome_channel_messages = await self.bot.get_channel(self.welcome_channel_id).history(
            limit=1).flatten()
        if is_custom:
            reaction_to_remove = discord.utils.find(lambda r: r.emoji.name == payload.emoji.name,
                                                    welcome_channel_messages[0].reactions)
        else:
            reaction_to_remove = discord.utils.find(lambda r: r.emoji == payload.emoji.name,
                                                    welcome_channel_messages[0].reactions)
        await reaction_to_remove.remove(payload.member)

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
                payload.emoji.is_unicode_emoji() or
                payload.member.bot):
            if (not payload.member.bot and
                    payload.message_id == self.bot.get_channel(self.welcome_channel_id).last_message_id):
                await self.remove_reaction(payload, False)
            return

        # find the role
        role = discord.utils.get(payload.member.guild.roles, name=payload.emoji.name)
        if role is None:
            await self.remove_reaction(payload, True)
            return

        # add or remove the role
        if role in payload.member.roles:
            await payload.member.remove_roles(role)
        else:
            await payload.member.add_roles(role)

        # remove the reaction
        await self.remove_reaction(payload, True)

    @commands.command()
    @commands.is_owner()
    async def create(self, ctx):
        # ignore the command
        if ctx.message.id != self.bot.get_channel(self.welcome_channel_id).last_message_id:
            raise commands.CommandError

        # acknowledge the command
        await ctx.message.delete()

        # send the message
        message = await ctx.send('click the icons below to add or remove the games you play from your roles')

        # add the reactions
        for r in reversed(ctx.guild.roles[1:ctx.guild.roles.index(ctx.guild.get_role(self.default_role_id))]):
            await message.add_reaction(discord.utils.get(ctx.guild.emojis, name=r.name))
