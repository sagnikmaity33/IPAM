local key = KEYS[1]

-- find first free bit
local pos = redis.call("BITPOS", key, 0)

if pos == -1 then
    return -1
end

-- allocate it
redis.call("SETBIT", key, pos, 1)

return pos