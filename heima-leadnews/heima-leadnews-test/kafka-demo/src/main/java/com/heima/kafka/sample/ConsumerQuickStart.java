package com.heima.kafka.sample;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * @author LiJing
 * @version 1.0
 * 消费者
 */
public class ConsumerQuickStart {
    public static void main(String[] args) {
        //1.添加kafka的配置信息
        Properties properties = new Properties();
        //kafka的连接地址
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.72.100:9092");
        //消费者组
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group1");
        //消息的反序列化器
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        //手动提交偏移量
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        //2.消费者对象
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        //3.订阅主题
        consumer.subscribe(Collections.singletonList("itcast-topic-out"));

        //当前线程一直处于监听状态
        while (true) {
            //4.获取消息
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                System.out.println("key=" + consumerRecord.key());
                System.out.println("value=" + consumerRecord.value());
                System.out.println("partition=" + consumerRecord.partition());
                System.out.println("offset=" + consumerRecord.offset());
            }
        }
    }
}
