import datetime
from discord.ext import commands
import constants


def setup(bot):
    bot.add_cog(Welcome(bot))


class Welcome(commands.Cog):
    def __init__(self, bot):
        self.bot = bot

    @commands.Cog.listener()
    async def on_message(self, message):
        member = message.author
        if member.bot:
            return

        invites = await member.guild.invites()
        most_recent = datetime.datetime(2020, 1, 1)
        for i in invites:
            if i.created_at > most_recent:
                most_recent = i.created_at
                id = i.inviter.id

        if len(invites) == 0:
            name = '???\'s Friend'
        elif id == 200067699534069761:
            name = 'Aeson\'s Friend'
        elif id == 191025031134838785:
            name = 'Aneesh\'s Friend'
        elif id == 235584239658205184:
            name = 'Sammie\'s Friend'
        elif id == 98909400936247296:
            name = 'Drew\'s Friend'
        elif id == 349934398067441665:
            name = 'Eric\'s Friend'
        elif id == 404462718209228810:
            name = 'Charlene\'s Friend'
        elif id == 135980703396397056:
            name = 'Collin\'s Friend'
        elif id == 318936907499438080:
            name = 'Davis\' Friend'
        elif id == 703454392602329149:
            name = 'Jon\'s Friend'
        elif id == 94844180680937472:
            name = 'Ray\'s Friend'
        elif id == 329152118923460609:
            name = 'Jack\'s Friend'
        elif id == 404459939269181453:
            name = 'Jack\'s Friend'
        elif id == 196141424318611457:
            name = 'Jon\'s Friend'
        elif id == 157673788585017344:
            name = 'Trevor\'s Friend'
        elif id == 330221855879200768:
            name = 'Julien\'s Friend'
        elif id == 84113068073680896:
            name = 'Jay\'s Friend'
        elif id == 374385728265912331:
            name = 'Neel\'s Friend'
        elif id == 269689252579770368:
            name = 'Andy\'s Friend'
        elif id == 147848898772205569:
            name = 'Emanuel\'s Friend'
        elif id == 204431167959728129:
            name = 'Daniel\'s Friend'
        elif id == 545837915251408898:
            name = 'Ray\'s Friend'
        else:
            name = '???\'s Friend'
        
        if len(invites) > 1: name += '?'

        await member.edit(nick=name, roles=[member.guild.get_role(constants.DEFAULT_ROLE_ID)])
