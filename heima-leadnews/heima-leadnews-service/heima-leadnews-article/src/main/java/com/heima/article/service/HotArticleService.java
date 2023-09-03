package com.heima.article.service;

import com.heima.model.article.pojos.ApArticle;

/**
 * @author LiJing
 * @version 1.0
 */
public interface HotArticleService {
    /**
     * 计算热点文章
     */
    void computeHotArticle();

    /**
     * 计算每个文章的分值
     *
     * @param apArticle
     * @return
     */
    Integer computeScore(ApArticle apArticle);
}
