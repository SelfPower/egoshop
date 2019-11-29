package com.ego.dubbo.service;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.pojo.TbContent;

import java.util.List;

public interface TbContentDubboService {
    /**
     * 查询出前n个
     * @param Count  个数
     * @param isSort 是否排序
     * @return
     */
    List<TbContent> selByCount(int Count,boolean isSort);

    /**
     * 分页查询
     * @param categoryId
     * @param page
     * @param rows
     * @return
     */
    EasyUIDataGrid selContentByPage(long categoryId, int page, int rows);

    /**
     * 新增
     * @param content
     * @return
     */
    int insContent(TbContent content);
}
