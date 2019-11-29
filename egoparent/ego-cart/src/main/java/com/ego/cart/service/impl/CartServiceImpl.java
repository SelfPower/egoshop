package com.ego.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.cart.service.CartService;
import com.ego.commons.pojo.EgoResult;
import com.ego.commons.pojo.TbItemChild;
import com.ego.commons.utils.CookieUtils;
import com.ego.commons.utils.HttpClientUtil;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.pojo.TbItem;
import com.ego.redis.dao.JedisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private JedisDao jedisDaoImpl;

    @Reference
    private TbItemDubboService tbItemDubboServiceImpl;

    @Value("${passport.url}")
    private String passportUrl;
    @Value("${cart.key}")
    private String cartKey;
    @Override
    public void addCart(long id, int num, HttpServletRequest request) {
        /*
         * 创建一个集合存放购物车
         * 向redis中取token得到放有user的EgoRusult对象
         * 创建一个购物车的key cart：+username
         *
         * 判断 key是否在redis中存在
         *  存在 取出购物车信息，把加上num 再把修改后的数据放入redis
         *  不存在
         *      根据商品id查询商品信息
         *      把信息放入TbItemChild
         *      把child放入购物车list
         *      把购物车放入redis
         */
        List<TbItemChild> list=new ArrayList<>();
        String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
        String jsonUser= HttpClientUtil.doPost(passportUrl+token);
        EgoResult er= JsonUtils.jsonToPojo(jsonUser,EgoResult.class);
        String key=cartKey+((LinkedHashMap)er.getData()).get("username");
        //如果redis中存在key
        if(jedisDaoImpl.exist(key)){
            String json=jedisDaoImpl.get(key);
            if(json!=null&&!json.equals("")){
                list=JsonUtils.jsonToList(json,TbItemChild.class);
                for(TbItemChild tbItemChild:list){
                    if((long)tbItemChild.getId()==id){
                        tbItemChild.setNum(tbItemChild.getNum()+num);
                        jedisDaoImpl.set(key,JsonUtils.objectToJson(list));
                        return;
                    }
                }
            }
        }
        TbItem item =tbItemDubboServiceImpl.selById(id);
        TbItemChild child=new TbItemChild();
        child.setId(item.getId());
        child.setTitle(item.getTitle());
        child.setImages(item.getImage()==null||item.getImage().equals("")?new String[1]:item.getImage().split(","));
        child.setNum(item.getNum());
        child.setPrice(item.getPrice());
        list.add(child);
        jedisDaoImpl.set(key,JsonUtils.objectToJson(list));

    }

    @Override
    public List<TbItemChild> showCart(HttpServletRequest request) {
        String token=CookieUtils.getCookieValue(request,"TT_TOKEN");
        String jsonUser=HttpClientUtil.doPost(passportUrl+token);
        EgoResult er=JsonUtils.jsonToPojo(jsonUser,EgoResult.class);
        String key=cartKey+((LinkedHashMap)er.getData()).get("username");
        String json=jedisDaoImpl.get(key);
        return JsonUtils.jsonToList(json,TbItemChild.class);
    }

    @Override
    public EgoResult update(long id, int num, HttpServletRequest request) {
        String token =CookieUtils.getCookieValue(request,"TT_TOKEN");
        String jsonUser=HttpClientUtil.doPost(passportUrl+token);
        EgoResult er=JsonUtils.jsonToPojo(jsonUser,EgoResult.class);
        String key=cartKey+((LinkedHashMap)er.getData()).get("username");
        String json =jedisDaoImpl.get(key);
        List<TbItemChild> list=JsonUtils.jsonToList(json,TbItemChild.class);
        for(TbItemChild child:list){
            if((long)child.getId()==id)
                child.setNum(num);
        }
        String ok=jedisDaoImpl.set(key,JsonUtils.objectToJson(list));
        EgoResult egoResult=new EgoResult();
        if(ok.equals("OK")){
            egoResult.setStatus(200);
        }
        return egoResult;
    }

    @Override
    public EgoResult delete(long id, HttpServletRequest request) {
        String token =CookieUtils.getCookieValue(request,"TT_TOKEN");
        String jsonUser=HttpClientUtil.doPost(passportUrl+token);
        EgoResult er=JsonUtils.jsonToPojo(jsonUser,EgoResult.class);
        String key=cartKey+((LinkedHashMap)er.getData()).get("username");
        String json =jedisDaoImpl.get(key);
        List<TbItemChild> list=JsonUtils.jsonToList(json,TbItemChild.class);
        TbItemChild tbItemChild=null;
        for(TbItemChild child:list){
            if((long)child.getId()==id)
                tbItemChild=child;
        }
        list.remove(tbItemChild);
        String ok =jedisDaoImpl.set(key,JsonUtils.objectToJson(list));
        EgoResult egoResult=new EgoResult();
        if(ok.equals("OK")){
            egoResult.setStatus(200);
        }
        return egoResult;
    }


}
