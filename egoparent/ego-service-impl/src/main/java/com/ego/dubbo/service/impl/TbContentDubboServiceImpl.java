package com.ego.dubbo.service.impl;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.dubbo.service.TbContentDubboService;
import com.ego.mapper.TbContentMapper;
import com.ego.pojo.TbContent;
import com.ego.pojo.TbContentExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TbContentDubboServiceImpl implements TbContentDubboService {
    @Autowired
    public TbContentMapper tbContentMapper;
    @Override
    public List<TbContent> selByCount(int count, boolean isSort) {
        //条件查询
        TbContentExample example=new TbContentExample();
        //是否需要根据修改时间排序
        if(isSort) {
            example.setOrderByClause("updated desc");
        }
        if(count!=0){
            //查询指定个数  采用分页查询
            PageHelper.startPage(1,count);
            List<TbContent> list=tbContentMapper.selectByExample(example);
            PageInfo<TbContent> pi=new PageInfo<>(list);
            return pi.getList();
        }else
        return tbContentMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public int insContent(TbContent content) {
        return tbContentMapper.insertSelective(content);
    }
    @Override
    public EasyUIDataGrid selContentByPage(long categoryId, int page, int rows) {
        PageHelper.startPage(page, rows);
        TbContentExample example = new TbContentExample();
        if(categoryId!=0){
            example.createCriteria().andCategoryIdEqualTo(categoryId);
        }
        List<TbContent> list = tbContentMapper.selectByExampleWithBLOBs(example);

        PageInfo<TbContent> pi = new PageInfo<>(list);

        EasyUIDataGrid datagrid = new EasyUIDataGrid();
        datagrid.setRows(pi.getList());
        datagrid.setTotal(pi.getTotal());
        return datagrid;
    }
}
