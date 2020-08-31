package com.iflow.service.node;


import com.iflow.entity.IflowInstanceParam;


/**
 * 流程参数处理服务
 */
public interface IflowInstanceParamService {


    
    /**
     * 根据实例ID，查询实例参数
     * @param instanceId
     * @return
     */
    IflowInstanceParam findByInstanceId(String instanceId) throws Exception;


    /**
     * 保存流程实例参数
     * @param iparams
     */
    IflowInstanceParam saveIflowInstanceParam(IflowInstanceParam iparams) throws Exception;
    
}
