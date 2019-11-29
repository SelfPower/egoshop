package com.ego.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.pojo.EasyUiTree;
import com.ego.commons.pojo.EgoResult;
import com.ego.commons.utils.IDUtils;
import com.ego.dubbo.service.TbContentCategoryDubboService;
import com.ego.manage.service.TbContentCategoryService;
import com.ego.pojo.TbContentCategory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TbContentCategoryServiceImpl implements TbContentCategoryService {
    @Reference
    private TbContentCategoryDubboService tbContentCategoryDubboServiceImpl;
    @Override
    public List<EasyUiTree> showTree(long id) {
        List<TbContentCategory> list = tbContentCategoryDubboServiceImpl.show(id);
        List<EasyUiTree> listTree=new ArrayList<>();
        for(TbContentCategory tree: list){
            EasyUiTree et=new EasyUiTree();
            et.setId(tree.getId());
            et.setText(tree.getName());
            //如果是父节点 节点closed
            et.setState(tree.getIsParent()?"closed":"open");
            listTree.add(et);
        }
        return listTree;
    }

    @Override
    public EgoResult create(TbContentCategory category) {
        EgoResult er=new EgoResult();
        //判断当前节点名是否存在
        List<TbContentCategory> children = tbContentCategoryDubboServiceImpl.selByPid(category.getParentId());
        for(TbContentCategory child:children){
            if(child.getName().equals(category.getName())){
                er.setData("该分类名已存在！");
                return er;
            }
        }
        Date date=new Date();
        category.setCreated(date);
        category.setUpdated(date);
        category.setStatus(1);
        category.setIsParent(false);
        category.setSortOrder(1);
        long id =IDUtils.genItemId();
        category.setId(id);
        int index=tbContentCategoryDubboServiceImpl.insTbContentCategory(category);
        //如果插入成功 修改父节点 为父节点
        if(index>0){
            TbContentCategory parent=new TbContentCategory();
            parent.setId(category.getParentId());
            parent.setIsParent(true);
            tbContentCategoryDubboServiceImpl.updByIsParentById(parent);
        }
        er.setStatus(200);
        Map<String,Long> map=new HashMap<>();
        map.put("id",id);
        er.setData(map);
        return er;
    }

    @Override
    public EgoResult update(TbContentCategory category) {
        EgoResult er=new EgoResult();
        //查询当前节点信息
        TbContentCategory cateSelet=tbContentCategoryDubboServiceImpl.selById(category.getId());
        //查询当前节点的父节点的所有子节点信息
        List<TbContentCategory> children=tbContentCategoryDubboServiceImpl.selByPid(cateSelet.getParentId());
        for(TbContentCategory child: children){
            if(child.getName().equals(category.getName())){
                er.setData("该分类名已存在！");
                return er;
            }
        }
        int index=tbContentCategoryDubboServiceImpl.updByIsParentById(category);
        if(index>0){
            er.setStatus(200);
        }
        return er;
    }

    @Override
    public EgoResult delete(TbContentCategory category) {
        EgoResult er=new EgoResult();
        category.setStatus(0);
        int index=tbContentCategoryDubboServiceImpl.updByIsParentById(category);
        if(index>0){
            TbContentCategory cur=tbContentCategoryDubboServiceImpl.selById(category.getId());
            List<TbContentCategory> list = tbContentCategoryDubboServiceImpl.selByPid(cur.getParentId());
            if(list==null||list.size()==0) {
                TbContentCategory parent = new TbContentCategory();
                parent.setId(cur.getParentId());
                parent.setIsParent(false);
                int result = tbContentCategoryDubboServiceImpl.updByIsParentById(parent);
                if (result > 0) {
                    er.setStatus(200);
                }
            }else {
                    er.setStatus(200);
                }
            }
        return er;
    }
}
