import os
import redis

guild_id=int(os.getenv('GUILD_ID'))
chat_channel_id=int(os.getenv('CHAT_CHANNEL_ID'))
default_role_id=int(os.getenv('DEFAULT_ROLE_ID'))
promoted_role_id=int(os.getenv('PROMOTED_ROLE_ID'))
redis = redis.from_url(os.environ.get('REDIS_URL'))
