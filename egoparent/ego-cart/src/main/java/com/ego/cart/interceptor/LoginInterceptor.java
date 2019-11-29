package com.ego.cart.interceptor;

import com.ego.commons.pojo.EgoResult;
import com.ego.commons.utils.CookieUtils;
import com.ego.commons.utils.HttpClientUtil;
import com.ego.commons.utils.JsonUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 未登录进行拦截
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        /*
        1.获取单点登录创的Cookie的类JSeesionid（token）
        2.如果得到了id，向redis中取TbUserInfo
            利用HttpClient技术向passport中利用token得到放在EgoResult中TbUserInfo的json书籍
        3.成功 返回true
        4.失败 携带商品数量的url的referer地址到登录界面
         */
        String token = CookieUtils.getCookieValue(httpServletRequest, "TT_TOKEN");
        System.out.println(token);
        if(token!=null&&!token.equals("")){
            String json= HttpClientUtil.doPost("http://localhost:8084/user/token/"+token);
            EgoResult er= JsonUtils.jsonToPojo(json,EgoResult.class);
            System.out.println(er.getStatus());
            if(er.getStatus()==200){
                return true;
            }
        }
        String num=httpServletRequest.getParameter("num");
        httpServletResponse.sendRedirect("http://localhost:8084/user/showLogin?interurl="+httpServletRequest.getRequestURL()+"?num="+num);

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
