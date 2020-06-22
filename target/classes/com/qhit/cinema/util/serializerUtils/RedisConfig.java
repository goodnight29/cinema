package com.qhit.fsaas.util.serializerUtils;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qhit.fsaas.util.serializerUtils.FastJson2JsonRedisSerializer;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

/*        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);*/

        //使用FastJson2JsonRedisSerializer来序列化和反序列化redis的value值
//        FastJson2JsonRedisSerializer serializer = new FastJson2JsonRedisSerializer<>(Object.class);
        FastJson2JsonRedisSerializer serializer = new FastJson2JsonRedisSerializer<>(Object.class);

        ObjectMapper mapper = new ObjectMapper();
        //指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        serializer.setObjectMapper(mapper);

        template.setDefaultSerializer(serializer);

        template.setKeySerializer(new StringRedisSerializer());
//        template.setKeySerializer(serializer);
        template.setValueSerializer(serializer);
//        template.setHashKeySerializer(serializer);
//        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }
}


