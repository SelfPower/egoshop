package com.ego.portal.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbContentDubboService;
import com.ego.pojo.TbContent;
import com.ego.portal.service.TbContentService;
import com.ego.redis.dao.JedisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class TbContentServiceImpl implements TbContentService {
    @Reference
    private TbContentDubboService tbContentDubboService;

    @Autowired
    private JedisDao jedisDaoImpl;

    @Value("${redis.bigpic.key}")
    private String key;

    /*
    用redis做缓存的步骤
    流程：
        1.先判断redis中是否存在数据
        2.如果存在直接从redis中取
        3.如果不存在从mysql中取，取完后放入redis中
     */
    @Override
    public String showBigPic() {
        //存在
        if (jedisDaoImpl.exist(key)) {
            String value=jedisDaoImpl.get(key);
            if(value!=null&&!value.equals("")){
                return value;
            }
        }
        //不存在
        List<TbContent> list=tbContentDubboService.selByCount(6,true);
        List<Map<String,Object>> listResult=new ArrayList<>();
        for(TbContent tc:list){
            Map<String,Object> map=new HashMap<>();
            map.put("srcB",tc.getPic2());
            map.put("height",240);
            map.put("alt","对不起,加载图片失败");
            map.put("width",670);
            map.put("src",tc.getPic());
            map.put("widthB",550);
            map.put("href",tc.getUrl());
            map.put("heightB",240);

            listResult.add(map);
        }
        String listJson= JsonUtils.objectToJson(listResult);
        //把数据放入到redis中
        jedisDaoImpl.set(key,listJson);
        return listJson;
    }
}
