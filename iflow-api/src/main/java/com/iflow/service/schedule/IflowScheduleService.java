package com.iflow.service.schedule;


import com.iflow.entity.IflowComponent;
import com.iflow.entity.IflowInstance;
import com.iflow.json.IflowParam;

/**
 * 流程调度器
 */
public interface IflowScheduleService {


    /**
     * 开始流程
     *
     * @param startParam 业务单据参数
     * @throws Exception
     */
    void start(IflowParam startParam) throws Exception;


    /**
     * 执行流程
     * 所有流程节点都要执行这个方法
     *
     * @param instance 流程实例
     * @param component 流程节点
     * @throws Exception
     */
    void process(IflowInstance instance, IflowComponent component) throws Exception;



}
