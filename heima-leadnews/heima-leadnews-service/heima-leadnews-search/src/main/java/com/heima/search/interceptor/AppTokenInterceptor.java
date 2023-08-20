package com.heima.search.interceptor;

import com.heima.model.user.pojos.ApUser;
import com.heima.utils.thread.ApThreadLocalUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author LiJing
 * @version 1.0
 */
public class AppTokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader("userId");
        if (userId != null) {
            //把用户id存入threadloacl中
            ApUser apUser = new ApUser();
            apUser.setId(Integer.valueOf(userId));
            ApThreadLocalUtil.setUser(apUser);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ApThreadLocalUtil.clear();
    }
}
