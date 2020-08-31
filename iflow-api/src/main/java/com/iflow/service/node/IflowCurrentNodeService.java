package com.iflow.service.node;


import com.iflow.entity.IflowCurrentNode;

import java.util.List;

/**
 * 流程当前节点服务
 */
public interface IflowCurrentNodeService {


    /**
     * 获取流程当前执行的节点
     * @param instanceId
     * @return
     */
    List<IflowCurrentNode> findByInstanceId(String instanceId) throws Exception;


    /**
     * 更新当前节点
     * @param componentId
     * @param now
     */
    void updateIflowCurrentNode(String componentId, IflowCurrentNode now) throws Exception;

    
}
