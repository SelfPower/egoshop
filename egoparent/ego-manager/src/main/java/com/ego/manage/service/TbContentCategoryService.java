package com.ego.manage.service;

import com.ego.commons.pojo.EasyUiTree;
import com.ego.commons.pojo.EgoResult;
import com.ego.pojo.TbContentCategory;

import java.util.List;

public interface TbContentCategoryService {
    /**
     * 根据父id 查询 类别
     * @param id
     * @return
     */
    List<EasyUiTree> showTree(long id);

    /**
     * 新增
     * @param category
     * @return
     */
    EgoResult create(TbContentCategory category);

    /**
     * 类目重命名
     * @param category
     * @return
     */
    EgoResult update(TbContentCategory category);

    /**
     * 删除
     * @param category
     * @return
     */
    EgoResult delete(TbContentCategory category);

}
