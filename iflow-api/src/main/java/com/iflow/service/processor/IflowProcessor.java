package com.iflow.service.processor;

import com.iflow.entity.IflowComponent;
import com.iflow.entity.IflowInstance;
import com.iflow.service.schedule.IflowScheduleService;

/**
 * 处理器接口
 * @ClassName: IflowProcessor
 * @Description: 
 * @author chenyf
 * @date 2015年11月20日 下午8:31:25
 */
public interface IflowProcessor {


    /**
     * 流程执行接口
     *
     * @param scheduleService 调度器
     * @param instance 流程实例
     * @param current 当前节点
     * @throws Exception
     */
    void process(IflowScheduleService scheduleService, IflowInstance instance, IflowComponent current) throws Exception;
    
    
    
    
}
