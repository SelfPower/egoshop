package com.ego.item.controller;

import com.ego.item.service.TbItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TbItemController {
    @Autowired
    private TbItemService tbItemServiceImpl;

    @RequestMapping("item/{id}.html")
    public String showItemDetails(@PathVariable long id, Model model){
        model.addAttribute("item",tbItemServiceImpl.show(id));
        return "item";
    }
}
