package com.ego.dubbo.service;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.pojo.TbItem;
import com.ego.pojo.TbItemDesc;
import com.ego.pojo.TbItemParamItem;

import java.util.List;

public interface TbItemDubboService {
    /**
     * 商品分页查询
     * @param page
     * @param rows
     * @return
     */
    EasyUIDataGrid Show(int page,int rows);

    /**
     * 修改商品状态
     * @param tbItem
     * @return
     */
    int updItemStatus(TbItem tbItem);

    /**
     * 新增商品和商品描述
     * @param tbItem
     * @param tbItemDesc
     * @return
     */
    int insTbItemandDesc(TbItem tbItem, TbItemDesc tbItemDesc, TbItemParamItem paramItem) throws Exception;

    /**
     * 根据状态查询商品
     * @param status
     * @return
     */
    List<TbItem> selAllByStatus(byte status);

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    TbItem selById(long id);
}
