package com.iflow.service.impl.processor;

import com.iflow.entity.IflowComponent;
import com.iflow.entity.IflowCurrentNode;
import com.iflow.entity.IflowInstance;
import com.iflow.entity.IflowInstanceParam;
import com.iflow.exception.IflowException;
import com.iflow.service.node.IflowComponentService;
import com.iflow.service.node.IflowCurrentNodeService;
import com.iflow.service.node.IflowInstanceParamService;
import com.iflow.service.processor.IflowAction;
import com.iflow.service.processor.IflowProcessor;
import com.iflow.service.schedule.IflowScheduleService;
import com.iflow.util.IflowConstant;
import com.iflow.util.SpringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;


/**
 * 动作节点处理器
 */
@Service
public class IflowNodeProcessor implements IflowProcessor {
    
    @Autowired
    private IflowComponentService componentService;
    
    @Autowired
    private IflowCurrentNodeService currentNodeService;
    
    @Autowired
    private IflowInstanceParamService instanceParamService;
    
    
    
    /**
     * 动作节点处理器
     *  执行动作节点bean，继续执行流程
     */
    @Override
    public void process(IflowScheduleService scheduleService, IflowInstance instance, IflowComponent current) throws Exception {
        
        String type = current.getComponentType();
        if(!IflowConstant.COMPONENT_TYPE_ACTION.equals(type)){
            throw new IflowException("该节点不是动作节点");
        }
        
        String beanId = current.getActionBean();
        if(StringUtils.isBlank(beanId)){
            return;
        }
        
        // 获取参数
        IflowInstanceParam instanceParam = instanceParamService.findByInstanceId(instance.getInstanceId());
        Map<String, Object> formMap = instanceParam.getFormMap();
        
        // 获取actionBean
        IflowAction action = (IflowAction) SpringUtil.getBean(beanId);
        action.execute(instance, formMap);
        
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
