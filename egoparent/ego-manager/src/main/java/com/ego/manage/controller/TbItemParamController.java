package com.ego.manage.controller;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.pojo.EgoResult;
import com.ego.manage.service.TbItemParamService;
import com.ego.pojo.TbItemParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TbItemParamController {
    @Autowired
    private TbItemParamService tbItemParamServiceImpl;

    /**
     * 显示规格列表
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("item/param/list")
    @ResponseBody
    public EasyUIDataGrid show(int page,int rows){
        return tbItemParamServiceImpl.showPage(page, rows);
    }

    /**
     * 批量删除规格ItemParam
     * @param ids
     * @return
     */
    @RequestMapping("item/param/delete")
    @ResponseBody
    public EgoResult delete(String ids){
        EgoResult er =new EgoResult();
        try {
            int index = tbItemParamServiceImpl.delete(ids);
            if(index==1){
                er.setStatus(200);
            }
        }catch (Exception e){
            e.printStackTrace();
            er.setStatus(400);
            er.setData(e.getMessage());
        }
        return er;
    }

    /**
     * 点击商品类目按钮显示添加分组按键
     * 判断类目是否已经添加模板
     * @param catId
     * @return
     */
    @RequestMapping("item/param/query/itemcatid/{catId}")
    @ResponseBody
    public EgoResult showParam(@PathVariable long catId){
        return tbItemParamServiceImpl.showparam(catId);
    }

    /**
     * 商品类目新增
     * @param param
     * @param catId
     * @return
     */
    @RequestMapping("item/param/save/{catId}")
    @ResponseBody
    public EgoResult save(TbItemParam param,@PathVariable long catId){
        param.setItemCatId(catId);
        return tbItemParamServiceImpl.save(param);
    }
}
