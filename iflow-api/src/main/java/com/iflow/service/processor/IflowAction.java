package com.iflow.service.processor;

import com.iflow.entity.IflowInstance;

import java.util.Map;


/**
 * 自定义动作
 */
public interface IflowAction {

    
    /**
     * 执行用户自定义方法
     * @param instance 当前流程实例
     * @param formMap 启动流程时保存的变量Map
     */
    void execute(IflowInstance instance, Map<String, Object> formMap);
    
    
}
