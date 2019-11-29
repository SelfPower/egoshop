package com.ego.manage.service;
import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.pojo.TbItem;

public interface TbItemService {

    /**
     * 查询全部
     * @param page
     * @param rows
     * @return
     */
    EasyUIDataGrid show(int page,int rows);

    /**
     *
     * @param ids
     * @param status
     * @return
     */
    int updItemStatus(String ids,byte status);

    int save(TbItem item ,String desc,String itemParams) throws Exception;
}
