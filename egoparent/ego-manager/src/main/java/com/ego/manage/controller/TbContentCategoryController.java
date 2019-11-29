package com.ego.manage.controller;

import com.ego.commons.pojo.EasyUiTree;
import com.ego.commons.pojo.EgoResult;
import com.ego.manage.service.TbContentCategoryService;
import com.ego.pojo.TbContentCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class TbContentCategoryController {
    @Autowired
    private TbContentCategoryService tbContentCategoryServiceImpl;

    /**
     * 显示类目
     * @param id
     * @return
     */
    @RequestMapping("content/category/list")
    @ResponseBody
    public  List<EasyUiTree> showTree(@RequestParam(defaultValue = "0") long id){
        return tbContentCategoryServiceImpl.showTree(id);
    }

    /**
     * 新增
     * @param cate
     * @return
     */
    @RequestMapping("content/category/create")
    @ResponseBody
    public EgoResult create(TbContentCategory cate){
        return tbContentCategoryServiceImpl.create(cate);
    }

    /**
     * 修改
     * @param category
     * @return
     */
    @RequestMapping("content/category/update")
    @ResponseBody
    public EgoResult update(TbContentCategory category){
        return tbContentCategoryServiceImpl.update(category);
    }

    /**
     * 删除
     * @param category
     * @return
     */
    @RequestMapping("content/category/delete")
    @ResponseBody
    public EgoResult delete(TbContentCategory category){
        return tbContentCategoryServiceImpl.delete(category);
    }

}
