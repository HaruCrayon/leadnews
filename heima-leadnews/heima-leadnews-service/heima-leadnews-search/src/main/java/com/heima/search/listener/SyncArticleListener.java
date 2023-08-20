package com.heima.search.listener;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.alibaba.fastjson.JSON;
import com.heima.common.constants.ArticleConstants;
import com.heima.model.search.vos.SearchArticleVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author LiJing
 * @version 1.0
 */
@Component
@Slf4j
public class SyncArticleListener {
    @Autowired
    private ElasticsearchClient esClient;

    @KafkaListener(topics = ArticleConstants.ARTICLE_ES_SYNC_TOPIC)
    public void onMessage(String message) {
        if (StringUtils.isNotBlank(message)) {
            log.info("SyncArticleListener message={}", message);
            SearchArticleVo searchArticleVo = JSON.parseObject(message, SearchArticleVo.class);

            try {
                esClient.index(i -> i
                        .index("app_info_article")
                        .id(searchArticleVo.getId().toString())
                        .document(searchArticleVo));
            } catch (IOException e) {
                e.printStackTrace();
                log.error("sync es error={}", e);
            }
        }
    }
}
