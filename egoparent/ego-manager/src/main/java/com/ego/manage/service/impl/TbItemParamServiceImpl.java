package com.ego.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.pojo.EgoResult;
import com.ego.dubbo.service.TbItemCatDubboService;
import com.ego.dubbo.service.TbItemParamDubboService;
import com.ego.manage.pojo.TbItemParamChild;
import com.ego.manage.service.TbItemParamService;
import com.ego.pojo.TbItemParam;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TbItemParamServiceImpl implements TbItemParamService {
    @Reference
    private TbItemParamDubboService tbItemParamDubboServiceImpl;
    @Reference
    private TbItemCatDubboService tbItemCatDubboServiceImpl;
    @Override
    public EasyUIDataGrid showPage(int page, int rows) {
        EasyUIDataGrid dataGrid = tbItemParamDubboServiceImpl.showpage(page, rows);
//        得到分页后的数据
        List<TbItemParam> list=(List<TbItemParam>)dataGrid.getRows();
        List<TbItemParamChild> listChild=new ArrayList<>();
        for (TbItemParam param :
                list) {
            TbItemParamChild child = new TbItemParamChild();
            child.setId(param.getId());
            child.setItemCatId(param.getItemCatId());
            child.setCreated(param.getCreated());
            child.setUpdated(param.getUpdated());
            child.setParamData(param.getParamData());
            child.setItemCatName(tbItemCatDubboServiceImpl.selById(param.getItemCatId()).getName());
            listChild.add(child);
        }
        dataGrid.setRows(listChild);
        return dataGrid;
    }

    @Override
    public int delete(String ids) throws Exception {
        return tbItemParamDubboServiceImpl.delById(ids);
    }

    @Override
    public EgoResult showparam(long catId) {
        EgoResult er=new EgoResult();
        TbItemParam tbItemParam = tbItemParamDubboServiceImpl.selByCatid(catId);
        if(tbItemParam!=null){
            er.setStatus(200);
            er.setData(tbItemParam);
        }
        return er;
    }

    /**
     * 新增模板信息
     * @param param
     * @return
     */
    @Override
    public EgoResult save(TbItemParam param) {
        Date date =new Date();
        param.setCreated(date);
        param.setUpdated(date);
        int index=tbItemParamDubboServiceImpl.insParam(param);
        EgoResult er=new EgoResult();
        if(index>0){
            er.setStatus(200);
        }
        return er;
    }
}
