package com.ego.dubbo.service.impl;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.mapper.TbItemDescMapper;
import com.ego.mapper.TbItemMapper;
import com.ego.mapper.TbItemParamItemMapper;
import com.ego.pojo.TbItem;
import com.ego.pojo.TbItemDesc;
import com.ego.pojo.TbItemExample;
import com.ego.pojo.TbItemParamItem;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TbItemDubboServiceImpl implements TbItemDubboService {
    @Autowired
    private TbItemMapper tbItemMapper;

    @Autowired
    private TbItemDescMapper tbItemDescMapper;
    @Autowired
    private TbItemParamItemMapper tbItemParamItemMapper;
//    @Resource
//    private TestMapper testMapper;
    @Override
    public EasyUIDataGrid Show(int page, int rows) {
        //启用分页工具
        PageHelper.startPage(page,rows);
        //查询全部
        List<TbItem> list =tbItemMapper.selectByExample(new TbItemExample());
//        List<TbItem> list=testMapper.selectAll();
        //分页代码
        //设置分页条件
        PageInfo<TbItem> pi=new PageInfo<>(list);
        //放入实体内中
        EasyUIDataGrid dataGrid=new EasyUIDataGrid();
        dataGrid.setRows(pi.getList());
        dataGrid.setTotal(pi.getTotal());
        return dataGrid;
    }

    @Override
    public int updItemStatus(TbItem tbItem) {
        return tbItemMapper.updateByPrimaryKeySelective(tbItem);
    }

    @Override
    public int insTbItemandDesc(TbItem tbItem, TbItemDesc tbItemDesc, TbItemParamItem paramItem) throws Exception {
        int index=0;
        try {
            index += tbItemMapper.insertSelective(tbItem);
            index += tbItemDescMapper.insertSelective(tbItemDesc);
            index+=tbItemParamItemMapper.insertSelective(paramItem);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(index==3){
            return 1;
        }else {
            throw new Exception("新增失败，数据还原");
        }
    }

    @Override
    public List<TbItem> selAllByStatus(byte status) {
        TbItemExample example=new TbItemExample();
        example.createCriteria().andStatusEqualTo(status);
        return tbItemMapper.selectByExample(example);
    }

    @Override
    public TbItem selById(long id) {

        return tbItemMapper.selectByPrimaryKey(id);
    }
}
