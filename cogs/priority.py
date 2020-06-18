from discord.ext import commands


def setup(bot):
    bot.add_cog(Priority(bot))


class Priority(commands.Cog):
    def __init__(self, bot):
        self.bot = bot

    async def toggle(self, payload):
        voice_channel = None
        for vc in payload.member.guild.voice_channels:
            if payload.member.guild.owner in vc.members:
                voice_channel = vc
                break
        if voice_channel is None:
            return
        members = voice_channel.members

        for m in members:
            if m == payload.member.guild.owner:
                continue
            await m.edit(mute=not m.voice.mute)
