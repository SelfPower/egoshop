package com.ego.portal.controller;

import com.ego.portal.service.TbContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TbContentController {
    @Autowired
    private TbContentService tbContentServiceImpl;

    @RequestMapping("showBigPic")
    public String showBigPic(Model model){
        model.addAttribute("ad1",tbContentServiceImpl.showBigPic());
        return "index";
    }
}
