package com.heima.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.AuthDto;
import com.heima.model.user.pojos.ApUserRealname;

public interface ApUserRealnameService extends IService<ApUserRealname> {

    /**
     * 按照状态分页查询用户列表
     *
     * @param dto
     * @return
     */
    ResponseResult loadListByStatus(AuthDto dto);

    /**
     * @param dto
     * @param status 2审核失败    9审核成功
     * @return
     */
    ResponseResult updateStatus(AuthDto dto, Short status);

}