package com.beyond.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.password}")
    private String password;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // standalone 구성에 호스트, 포트, 비밀번호를 설정
        RedisStandaloneConfiguration redisConf = new RedisStandaloneConfiguration(host, port);
        redisConf.setPassword(password);

        // LettuceConnectionFactory 에 구성 정보 전달
        return new LettuceConnectionFactory(redisConf); // -> 비동기 및 논블로킹 방식으로 효율적
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // Key, Value 직렬화 설정
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer()); //복잡한 문자열

        return redisTemplate;
    }

    // Redis 메시지 리스너 어댑터 설정: 실제 메시지 처리 로직(RedisMessageSubscriber)을 주입
/*    @Bean
    public MessageListenerAdapter messageListener(RedisMessageSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber);
    }*/

    // Redis 메시지 리스너 컨테이너 설정: 특정 채널("notification")을 구독하도록 구성
/*    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory,
                                                        MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // "notification" 채널에 발행되는 모든 메시지를 listenerAdapter 가 수신
        container.addMessageListener(listenerAdapter, new ChannelTopic("notification"));
        return container;
    }*/
}