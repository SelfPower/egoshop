package com.ego.dubbo.service;

import com.ego.pojo.TbItemCat;

import java.util.List;

public interface TbItemCatDubboService {

    /**
     * 根据父id查子商品
     * @param id
     * @return
     */
    List<TbItemCat> show(long id);

    /**
     * 根据Id查询指定商品
     * @param id
     * @return
     */
    TbItemCat selById(long id);

}
