package com.ego.mapper;

import com.ego.pojo.TbItem;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TestMapper {
    @Select("select * from tb_item")
    List<TbItem> selectAll();
}
