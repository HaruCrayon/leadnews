package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.NewsAuthDto;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.model.wemedia.pojos.WmNews;

public interface WmNewsService extends IService<WmNews> {

    /**
     * 查询文章
     *
     * @param dto
     * @return
     */
    ResponseResult findAll(WmNewsPageReqDto dto);

    /**
     * 发布修改文章或保存为草稿
     *
     * @param dto
     * @return
     */
    ResponseResult submitNews(WmNewsDto dto);

    /**
     * 文章的上下架
     *
     * @param dto
     * @return
     */
    ResponseResult downOrUp(WmNewsDto dto);

    /**
     * 查询文章列表
     *
     * @param dto
     * @return
     */
    ResponseResult findList(NewsAuthDto dto);

    /**
     * 查询文章详情
     *
     * @param id
     * @return
     */
    ResponseResult findWmNewsVo(Integer id);

    /**
     * 文章审核，修改状态
     *
     * @param status 2 审核失败  4 人工审核通过
     * @param dto
     * @return
     */
    ResponseResult updateStatus(Short status, NewsAuthDto dto);
}