package com.iflow.service.node;


import com.iflow.entity.IflowApproveRecord;

/**
 * 流程记录审核服务
 */
public interface IflowApproveRecordService {


    /**
     * 根据ID，查询审核记录
     * @return
     */
    IflowApproveRecord findById(String id) throws Exception;


    /**
     * 保存审核记录
     * @param record
     */
    IflowApproveRecord saveIflowApproveRecord(IflowApproveRecord record) throws Exception;


}
