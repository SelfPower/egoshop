package com.ego.manage.controller;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.pojo.EgoResult;
import com.ego.manage.service.TbContentSerivce;
import com.ego.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TbContentController {
    @Autowired
    private TbContentSerivce tbContentSerivce;

    /**
     * 显示网站内容
     * @param categoryId
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("content/query/list")
    @ResponseBody
    public EasyUIDataGrid showContent(long categoryId,int page,int rows){
        return tbContentSerivce.showContent(categoryId, page, rows);
    }

    /**
     * 新增内容
     * @param content
     * @return
     */
    public EgoResult save(TbContent content){
        EgoResult er=new EgoResult();
        int index=tbContentSerivce.save(content);
        if(index>0){
            er.setStatus(200);
        }
        return er;
    }
}
