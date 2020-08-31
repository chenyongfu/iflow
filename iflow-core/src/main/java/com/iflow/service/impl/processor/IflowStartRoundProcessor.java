package com.iflow.service.impl.processor;

import com.iflow.entity.IflowApproveRecord;
import com.iflow.entity.IflowComponent;
import com.iflow.entity.IflowCurrentNode;
import com.iflow.entity.IflowInstance;
import com.iflow.exception.IflowException;
import com.iflow.service.node.IflowApproveRecordService;
import com.iflow.service.node.IflowComponentService;
import com.iflow.service.node.IflowCurrentNodeService;
import com.iflow.service.processor.IflowProcessor;
import com.iflow.service.schedule.IflowScheduleService;
import com.iflow.util.IflowConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * 开始节点处理器
 */
@Service
public class IflowStartRoundProcessor implements IflowProcessor {

    @Autowired
    private IflowApproveRecordService approveRecordService;
    
    @Autowired
    private IflowComponentService componentService;
    
    @Autowired
    private IflowCurrentNodeService currentNodeService;
    
    
    
    /**
     * 开始节点处理器
     *  自动通过开始节点，设置下一个执行的节点
     */
    @Override
    public void process(IflowScheduleService scheduleService, IflowInstance instance, IflowComponent current) throws Exception {
        
        String type = current.getComponentType();
        if(!IflowConstant.COMPONENT_TYPE_START.equals(type)){
            throw new IflowException("该节点不是开始节点");
        }
        
        // 设置开始节点审核自动通过
        IflowApproveRecord record = new IflowApproveRecord();
        record.setCreatedDate(new Date());
        record.setInstanceId(instance.getInstanceId());
        record.setComponentId(current.getComponentId());
        record.setState(IflowConstant.APPROVE_STATE_AUTO);
        approveRecordService.saveIflowApproveRecord(record);
        
        // 设置下一批要执行的节点为流程当前节点
        IflowComponent next = componentService.findNext(current);
        if(next == null){
            return;
        }
        IflowCurrentNode cn = new IflowCurrentNode();
        cn.setComponentId(next.getComponentId());
        cn.setInstanceId(instance.getInstanceId());
        cn.setCreatedDate(new Date());
        currentNodeService.updateIflowCurrentNode(current.getComponentId(), cn);
        
        // 继续执行流程
        scheduleService.process(instance, next);
    }


}
