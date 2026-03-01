package com.digiSchool.digiSchool.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Configuration Redis pour le cache et le stockage de donnees.
 *
 * Fallback: si Redis est indisponible, un cache en memoire est utilise.
 *
 * Serialisation: ObjectMapper sans type polymorphique pour eviter les erreurs de deserialisation.
 * Le cache est automatiquement nettoye au demarrage pour eviter les donnees corrompues.
 */
@Configuration
@EnableCaching
public class RedisConfig {

    private static final Logger log = LoggerFactory.getLogger(RedisConfig.class);
    
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * ObjectMapper configure pour la serialisation JSON simple sans metadata de type :
     * - FAIL_ON_UNKNOWN_PROPERTIES=false : champs inconnus ignores (compatibilite apres rebuild)
     * - JavaTimeModule : support LocalDateTime / LocalDate sans timestamp
     * - Pas de typage polymorphique pour eviter les erreurs de deserialisation
     */
    private GenericJackson2JsonRedisSerializer buildSerializer() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // Ne pas activer le typage polymorphique pour eviter les problemes de deserialisation
        return new GenericJackson2JsonRedisSerializer(mapper);
    }
    
    /**
     * Nettoie automatiquement le cache Redis au demarrage de l'application
     * pour eviter les problemes de donnees corrompues apres un rebuild
     */
    @EventListener(ApplicationReadyEvent.class)
    public void clearCacheOnStartup() {
        try {
            if (redisConnectionFactory != null) {
                RedisTemplate<String, Object> template = new RedisTemplate<>();
                template.setConnectionFactory(redisConnectionFactory);
                template.setKeySerializer(new StringRedisSerializer());
                template.afterPropertiesSet();
                
                // Nettoie uniquement les cles de cache (prefixe "cache::")
                template.execute((org.springframework.data.redis.core.RedisCallback<Object>) connection -> {
                    connection.serverCommands().flushDb();
                    return null;
                });
                
                log.info("✓ Cache Redis nettoye au demarrage pour eviter les donnees corrompues");
            }
        } catch (Exception e) {
            log.warn("Impossible de nettoyer le cache Redis au demarrage: {}", e.getMessage());
        }
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        GenericJackson2JsonRedisSerializer serializer = buildSerializer();
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        try {
            // Sauvegarde la reference pour le nettoyage au demarrage
            this.redisConnectionFactory = connectionFactory;
            
            // Test la connexion Redis
            connectionFactory.getConnection().close();

            RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1))
                .serializeValuesWith(
                    RedisSerializationContext.SerializationPair.fromSerializer(buildSerializer())
                )
                .disableCachingNullValues();

            log.info("Redis disponible - cache Redis active");

            return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .withCacheConfiguration("roles",
                    config.entryTtl(Duration.ofHours(24)))
                .withCacheConfiguration("regions",
                    config.entryTtl(Duration.ofHours(24)))
                .withCacheConfiguration("users",
                    config.entryTtl(Duration.ofMinutes(30)))
                .withCacheConfiguration("classes",
                    config.entryTtl(Duration.ofMinutes(15)))
                .withCacheConfiguration("jwt-blacklist",
                    config.entryTtl(Duration.ofHours(24)))
                .build();

        } catch (Exception e) {
            log.warn("Redis indisponible - fallback sur cache en memoire: {}", e.getMessage());
            return new ConcurrentMapCacheManager("roles", "regions", "users", "classes", "jwt-blacklist");
        }
    }
}