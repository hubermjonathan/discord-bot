from io import BytesIO
from tempfile import TemporaryFile
import asyncio
from gtts import gTTS
import discord
from discord.ext import commands


def setup(bot):
    bot.add_cog(Hello(bot))


class Hello(commands.Cog):
    def __init__(self, bot):
        self.bot = bot
        self.enabled = False
        self.speaking = False
        self.queue = []

    async def toggle(self):
        self.enabled = not self.enabled

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
