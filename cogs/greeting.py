from io import BytesIO
from tempfile import TemporaryFile
import asyncio
from gtts import gTTS
import discord
from discord.ext import commands
import constants


def setup(bot):
    bot.add_cog(Greeting(bot))


class Greeting(commands.Cog):
    def __init__(self, bot):
        self.bot = bot
        self.enabled = False
        self.speaking = False
        self.queue = []

    @commands.Cog.listener()
    async def on_voice_state_update(self, member, before, after):
        if member.bot or before.channel is not None or not self.enabled:
            return

        self.queue.append(member.display_name)
        if self.speaking:
            return

        self.speaking = True
        voice_channel = await after.channel.connect()
        while len(self.queue) != 0:
            member_name = self.queue.pop()
            tts_fp = BytesIO()
            tts = gTTS(f'hello {member_name}')
            tts.write_to_fp(tts_fp)
            tts_fp.seek(0)

            with TemporaryFile() as file:
                file.write(tts_fp.read())
                file.seek(0)
                source = discord.FFmpegPCMAudio(file, pipe=True)
                voice_channel.play(source)

                while voice_channel.is_playing():
                    await asyncio.sleep(0.01)
                else:
                    tts_fp.close()

        await voice_channel.disconnect()
        self.speaking = False

    @commands.group(aliases=['g'])
    async def greeting(self, ctx):
        if ctx.invoked_subcommand is None:
            await ctx.message.add_reaction(constants.NO_COMMAND)

    @greeting.command(aliases=['t'])
    async def toggle(self, ctx):
        self.enabled = not self.enabled
        await ctx.message.add_reaction(constants.CONFIRM)
