package sagnikverse.IPAM.engine.allocation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Range;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class RedisBitmapAllocator {

    private final StringRedisTemplate redisTemplate;
    private final DefaultRedisScript<Long> allocateScript;


    /**
     * Atomic allocation using Lua
     */
    public long allocateAtomic(String subnetKey) {

        Long index = redisTemplate.execute(
                allocateScript,
                Collections.singletonList(subnetKey)
        );

        return index == null ? -1 : index;
    }


    /**
     * Find first free IP index
     */
    public long findFreeIndex(String subnetKey) {

        Long index = redisTemplate.execute((RedisCallback<Long>) connection ->
                connection.stringCommands()
                        .bitPos(subnetKey.getBytes(), false)
        );

        if (index == null) {
            return -1;
        }

        /*
         Skip network address (index 0)
        */
        if (index == 0) {

            index = redisTemplate.execute((RedisCallback<Long>) connection ->
                    connection.stringCommands()
                            .bitPos(
                                    subnetKey.getBytes(),
                                    false,
                                    Range.closed(1L, Long.MAX_VALUE)
                            )
            );
        }

        return index == null ? -1 : index;
    }

    /**
     * Mark IP allocated
     */
    public void markAllocated(String subnetKey, long index) {

        redisTemplate.opsForValue().setBit(subnetKey, index, true);

    }

    /**
     * Release IP
     */
    public void release(String subnetKey, long index) {

        redisTemplate.opsForValue().setBit(subnetKey, index, false);

    }
}