package com.iflow.service.node;


import com.iflow.entity.IflowApproveRecord;
import com.iflow.entity.IflowInstance;

public interface IflowInstanceService {


    /**
     * 任务节点审批
     *  只做2件事，保存意见 >> 告诉流程自动更新
     * @param record 审核意见
     * @return
     */
    Boolean approve(IflowApproveRecord record) throws Exception;


    /**
     * 保存流程实例
     * @param instance
     */
    IflowInstance saveIflowInstance(IflowInstance instance) throws Exception;


    /**
     * 根据ID，查询流程ID
     * @param id
     * @return
     * @throws Exception
     */
    IflowInstance findById(String id) throws Exception;

}
