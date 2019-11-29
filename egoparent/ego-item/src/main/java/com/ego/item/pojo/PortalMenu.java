package com.ego.item.pojo;

import java.util.List;

/**
 * 最终返回给ego-portal的jsonp数据中函数的实体类
 */
public class PortalMenu {
    /**
     * 最终返回给ego-portal的jsonp数据中函数的参数值
     */
    private List<Object> data;

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }
}
