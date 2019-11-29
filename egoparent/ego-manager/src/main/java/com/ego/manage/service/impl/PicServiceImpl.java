package com.ego.manage.service.impl;

import com.ego.commons.utils.FtpUtil;
import com.ego.commons.utils.IDUtils;
import com.ego.manage.service.PicService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
@Service
public class PicServiceImpl implements PicService {
    @Value("${ftpclient.host}")
    private String host;
    @Value("${ftpclient.port}")
    private int port;
    @Value("${ftpclient.username}")
    private String username;
    @Value("${ftpclient.password}")
    private String password;
    @Value("${ftpclient.basepath}")
    private String basepath;
    @Value("${ftpclient.filepath}")
    private String filepath;
    @Override
    public Map<String, Object> upload(MultipartFile file) throws Exception{
//        System.out.println(file.toString());
        //为图片生成名
        String picName= IDUtils.genImageName()+file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        boolean result = FtpUtil.uploadFile(host, port, username, password, basepath, filepath, picName, file.getInputStream());
        Map<String,Object> map=new HashMap<>();
        if(result){
            map.put("error",0);
            map.put("url","http://"+host+"/"+picName);
        }else {
            map.put("error",1);
            map.put("message","图片上传失败");
        }
        return map;
    }
}
