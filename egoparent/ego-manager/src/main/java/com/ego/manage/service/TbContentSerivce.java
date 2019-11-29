package com.ego.manage.service;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.pojo.TbContent;

public interface TbContentSerivce {
    /**
     * 分页显示内容信息
     * @param categoruId
     * @param page
     * @param rows
     * @return
     */
    EasyUIDataGrid showContent(long categoruId,int page,int rows);

    /**
     * 新增内容
     * @param content
     * @return
     */
    int save(TbContent content);
}
