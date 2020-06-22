package com.qhit.fsaas.src;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/temp")
public class MyTempCode {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "/hello/{name}", method = RequestMethod.GET)
    public String sayHello(@RequestParam Integer id, @PathVariable(name = "name") String name) {
        return "id:" + id + ";name" + name;
    }
    public void getRedis(String key) {
        String s = stringRedisTemplate.opsForValue().get(key);
    }
    public void setRedis(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }
}
