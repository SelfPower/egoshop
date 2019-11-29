package com.ego.dubbo.service.impl;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.dubbo.service.TbItemParamDubboService;
import com.ego.mapper.TbItemParamMapper;
import com.ego.pojo.TbItemParam;
import com.ego.pojo.TbItemParamExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TbItemParamDubboServiceImpl implements TbItemParamDubboService {
    @Autowired
    private TbItemParamMapper tbItemParamMapper;
    @Override
    public EasyUIDataGrid showpage(int page, int rows) {
        //启动分页插件
        PageHelper.startPage(page,rows);
        //得到查询结果
        List<TbItemParam> list= tbItemParamMapper.selectByExampleWithBLOBs(new TbItemParamExample());
        //把结果封装到pageInfo中
        PageInfo<TbItemParam> pi=new PageInfo<>(list);
        EasyUIDataGrid dataGrid=new EasyUIDataGrid();
        dataGrid.setTotal(pi.getTotal());
        dataGrid.setRows(pi.getList());
        return dataGrid;
    }

    @Override
    public int delById(String ids) throws Exception {
        String [] idStrs=ids.split(",");
        int index=0;
        for (String id :
                idStrs) {
            index+=tbItemParamMapper.deleteByPrimaryKey(Long.parseLong(id));
        }
        if(index==idStrs.length){
            return 1;
        }else {
            throw new Exception("删除失败，原因数据不存在！");
        }
    }

    @Override
    public TbItemParam selByCatid(long id) {
        TbItemParamExample example=new TbItemParamExample();
        example.createCriteria().andItemCatIdEqualTo(id);
        List<TbItemParam> tbItemParams = tbItemParamMapper.selectByExampleWithBLOBs(example);
        if(tbItemParams!=null&&tbItemParams.size()>0){
            //要求每个类目只能有一个模板
            return tbItemParams.get(0);
        }
        return null;
    }

    @Override
    public int insParam(TbItemParam param) {

        return tbItemParamMapper.insertSelective(param);
    }
}
