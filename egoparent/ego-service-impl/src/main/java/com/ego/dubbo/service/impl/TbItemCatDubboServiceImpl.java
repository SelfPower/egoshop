package com.ego.dubbo.service.impl;

import com.ego.dubbo.service.TbItemCatDubboService;
import com.ego.mapper.TbItemCatMapper;
import com.ego.pojo.TbItemCat;
import com.ego.pojo.TbItemCatExample;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TbItemCatDubboServiceImpl implements TbItemCatDubboService {
    @Autowired
    private TbItemCatMapper tbItemCatMapper;
    @Override
    public List<TbItemCat> show(long id) {
        //创建查询对象
        TbItemCatExample example=new TbItemCatExample();
        example.createCriteria().andParentIdEqualTo(id);
        return tbItemCatMapper.selectByExample(example);
    }

    @Override
    public TbItemCat selById(long id) {
        return tbItemCatMapper.selectByPrimaryKey(id);
    }
}
