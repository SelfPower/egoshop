package com.ego.dubbo.service;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.pojo.TbItemParam;

public interface TbItemParamDubboService {

    /**
     * 显示产品规格模板
     * @param page
     * @param rows
     * @return
     */
    EasyUIDataGrid showpage(int page,int rows);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    int delById(String ids) throws Exception;

    /**
     * 根据类目id查询参数模板
     * @param id
     * @return
     */
    TbItemParam selByCatid(long id);

    /**
     * 新增，支持主键自增
     * @param param
     * @return
     */
    int insParam(TbItemParam param);
}
