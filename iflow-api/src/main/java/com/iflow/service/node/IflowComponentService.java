package com.iflow.service.node;

import com.iflow.entity.IflowComponent;

import java.util.List;
import java.util.Map;


/**
 * 流程节点服务
 */
public interface IflowComponentService {


    /**
     * 根据ID，查询IflowTemplate
     *
     * @param id
     * @return
     * @throws Exception
     */
    IflowComponent findById(String id) throws Exception;

    /**
     * 根据模板ID，查询所有组件
     * @param templateId
     * @return
     * @throws Exception
     */
    List<IflowComponent> findByTemplateId(String templateId) throws Exception;


    /**
     * 根据模板ID，查询IflowComponent
     * @param templateId
     * @return
     */
    Map<String, Object> findMapByTemplateId(String templateId) throws Exception;


    /**
     * 保存流程模板
     * @param cmp
     */
    IflowComponent saveIflowComponent(IflowComponent cmp) throws Exception;


    /**
     * 删除流程模板
     * @param id
     * @return
     */
    Boolean deleteIflowComponentById(String id) throws Exception;


    /**
     * 保存流程模板
     * @param param
     */
    Boolean saveIflowComponentList(Map<String, Object> param) throws Exception;


    /**
     * 根据模板ID，查询开始节点
     * @param templateId
     * @return
     */
    IflowComponent findStartByTemplateId(String templateId) throws Exception;


    /**
     * 根据模板ID，查询结束节点
     * @param templateId
     * @return
     */
    IflowComponent findEndByTemplateId(String templateId) throws Exception;


    /**
     * 查询给定节点之前的节点
     * @param component
     * @return
     */
    IflowComponent findNext(IflowComponent component) throws Exception;


    /**
     * 获取分支节点上下一级所有的箭头连线
     * @param componentId
     * @return
     */
    List<IflowComponent> findArrowsByFork(IflowComponent component) throws Exception;
    




}
