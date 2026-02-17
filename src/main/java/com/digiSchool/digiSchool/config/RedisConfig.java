package com.digiSchool.digiSchool.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Configuration Redis pour le cache et le stockage de donnees.
 *
 * Utilisation:
 * - Cache des donnees frequemment accedees (roles, regions, etc.)
 * - Stockage des sessions (optionnel)
 * - Stockage des tokens invalides (blacklist JWT)
 */
@Configuration
@EnableCaching
public class RedisConfig {

    /**
     * Template Redis pour les operations de lecture/ecriture
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Serialisation des cles en String
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // Serialisation des valeurs en JSON
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }

    /**
     * Configuration du cache manager Redis
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
            // TTL par defaut: 1 heure
            .entryTtl(Duration.ofHours(1))
            // Serialisation JSON pour les valeurs
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new GenericJackson2JsonRedisSerializer()
                )
            )
            // Ne pas cacher les valeurs null
            .disableCachingNullValues();

        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(config)
            // Caches specifiques avec TTL personnalise
            .withCacheConfiguration("roles",
                config.entryTtl(Duration.ofHours(24)))  // Roles: 24h
            .withCacheConfiguration("regions",
                config.entryTtl(Duration.ofHours(24)))  // Regions: 24h
            .withCacheConfiguration("users",
                config.entryTtl(Duration.ofMinutes(30))) // Users: 30min
            .withCacheConfiguration("classes",
                config.entryTtl(Duration.ofMinutes(15))) // Classes: 15min
            .withCacheConfiguration("jwt-blacklist",
                config.entryTtl(Duration.ofHours(24)))  // JWT blacklist: 24h
            .build();
    }
}
