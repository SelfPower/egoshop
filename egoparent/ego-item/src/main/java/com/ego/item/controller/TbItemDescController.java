package com.ego.item.controller;

import com.ego.item.service.TbItemDescService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TbItemDescController {
    @Autowired
    private TbItemDescService tbItemDescServiceImpl;

    @RequestMapping(value = "item/desc/{id}.html",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String desc(@PathVariable long id){ //@PathVariable 接收请求路径中占位符的值
        return tbItemDescServiceImpl.showDesc(id);
    }

}
