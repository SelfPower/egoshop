package com.ego.item.controller;

import com.ego.item.service.TbItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
public class TbItemCatController {
    @Autowired
    private TbItemCatService tbItemCatServiceImpl;

    /**
     * 放回jsonp数据格式包含所有菜单信息
     * @param callback
     * @return
     */
    @RequestMapping("rest/itemcat/all")
    @ResponseBody
    public MappingJacksonValue showMenu(String callback){
        MappingJacksonValue mjv=new MappingJacksonValue(tbItemCatServiceImpl.showCatMenu());
        mjv.setJsonpFunction(callback);
        return mjv;
    }
}
