package com.ego.item.service;

import com.ego.commons.pojo.TbItemChild;

public interface TbItemService {

    /**
     * 根据主键id显示商品详情
     * @param id
     * @return
     */
    TbItemChild show(long id);
}
