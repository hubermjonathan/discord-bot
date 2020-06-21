import os
import redis


CONFIRM = '👍'
DENY = '👎'
NO_COMMAND = '❔'
NOT_IN_VC = '🔇'
NOT_ENOUGH_POINTS = '💵'
ON_COOLDOWN = '⏲'
SHIELD = '🛡'

GUILD_ID = int(os.getenv('GUILD_ID'))
CHAT_CHANNEL_ID = int(os.getenv('CHAT_CHANNEL_ID'))
DEFAULT_ROLE_ID = int(os.getenv('DEFAULT_ROLE_ID'))
PROMOTED_ROLE_ID = int(os.getenv('PROMOTED_ROLE_ID'))
REDIS = redis.from_url(os.environ.get('REDIS_URL'))
