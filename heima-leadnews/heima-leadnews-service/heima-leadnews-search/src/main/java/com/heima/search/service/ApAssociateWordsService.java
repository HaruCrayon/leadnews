package com.heima.search.service;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.dtos.UserSearchDto;

/**
 * @author LiJing
 * @version 1.0
 */
public interface ApAssociateWordsService {

    /**
     * 联想词
     *
     * @param dto
     * @return
     */
    ResponseResult findAssociate(UserSearchDto dto);
}
