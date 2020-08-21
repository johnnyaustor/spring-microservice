package com.jap.microservice.emailservice.config;

import com.jap.microservice.emailservice.service.RedisMessageSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * @author jap
 * @since 2020.08
 */

@Configuration
public class RedisListenerConfig {
    @Value("${spring.redis.host:localhost}")
    private String redisHost;
    @Value("${spring.redis.port:6397}")
    private Integer redisPort;
    @Value("${spring.redis.channel-name:emailSender}")
    private String redisChannelName;

    @Autowired
    private RedisMessageSubscriber redisMessageSubscriber;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new LettuceConnectionFactory(redisConfig);
    }

    @Bean
    public ChannelTopic channelTopic() {
        return new ChannelTopic(redisChannelName);
    }

    @Bean
    public MessageListener messageListenerAdapter() {
        return new MessageListenerAdapter(redisMessageSubscriber);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer redisContainer = new RedisMessageListenerContainer();
        redisContainer.setConnectionFactory(redisConnectionFactory());
        redisContainer.addMessageListener(messageListenerAdapter(), channelTopic());
        return redisContainer;
    }
}
