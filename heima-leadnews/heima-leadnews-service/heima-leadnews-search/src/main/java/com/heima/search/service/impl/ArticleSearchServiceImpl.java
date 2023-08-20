package com.heima.search.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.alibaba.fastjson.JSON;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.search.dtos.UserSearchDto;
import com.heima.model.search.vos.SearchArticleVo;
import com.heima.model.user.pojos.ApUser;
import com.heima.search.service.ApUserSearchService;
import com.heima.search.service.ArticleSearchService;
import com.heima.utils.thread.ApThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author LiJing
 * @version 1.0
 */
@Service
@Slf4j
public class ArticleSearchServiceImpl implements ArticleSearchService {
    @Autowired
    private ElasticsearchClient esClient;

    @Autowired
    private ApUserSearchService apUserSearchService;

    /**
     * ES文章分页搜索
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult search(UserSearchDto dto) throws IOException {
        //1.检查参数
        if (dto == null || StringUtils.isEmpty(dto.getSearchWords())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        ApUser user = ApThreadLocalUtil.getUser();
        //异步调用 保存搜索记录
        if (user != null && dto.getFromIndex() == 0) {
            apUserSearchService.insert(dto.getSearchWords(), user.getId());
        }

        //2.设置查询条件
        SearchRequest.Builder searchRequestBuilder = new SearchRequest.Builder();

        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

        //关键字搜索 Search by "content"
        Query byTitleAndContent = MatchQuery.of(m -> m
                .field("content")
                .query(dto.getSearchWords())
        )._toQuery();
        boolQueryBuilder.must(byTitleAndContent);

        //查询小于MinBehotTime的数据 Search by "publishTime"
        Query byPublishTime = RangeQuery.of(r -> r
                .field("publishTime")
                .lt(JsonData.of(dto.getMinBehotTime().getTime()))
        )._toQuery();
        boolQueryBuilder.filter(byPublishTime);

        //分页查询

        //按照发布时间倒序查询

        //设置高亮  title

        searchRequestBuilder.index("app_info_article")
                .query(q -> q.bool(boolQueryBuilder.build()))
                .from(dto.getFromIndex())
                .size(dto.getPageSize())
                .sort(s -> s.field(new FieldSort.Builder().field("publishTime").order(SortOrder.Desc).build()))
                .highlight(h -> h.fields("title", f -> f
                        .requireFieldMatch(false)
                        .preTags("<font style='color: red; font-size: inherit;'>")
                        .postTags("</font>")));

        SearchResponse<SearchArticleVo> searchResponse = esClient.search(searchRequestBuilder.build(), SearchArticleVo.class);

        //3.结果封装返回
        ArrayList<Map> list = new ArrayList<>();

        List<Hit<SearchArticleVo>> hits = searchResponse.hits().hits();
        for (Hit<SearchArticleVo> hit : hits) {
            SearchArticleVo searchArticleVo = hit.source();
            Map map = JSON.parseObject(JSON.toJSONString(searchArticleVo), Map.class);

            // 获取高亮结果
            Map<String, List<String>> highlight = hit.highlight();
            if (!CollectionUtils.isEmpty(highlight)) {
                List<String> title = highlight.get("title");
                if (title != null) {
                    String result = title.get(0);
                    //高亮标题
                    map.put("h_title", result);
                }
            } else {
                //原始标题
                map.put("h_title", map.get("title"));
            }
            list.add(map);
        }

        return ResponseResult.okResult(list);
    }

}
