package com.ego.item.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.dubbo.service.TbItemCatDubboService;
import com.ego.item.pojo.PortalMenu;
import com.ego.item.pojo.PortalMenuNode;
import com.ego.item.service.TbItemCatService;
import com.ego.pojo.TbItemCat;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class TbItemCatServiceImpl implements TbItemCatService {

    @Reference
    private TbItemCatDubboService tbItemCatDubboService;

    /**
     * 显示导航 查询出所有所有类目并转圜为特定格式
     * @return
     */
    @Override
    public PortalMenu showCatMenu() {
        //查询出所有一级菜单
        List<TbItemCat> list=tbItemCatDubboService.show(0);
        PortalMenu pm =new PortalMenu();
        pm.setData(selAllMenu(list));
        return pm;
    }

    /**
     * 把查询结果转换为他的格式
     * @param list
     * @return
     */
    public List<Object> selAllMenu(List<TbItemCat> list){
        List<Object> listNode=new ArrayList<>();
        for(TbItemCat tbItemCat:list){
            //如果他是父目录 把他解析成特定格式
            if(tbItemCat.getIsParent()){
                PortalMenuNode pmd=new PortalMenuNode();
                pmd.setU("/products/"+tbItemCat.getId()+".html");
                pmd.setN("<a href='/products/"+tbItemCat.getId()+".html'>"+tbItemCat.getName()+"</a>");
                pmd.setI(selAllMenu(tbItemCatDubboService.show(tbItemCat.getId())));
                listNode.add(pmd);
            }else {
                listNode.add("/products/"+tbItemCat.getId()+".html|"+tbItemCat.getName());
            }
        }
        return listNode;
    }
}
