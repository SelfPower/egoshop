package com.ego.manage.controller;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.pojo.EgoResult;
import com.ego.manage.service.TbItemService;
import com.ego.pojo.TbContentCategory;
import com.ego.pojo.TbItem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class TbItemController {
    @Resource
    private TbItemService tbItemServiceImpl;

    @RequestMapping("item/list")
    @ResponseBody
    public EasyUIDataGrid show(int page,int rows){
        return tbItemServiceImpl.show(page,rows);
    }

    /**
     * 商品删除
     * @param ids
     * @return
     */
    @RequestMapping("rest/item/delete")
    @ResponseBody
    public EgoResult delete(String ids){
        EgoResult er=new EgoResult();
        int i = tbItemServiceImpl.updItemStatus(ids, (byte) 3);
        if(i==1){
            er.setStatus(200);
        }
        return er;
    }

    /**
     * 商品下架
     * @param ids
     * @return
     */
    @RequestMapping("rest/item/instock")
    @ResponseBody
    public EgoResult instock(String ids){
        EgoResult er=new EgoResult();
        int i = tbItemServiceImpl.updItemStatus(ids, (byte) 2);
        if(i==1){
            er.setStatus(200);
        }
        return er;
    }

    /**
     * 商品上架
     * @param ids
     * @return
     */
    @RequestMapping("rest/item/reshelf")
    @ResponseBody
    public EgoResult reshelf(String ids){
        EgoResult er=new EgoResult();
        int i = tbItemServiceImpl.updItemStatus(ids, (byte) 1);
        if(i==1){
            er.setStatus(200);
        }
        return er;
    }
    @RequestMapping("item/save")
    @ResponseBody
    public EgoResult insert(TbItem item,String desc,String itemParams){
        EgoResult er=new EgoResult();
        int index;
        try {
            index=tbItemServiceImpl.save(item,desc,itemParams);
            if(index==1){
                er.setStatus(200);
            }
        }catch (Exception e){
            er.setData(e.getMessage());
        }
        return er;
    }


}
