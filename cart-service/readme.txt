docker run -dp 8081:8081 cart-service
//package com.ecommerce.product_service.config;
//
//import org.redisson.Redisson;
//import org.redisson.api.RedissonClient;
//import org.redisson.config.Config;
//import org.redisson.spring.cache.RedissonSpringCacheManager;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.cache.CacheManager;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//@Configuration
//public class RedissonConfig {
//
//    private static final Logger logger = LoggerFactory.getLogger(RedissonConfig.class);
//
//    @Value("${spring.redis.url}")
//    private String redisUrl;
//
//    @Bean
//    public RedissonClient redissonClient() {
//        Config config = new Config();
//        try {
//            logger.info("Connecting to Redis at: {}", redisUrl);
//            config.useSingleServer().setAddress(redisUrl);
//            return Redisson.create(config);
//        } catch (Exception e) {
//            logger.error("Failed to configure Redisson client", e);
//            throw new RuntimeException("Failed to configure Redisson client", e);
//        }
//    }
//
//    @Bean
//    public CacheManager cacheManager(RedissonClient redissonClient) {
//        return new RedissonSpringCacheManager(redissonClient);
//    }
//}
