package com.iflow.service.processor;

import com.iflow.entity.IflowInstance;
import com.iflow.json.HandlerInfo;

import java.util.Map;


public interface IflowHandler {

    
    /**
     * 返回节点的处理人信息 HandlerInfo
     *  HandlerInfo包括2个字段：id, email
     *      ID      ： 该ID是业务系统处理人ID，必填
     *      email   ： 如果返回了email，则会向这个邮箱发提醒邮件
     * 
     * @param instance 当前流程实例
     * @param formMap 启动流程时保存的变量Map
     */
    HandlerInfo getHandler(IflowInstance instance, Map<String, Object> formMap);
    
}
