package com.ego.dubbo.service.impl;

import com.ego.dubbo.service.TbUserDubboService;
import com.ego.mapper.TbUserMapper;
import com.ego.pojo.TbUser;
import com.ego.pojo.TbUserExample;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TbUserDubboServiceImpl implements TbUserDubboService {

    @Autowired
    private TbUserMapper tbUserMapper;

    @Override
    public TbUser selByUser(TbUser user) {
        TbUserExample example=new TbUserExample();
        example.createCriteria().andUsernameEqualTo(user.getUsername()).andPasswordEqualTo(user.getPassword());
        List<TbUser> userList = tbUserMapper.selectByExample(example);
        if(userList!=null&&userList.size()>0){
            return userList.get(0);
        }
        return null;
    }
}
