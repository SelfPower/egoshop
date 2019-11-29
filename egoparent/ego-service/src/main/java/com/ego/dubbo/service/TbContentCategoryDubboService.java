package com.ego.dubbo.service;

import com.ego.pojo.TbContentCategory;

import java.util.List;

public interface TbContentCategoryDubboService {

    /**
     * 根据是父类目id查询子类别
     * @param id
     * @return
     */
    List<TbContentCategory> show(long id);

    /**
     * 新增
     * @param tbContentCategory
     * @return
     */
    int insTbContentCategory(TbContentCategory tbContentCategory);

    /**
     * 删除
     * @param id
     * @return
     */

//    int delById(long id);


    /**
     * 改节点为数父节点
     * @param category
     * @return
     */
    int updByIsParentById(TbContentCategory category);

    /**
     * 根据父id查询类别
     * @param parentId
     * @return
     */
    List<TbContentCategory> selByPid(Long parentId);

    /**
     * 通过id查询内容类目详细信息
     * @param id
     * @return
     */
    TbContentCategory selById(long id);
}
