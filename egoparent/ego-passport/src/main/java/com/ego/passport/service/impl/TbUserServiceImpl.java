package com.ego.passport.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.pojo.EgoResult;
import com.ego.commons.utils.CookieUtils;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbUserDubboService;
import com.ego.passport.service.TbUserService;
import com.ego.pojo.TbUser;
import com.ego.redis.dao.JedisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
public class TbUserServiceImpl implements TbUserService {

    @Reference
    private TbUserDubboService tbUserDubboServiceImpl;
    @Autowired
    private JedisDao jedisDaoImpl;

    @Override
    public EgoResult login(TbUser user, HttpServletRequest request, HttpServletResponse response) {
        EgoResult er=new EgoResult();
        TbUser userSelect=tbUserDubboServiceImpl.selByUser(user);
        if(userSelect!=null)
        {
            er.setStatus(200);
            //当用户登录成功后把用户信息放入到redis中 相当也session
            String key= UUID.randomUUID().toString();
            jedisDaoImpl.set(key, JsonUtils.objectToJson(userSelect));
            //设置过期时间
            jedisDaoImpl.expire(key,60*60*24*7);
            //产生Cookie
            CookieUtils.setCookie(request,response,"TT_TOKEN",key,60*60*24*7);
        }else {
            er.setMsg("用户名或密码错误！");
        }
        return er;
    }

    @Override
    public EgoResult getUserInfoByToken(String token) {
        EgoResult er=new EgoResult();
        String json=jedisDaoImpl.get(token);
        if(json!=null&&!json.equals("")){
            TbUser tbUser=JsonUtils.jsonToPojo(json,TbUser.class);
            //可以把密码清空
            tbUser.setPassword(null);
            er.setStatus(200);
            er.setMsg("OK");
            er.setData(tbUser);
        }else{
            er.setMsg("获取失败！");
        }

        return er;
    }

    @Override
    public EgoResult logout(String token, HttpServletRequest request, HttpServletResponse response) {
        Long index=jedisDaoImpl.del(token);
        CookieUtils.deleteCookie(request,response,"TT_TOKEN");
        EgoResult er=new EgoResult();
        er.setStatus(200);
        er.setMsg("OK");
        return er;
    }
}
