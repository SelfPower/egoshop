package com.ego.search.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.dubbo.service.TbItemCatDubboService;
import com.ego.dubbo.service.TbItemDescDubboService;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.pojo.TbItem;
import com.ego.pojo.TbItemCat;
import com.ego.pojo.TbItemDesc;
import com.ego.search.pojo.TbItemChild;
import com.ego.search.service.TbItemService;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TbItemServiceImpl implements TbItemService {
    
    @Reference
    private TbItemDubboService tbItemDubboServiceImpl;
    
    @Reference
    private TbItemCatDubboService tbItemCatDubboServiceImpl;
    
    @Reference
    private TbItemDescDubboService tbItemDescDubboServiceImpl;

    @Autowired
    private CloudSolrClient solrClient;
    /*
    初始化逻辑
      一、solr初始化 add
        1.引入solrClient对象 （进入图形化界面）
        2.创建SolrInputDocument对象  doc xml方式
        3.向doc中添加Field
      二、根据需求添加Field
        1.查询到所有状态正常的商品
        2.
      三、把doc对象加入solrClient中
      四、solrClient提交
     */
    @Override
    public void init() throws IOException, SolrServerException {
        solrClient.deleteByQuery("*:*");
        solrClient.commit();
        List<TbItem> tbItems = tbItemDubboServiceImpl.selAllByStatus((byte) 1);
        for (TbItem tbItem :
                tbItems) {
            TbItemCat tbItemCat = tbItemCatDubboServiceImpl.selById(tbItem.getCid());
//            System.out.println(tbItem.getId());
            TbItemDesc itemDesc = tbItemDescDubboServiceImpl.selByItemid(tbItem.getId());

            /*
            <field name="item_title" type="text_ik" indexed="true" stored="true"/>
<field name="item_sell_point" type="text_ik" indexed="true" stored="true"/>
<field name="item_price"  type="long" indexed="true" stored="true"/>
<field name="item_image" type="string" indexed="false" stored="true" />
<field name="item_category_name" type="string" indexed="true" stored="true" />
<field name="item_desc" type="text_ik" indexed="true" stored="false" />
             */
            //创建solr输入流文档
            SolrInputDocument doc=new SolrInputDocument();
            doc.setField("id",tbItem.getId());
            doc.setField("item_title",tbItem.getTitle());
            doc.setField("item_sell_point",tbItem.getSellPoint());;
            doc.setField("item_price",tbItem.getPrice());
            doc.setField("item_image",tbItem.getImage());
            doc.setField("item_category_name",tbItemCat.getName());
            doc.setField("item_desc",itemDesc.getItemDesc());
            doc.setField("item_updated",tbItem.getUpdated());
            //向solrClient对象加入doc流对象
            solrClient.add(doc);
        }
        //提交
        solrClient.commit();
    }

    /*
    根据条件显示数据
    一、solr查询
    1.创建可视化寂寞中左侧条件对象 SolrQuery
    2.根据需求向SolrQuery中设置条件
    二、查询
    1.创建查询对象SolrClient.query(SolrQuery) (相当图形化界面中的查询按钮）
    2.得到查询结果 QueryResponse 中包括结果Json数据
    三、操作查询结果
     */
    @Override
    public Map<String, Object> selByQuery(String query, int page, int rows) throws IOException, SolrServerException {
        SolrQuery params=new SolrQuery();//可视化界面左侧的条件
        //设置分页条件
        params.setStart(rows*(page-1));
        params.setRows(rows);
        //添加排序
        params.setSort("item_updated", SolrQuery.ORDER.desc);
        //设置条件
        params.setQuery("item_keywords:"+query);
        //启用高光
        params.setHighlight(true);
        //设置高亮列
        params.addHighlightField("item_title");
        params.setHighlightSimplePre("<span style='color:red'>");
        params.setHighlightSimplePost("</span");
        //相当与点击查询按钮，本质，向solr web服务器发送请求并接受响应， query对象包含了返回的Json格式
        QueryResponse response=solrClient.query(params);
        List<TbItemChild> listChild=new ArrayList<>();
        //返回的是Json数据中docs[] 数组对象
        SolrDocumentList listSolr=response.getResults();
        //高亮内容
        Map<String, Map<String,List<String>>> hh=response.getHighlighting();
        for(SolrDocument doc:listSolr){
            TbItemChild child=new TbItemChild();
            child.setId(Long.parseLong(doc.getFieldValue("id").toString()));
            List<String> list=hh.get(doc.getFieldValue("id")).get("item_title");
            if(list!=null&&list.size()>0){
                child.setTitle(list.get(0));
            }else {
                child.setTitle(doc.getFieldValue("item_title").toString());
            }
            child.setPrice((Long)doc.getFieldValue("item_price"));
            Object image=doc.getFieldValue("item_image");
            child.setImages(image==null||image.equals("")?new String[1]:image.toString().split(","));
            child.setSellPoint(doc.getFieldValue("item_sell_point").toString());
            listChild.add(child);
        }
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("itemList",listChild);
        resultMap.put("totalPages",listSolr.getNumFound()%rows==0?listSolr.getNumFound()/rows:listSolr.getNumFound()/rows+1);

        return resultMap;
    }

    /*
    solr 新增
    1.创建doc对象
    2.向doc中Field设置
    3.向solrClient add（doc）对象
    4.提交solrClient
     */
    @Override
    public int add(Map<String, Object> map, String desc) throws IOException, SolrServerException {
        SolrInputDocument doc=new SolrInputDocument();
        doc.setField("id",map.get("id"));;
        doc.setField("item_title",map.get("title"));
        doc.setField("item_sell_point",map.get("sellPoint"));
        doc.setField("item_price",map.get("price"));
        doc.setField("item_image",map.get("image"));
        doc.setField("item_category_name",tbItemCatDubboServiceImpl.selById((Integer)map.get("cid")).getName());
        doc.setField("item_desc",desc);
        UpdateResponse response=solrClient.add(doc);
        solrClient.commit();
        if(response.getStatus()==0){
            return 1;
        }
        return 0;
    }
}
