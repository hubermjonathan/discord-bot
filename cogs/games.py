import discord
from discord.ext import commands


def setup(bot):
    # bot.add_cog(Games(bot))
    pass


class Games(commands.Cog):
    def __init__(self, bot):
        self.bot = bot

    async def is_new_reaction(self):
        guild = self.bot.get_guild(self.payload.guild_id)
        channel = self.bot.get_channel(self.controls_channel_id)
        message = await channel.fetch_message(self.payload.message_id)

        for r in message.reactions:
            if isinstance(r.emoji, str) and r.emoji == self.payload.emoji.name:
                reaction = r
            elif not isinstance(r.emoji, str) and r.emoji.name == self.payload.emoji.name:
                reaction = r

        members = await reaction.users().flatten()
        if guild.me not in members:
            return True
        else:
            return False

    async def is_game_emoji(self):
        role = discord.utils.get(self.payload.member.guild.roles, name=self.payload.emoji.name)
        if role is None:
            return False
        return True

    async def send(self):
        message = await ctx.send('add or remove games from your roles')
        for r in reversed(ctx.guild.roles[1:ctx.guild.roles.index(ctx.guild.get_role(globals.DEFAULT_ROLE_ID))]):
            await message.add_reaction(discord.utils.get(ctx.guild.emojis, name=r.name))

    async def update(self, payload):
        game = discord.utils.get(payload.member.guild.roles, name=payload.emoji.name)
        if game in payload.member.roles:
            await payload.member.remove_roles(game)
        else:
            await payload.member.add_roles(game)
