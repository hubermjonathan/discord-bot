import os
import redis

GUILD_ID=int(os.getenv('GUILD_ID'))
CHAT_CHANNEL_ID=int(os.getenv('CHAT_CHANNEL_ID'))
DEFAULT_ROLE_ID=int(os.getenv('DEFAULT_ROLE_ID'))
PROMOTED_ROLE_ID=int(os.getenv('PROMOTED_ROLE_ID'))
REDIS = redis.from_url(os.environ.get('REDIS_URL'))
