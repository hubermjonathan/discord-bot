from io import BytesIO
from tempfile import TemporaryFile
import asyncio
from gtts import gTTS
import discord
from discord.ext import commands


def setup(bot):
    return
    bot.add_cog(Hello(bot))


class Hello(commands.Cog):
    def __init__(self, bot):
        self.bot = bot
        self.enabled = True
        self.speaking = False
        self.queue = []

    async def toggle(self):
        # toggle the greeting
        self.enabled = not self.enabled

        # update status
        await self.bot.change_presence(status=discord.Status.online if self.enabled else discord.Status.dnd)

    @commands.Cog.listener()
    async def on_voice_state_update(self, member, before, after):
        # ignore the event
        if member.bot or before.channel is not None or not self.enabled:
            return

        # add to the queue
        self.queue.append(member.display_name)
        if self.speaking:
            return

        # play through the queue
        self.speaking = True
        voice_channel = await after.channel.connect()
        while len(self.queue) != 0:
            # create the greeting
            member_name = self.queue.pop()
            tts_fp = BytesIO()
            tts = gTTS(f'hello {member_name}')
            tts.write_to_fp(tts_fp)
            tts_fp.seek(0)

            # greet the user
            with TemporaryFile() as file:
                file.write(tts_fp.read())
                file.seek(0)
                source = discord.FFmpegPCMAudio(file, pipe=True)
                voice_channel.play(source)

                # play the message and delete it
                while voice_channel.is_playing():
                    await asyncio.sleep(0.01)
                else:
                    tts_fp.close()

        # leave the voice channel
        await voice_channel.disconnect()
        self.speaking = False
