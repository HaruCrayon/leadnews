package com.heima.kafka.sample;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @author LiJing
 * @version 1.0
 * 生产者
 */
public class ProducerQuickStart {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //1.kafka的配置信息
        Properties properties = new Properties();
        //kafka的连接地址
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.72.100:9092");
        //消息key的序列化器
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        //消息value的序列化器
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        //ack配置  消息确认机制
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        //重试次数
        properties.put(ProducerConfig.RETRIES_CONFIG, 10);
        //消息数据压缩
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "lz4");

        //2.生产者对象
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        //封装发送的消息
//        ProducerRecord<String, String> record = new ProducerRecord<>("topic-first", "key-001", "hello kafka");

        //测试流式计算
        for (int i = 0; i < 5; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>("itcast-topic-input", "hello kafka");
            producer.send(record);
        }

        //3.发送消息
        //同步发送
//        RecordMetadata recordMetadata = producer.send(record).get();
//        System.out.println("offset=" + recordMetadata.offset());

        //异步发送
//        producer.send(record, new Callback() {
//            @Override
//            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
//                if (e != null) {
//                    System.out.println("记录异常信息到日志表中");
//                }
//                System.out.println("offset=" + recordMetadata.offset());
//            }
//        });

        //4.关闭消息通道，必须关闭，否则消息发送不成功
        producer.close();
    }
}
