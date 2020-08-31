package com.iflow.service.node;


import com.iflow.entity.IflowTemplate;
import com.iflow.json.Page;

import java.util.Map;


/**
 * 模板操作服务器
 *
 * 对外暴露
 *
 */
public interface IflowTemplateService {


    
    /**
     * 根据ID，查询IflowTemplate
     * @return
     */
    IflowTemplate findById(String id) throws Exception;


    /**
     * 分页查询IflowTemplate
     * 
     * @param param
     * @return
     */
    Page findByPage(Page page, Map<String, Object> param) throws Exception;

    
    /**
     * 保存流程模板
     * @param template
     */
    IflowTemplate saveIflowTemplate(IflowTemplate template) throws Exception;


    /**
     * 删除流程模板
     * @param id
     * @return
     */
    Boolean deleteById(String id) throws Exception;


    /**
     * 校验流程模板CODE 唯一性
     * 唯一返回true
     * @return
     */
    boolean uniqueValidate(String code) throws Exception;


}
