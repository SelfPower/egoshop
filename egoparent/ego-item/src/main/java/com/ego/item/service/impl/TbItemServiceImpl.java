package com.ego.item.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.pojo.TbItemChild;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.item.service.TbItemService;
import com.ego.pojo.TbItem;
import com.ego.redis.dao.JedisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TbItemServiceImpl implements TbItemService {
    @Reference
    private TbItemDubboService tbItemDubboService;

    @Autowired
    private JedisDao jedisDaoImpl;

    @Value("${redis.item.key}")
    private String itemKey;
    @Override
    public TbItemChild show(long id) {
        //如果redis中存在该数据，直接把json数据转化为实体类对象
        String key=itemKey+id;
        if(jedisDaoImpl.exist(key)){
            String json=jedisDaoImpl.get(key);
            if(json!=null&&!json.equals("")){
                return JsonUtils.jsonToPojo(json,TbItemChild.class);
            }
        }



        TbItem item=tbItemDubboService.selById(id);
        TbItemChild child=new TbItemChild();
        child.setId(item.getId());
        child.setTitle(item.getTitle());
        child.setPrice(item.getPrice());
        child.setSellPoint(item.getSellPoint());

        child.setImages(item.getImage()!=null&&!item.equals("")?item.getImage().split(","):new String[1]);


        //把数据存到数据库中
        jedisDaoImpl.set(key,JsonUtils.objectToJson(child));

        return child;

    }
}
