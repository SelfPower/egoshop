package com.ego.manage.controller;

import com.ego.manage.service.PicService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PicController {
    @Resource
    private PicService picServiceImpl;

    @RequestMapping("pic/upload")
    @ResponseBody
    public Map<String,Object> upload(MultipartFile uploadFile){
        Map<String,Object> map=new HashMap<>();
        try {
            map=picServiceImpl.upload(uploadFile);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error",1);
            map.put("message","上传图片异常！");
        }
        return map;
    }
}
