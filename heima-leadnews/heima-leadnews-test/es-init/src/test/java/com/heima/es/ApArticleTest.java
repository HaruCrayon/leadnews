package com.heima.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import com.heima.es.mapper.ApArticleMapper;
import com.heima.es.pojo.SearchArticleVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class ApArticleTest {
    @Autowired
    private ApArticleMapper apArticleMapper;

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    /**
     * 注意：数据的导入，如果数据量过大，需要分页导入
     *
     * @throws Exception
     */
    @Test
    public void init() throws Exception {
        //1.查询所有符合条件的文章数据
        List<SearchArticleVo> searchArticleVos = apArticleMapper.loadArticleList();

        //2.批量导入到es索引库
        BulkRequest.Builder br = new BulkRequest.Builder();
        for (SearchArticleVo searchArticleVo : searchArticleVos) {
            br.operations(op -> op
                    .index(idx -> idx
                            .index("app_info_article")
                            .id(searchArticleVo.getId().toString())
                            .document(searchArticleVo)
                    )
            );
        }
        BulkResponse bulkResponse = elasticsearchClient.bulk(br.build());

        // Log errors
        if (bulkResponse.errors()) {
            log.error("Bulk had errors");
            for (BulkResponseItem item : bulkResponse.items()) {
                if (item.error() != null) {
                    log.error(item.error().reason());
                }
            }
        }

    }

}