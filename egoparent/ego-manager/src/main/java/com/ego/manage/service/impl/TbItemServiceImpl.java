package com.ego.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.utils.HttpClientUtil;
import com.ego.commons.utils.IDUtils;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbItemDescDubboService;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.manage.service.TbItemService;
import com.ego.pojo.TbItem;
import com.ego.pojo.TbItemDesc;
import com.ego.pojo.TbItemParam;
import com.ego.pojo.TbItemParamItem;
import com.ego.redis.dao.JedisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 上架不需要操作redis，下架和删除都是从redis中删除key
 */
@Service
public class TbItemServiceImpl implements TbItemService {

    @Reference //dubbo动态代理
    private TbItemDubboService tbItemDubboService;

    @Reference
    private TbItemDescDubboService tbItemDescDubboService;

    @Value("${search.url}")
    private String url;

    @Autowired
    private JedisDao jedisDaoImpl;

    @Value("${redis.item.key}")
    private String itemKey;
    @Override
    public EasyUIDataGrid show(int page, int rows) {
        return tbItemDubboService.Show(page,rows);
    }

    @Override
    public int updItemStatus(String ids, byte status) {
        int index=0;
        TbItem tbItem=new TbItem();
        String[] idsStr=ids.split(",") ;
        for(String id : idsStr){
            tbItem.setId(Long.parseLong(id));
            tbItem.setStatus(status);
            index +=tbItemDubboService.updItemStatus(tbItem);
            //status 1 上架，23 下架删除
            if(status==2||status==3){
                jedisDaoImpl.del(itemKey+id);
            }
        }
        if(index==idsStr.length){
            return 1;
        }
        return 0;
    }

    @Override
    public int save(TbItem item, String desc,String itemParams) throws Exception {
        long id =IDUtils.genItemId();
        item.setId(id);
        Date date=new Date();
        item.setCreated(date);
        item.setUpdated(date);
        item.setStatus((byte)1);
        TbItemDesc itemDesc=new TbItemDesc();
        itemDesc.setItemDesc(desc);
        itemDesc.setItemId(id);
        itemDesc.setCreated(date);
        itemDesc.setUpdated(date);

        TbItemParamItem paramItem=new TbItemParamItem();
        paramItem.setCreated(date);
        paramItem.setUpdated(date);
        paramItem.setItemId(id);
        paramItem.setParamData(itemParams);
        int index=0;
        index=tbItemDubboService.insTbItemandDesc(item,itemDesc,paramItem);

        final TbItem itemFinal=item;
        final String descFinal=desc;
        new Thread(){
            public void run(){
                Map<String,Object> map=new HashMap<>();
                map.put("item",itemFinal);
                map.put("desc",descFinal);
                HttpClientUtil.doPostJson(url, JsonUtils.objectToJson(map));
                //使用java代码调用其他项目的控制器
            }
        }.start();

        return index;
    }


}
