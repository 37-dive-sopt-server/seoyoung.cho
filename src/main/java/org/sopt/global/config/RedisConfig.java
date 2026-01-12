package org.sopt.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.sopt.global.constants.CacheConstants.*;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // 타입 정보 저장 설정
        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType(Object.class)
                .build();

        // DTO가 Record 이기 때문에 EVERYTHING 사용
        mapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.EVERYTHING);

        return mapper;
    }

    @Bean
    public CacheManager cacheManager(
            RedisConnectionFactory connectionFactory,
            ObjectMapper objectMapper) {

        RedisCacheConfiguration defaultConfig = createDefaultCacheConfig(objectMapper);
        Map<String, RedisCacheConfiguration> cacheConfigurations = createCacheConfigurations(defaultConfig);

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }

    /* 기본 캐시 설정 생성 */
    private RedisCacheConfiguration createDefaultCacheConfig(ObjectMapper objectMapper) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(createStringSerializationPair())
                .serializeValuesWith(createJsonSerializationPair(objectMapper))
                .entryTtl(Duration.ofMinutes(DEFAULT_TTL_MINUTES));
    }

    /* 개별 설정 생성 */
    private Map<String, RedisCacheConfiguration> createCacheConfigurations(
            RedisCacheConfiguration defaultConfig) {

        Map<String, RedisCacheConfiguration> configurations = new HashMap<>();

        // 게시글 목록: 5분
        configurations.put(
                ARTICLES_LIST,
                defaultConfig.entryTtl(Duration.ofMinutes(ARTICLES_LIST_TTL_MINUTES))
        );

        // 게시글 상세: 10분
        configurations.put(
                ARTICLE_DETAIL,
                defaultConfig.entryTtl(Duration.ofMinutes(ARTICLE_DETAIL_TTL_MINUTES))
        );

        return configurations;
    }

    /* String 직렬화 설정 */
    private RedisSerializationContext.SerializationPair<String> createStringSerializationPair() {
        return RedisSerializationContext.SerializationPair.fromSerializer(
                new StringRedisSerializer()
        );
    }

    /* JSON 직렬화 설정 */
    private RedisSerializationContext.SerializationPair<Object> createJsonSerializationPair(
            ObjectMapper objectMapper) {
        return RedisSerializationContext.SerializationPair.fromSerializer(
                new GenericJackson2JsonRedisSerializer(objectMapper)
        );
    }
}
