package com.ego.dubbo.service;

import com.ego.pojo.TbUser;

public interface TbUserDubboService {
    /**
     * 根据用户名密码登录
     * @param user
     * @return
     */
    TbUser selByUser(TbUser user);

}
