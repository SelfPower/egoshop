package com.ego.dubbo.service.impl;

import com.ego.dubbo.service.TbContentCategoryDubboService;
import com.ego.mapper.TbContentCategoryMapper;
import com.ego.pojo.TbContentCategory;
import com.ego.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TbContentCategoryDubboServiceImpl implements TbContentCategoryDubboService {
    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;
    /**
     * 根据是父类目id查询类别
     *  前台 根据 类别的json数据得到 类别id 作为父类目id 在依次查询
     * @param id
     * @return
     */
    @Override
    public List<TbContentCategory> show(long id) {
        TbContentCategoryExample example=new TbContentCategoryExample();
        example.createCriteria().andParentIdEqualTo(id);
        return tbContentCategoryMapper.selectByExample(example);
    }

    @Override
    public int insTbContentCategory(TbContentCategory tbContentCategory) {
        return tbContentCategoryMapper.insertSelective(tbContentCategory);
    }

    @Override
    public int updByIsParentById(TbContentCategory category) {
        return tbContentCategoryMapper.updateByPrimaryKey(category);
    }

    @Override
    public List<TbContentCategory> selByPid(Long parentId) {
        TbContentCategoryExample example=new TbContentCategoryExample();
        example.createCriteria().andParentIdEqualTo(parentId);
        return tbContentCategoryMapper.selectByExample(example);
    }

    @Override
    public TbContentCategory selById(long id) {
        return tbContentCategoryMapper.selectByPrimaryKey(id);
    }
}
