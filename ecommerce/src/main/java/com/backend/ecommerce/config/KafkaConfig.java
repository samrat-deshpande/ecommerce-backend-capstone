package com.backend.ecommerce.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for Kafka setup
 */
@Configuration
public class KafkaConfig {
    
    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;
    
    @Value("${kafka.topic.user-events:user-events}")
    private String userEventsTopic;
    
    @Value("${kafka.topic.user-registrations:user-registrations}")
    private String userRegistrationsTopic;
    
    @Value("${kafka.topic.user-logins:user-logins}")
    private String userLoginsTopic;
    
    @Value("${kafka.topic.order-events:order-events}")
    private String orderEventsTopic;
    
    @Value("${kafka.topic.payment-events:payment-events}")
    private String paymentEventsTopic;
    
    @Value("${kafka.topic.payment-verification:payment-verification}")
    private String paymentVerificationTopic;
    
    /**
     * Configure Kafka producer factory
     */
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(ProducerConfig.RETRIES_CONFIG, 3);
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        configProps.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        
        return new DefaultKafkaProducerFactory<>(configProps);
    }
    
    /**
     * Configure Kafka template
     */
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
    
    /**
     * Create user events topic
     */
    @Bean
    public NewTopic userEventsTopic() {
        return TopicBuilder.name(userEventsTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
    
    /**
     * Create user registrations topic
     */
    @Bean
    public NewTopic userRegistrationsTopic() {
        return TopicBuilder.name(userRegistrationsTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
    
    /**
     * Create user logins topic
     */
    @Bean
    public NewTopic userLoginsTopic() {
        return TopicBuilder.name(userLoginsTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
    
    /**
     * Create order events topic
     */
    @Bean
    public NewTopic orderEventsTopic() {
        return TopicBuilder.name(orderEventsTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
    
    /**
     * Create payment events topic
     */
    @Bean
    public NewTopic paymentEventsTopic() {
        return TopicBuilder.name(paymentEventsTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
    
    /**
     * Create payment verification topic
     */
    @Bean
    public NewTopic paymentVerificationTopic() {
        return TopicBuilder.name(paymentVerificationTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
    
    /**
     * Create test topic for development
     */
    @Bean
    public NewTopic testTopic() {
        return TopicBuilder.name("test-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
