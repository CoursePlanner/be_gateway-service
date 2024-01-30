//package org.course_planner.gws.config;
//
//import org.course_planner.gws.dto.message.ReceiveMessageResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.core.ProducerFactory;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//public class KafkaProducerConfig {
//    @Autowired
//    private KafkaProperties kafkaProperties;
//
//    @Bean
//    public ProducerFactory<String, ReceiveMessageResponse> producerFactory() {
//        return new DefaultKafkaProducerFactory<>(producerConfigs());
//    }
//
//    @Bean
//    public Map<String, Object> producerConfigs() {
//        Map<String, Object> props = new HashMap<>(kafkaProperties.buildProducerProperties());
//
////        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
////        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
////        props.put(ProducerConfig.CLIENT_ID_CONFIG, "message-deletion-listeners");
////        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
////        props.put(ProducerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, 500);
////        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 800);
////        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 500);
////        // Increased time to 2 seconds, because it failed to run in 1 second
////        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 2000);
//
//        return props;
//    }
//
//    @Bean
//    public KafkaTemplate<String, ReceiveMessageResponse> deleteMessageKafkaTemplate() {
//        return new KafkaTemplate<>(producerFactory(), producerConfigs());
//    }
//}
