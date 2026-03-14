package sagnikverse.IPAM.locking;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DistributedLockService {

    private final RedissonClient redissonClient;

    public RLock acquireLock(String key) {

        RLock lock = redissonClient.getLock(key);

        // BLOCKING LOCK
        lock.lock();

        return lock;
    }

    public void releaseLock(RLock lock){

        if(lock != null && lock.isHeldByCurrentThread()){
            lock.unlock();
        }

    }

}