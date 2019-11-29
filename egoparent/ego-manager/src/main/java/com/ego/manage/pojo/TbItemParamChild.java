package com.ego.manage.pojo;

import com.ego.pojo.TbItemParam;

/**
 * 作用封装jsp中TbItemParam额外的数据
 */
public class TbItemParamChild extends TbItemParam {

    private String itemCatName;

    public String getItemCatName() {
        return itemCatName;
    }

    public void setItemCatName(String itemCatName) {
        this.itemCatName = itemCatName;
    }
}
