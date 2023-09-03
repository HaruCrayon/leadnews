package com.heima.kafka.stream;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Arrays;

/**
 * @author LiJing
 * @version 1.0
 */
@Configuration
@Slf4j
public class KafkaStreamHelloListener {

    @Bean
    public KStream<String, String> kStream(StreamsBuilder streamsBuilder) {
        //创建KStream对象，同时指定从哪个topic中接收消息
        KStream<String, String> stream = streamsBuilder.stream("itcast-topic-input");

        //处理消息
        stream.flatMapValues(new ValueMapper<String, Iterable<String>>() {
                    @Override
                    public Iterable<String> apply(String value) {
                        return Arrays.asList(value.split(" "));
                    }
                })
                //根据value进行分组
                .groupBy((key, value) -> value)
                //时间窗口：聚合计算时间间隔
                .windowedBy(TimeWindows.of(Duration.ofSeconds(10)))
                //聚合查询：统计单词的个数
                .count()
                //转换为KStream
                .toStream()
                //处理后的结果转换为string字符串
                .map((key, value) -> {
                    System.out.println("key:" + key + ", vlaue:" + value);
                    return new KeyValue<>(key.key().toString(), value.toString());
                })
                //发送消息
                .to("itcast-topic-out");

        return stream;
    }
}
