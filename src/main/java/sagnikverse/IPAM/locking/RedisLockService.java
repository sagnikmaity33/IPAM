package sagnikverse.IPAM.locking;




import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisLockService {

    private final StringRedisTemplate redisTemplate;

    public boolean acquireLock(String key){

        Boolean success = redisTemplate
                .opsForValue()
                .setIfAbsent(key, "locked", Duration.ofSeconds(5));

        return Boolean.TRUE.equals(success);
    }

    public void releaseLock(String key){

        redisTemplate.delete(key);
    }

}