package com.ego.item.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbItemParamItemDubboService;
import com.ego.item.pojo.ParamItem;
import com.ego.item.service.TbItemParamItemService;
import com.ego.pojo.TbItemParamItem;
import com.ego.redis.dao.JedisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TbItemParamItemServiceImpl implements TbItemParamItemService {
    @Reference
    private TbItemParamItemDubboService tbItemParamItemDubboServiceImpl;

    @Autowired
    private JedisDao jedisDaoImpl;
    @Value("${redis.paramItem.key}")
    private String paramItemKey;

    @Override
    public String showParam(long itemId) {
        TbItemParamItem item=null;

        String key=paramItemKey+itemId;
        if(jedisDaoImpl.exist(key)){
            String json=jedisDaoImpl.get(key);
            if(json!=null&&!json.equals("")){
                item=JsonUtils.jsonToPojo(json,TbItemParamItem.class);
            }
        }else {
            item = tbItemParamItemDubboServiceImpl.selByItemid(itemId);
            jedisDaoImpl.set(key,JsonUtils.objectToJson(item));
        }
        List<ParamItem> list = JsonUtils.jsonToList(item.getParamData(), ParamItem.class);
        System.out.println(list);
        StringBuffer sf = new StringBuffer();

        for (ParamItem param : list) {
            sf.append("<table width='500' style='color:gray;'>");
            for (int i = 0 ;i<param.getParams().size();i++) {
                if(i==0){
                    sf.append("<tr>");
                    sf.append("<td align='right' width='30%'>"+param.getGroup()+"</td>");
                    sf.append("<td align='right' width='30%'>"+param.getParams().get(i).getK()+"</td>");
                    sf.append("<td>"+param.getParams().get(i).getV()+"</td>");
                    sf.append("<tr/>");
                }else{
                    sf.append("<tr>");
                    sf.append("<td> </td>");
                    sf.append("<td align='right'>"+param.getParams().get(i).getK()+"</td>");
                    sf.append("<td>"+param.getParams().get(i).getV()+"</td>");
                    sf.append("</tr>");
                }
            }
            sf.append("</table>");
            sf.append("<hr style='color:gray;'/>");
        }
        return sf.toString();

    }
}
