package com.iflow.service.impl.processor;

import com.iflow.entity.IflowComponent;
import com.iflow.entity.IflowCurrentNode;
import com.iflow.entity.IflowInstance;
import com.iflow.exception.IflowException;
import com.iflow.service.node.IflowComponentService;
import com.iflow.service.node.IflowCurrentNodeService;
import com.iflow.service.processor.IflowProcessor;
import com.iflow.service.schedule.IflowScheduleService;
import com.iflow.util.IflowConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * 汇合节点处理器
 */
@Service
public class IflowJoinRoundProcessor implements IflowProcessor {
    
    @Autowired
    private IflowComponentService componentService;
    
    @Autowired
    private IflowCurrentNodeService currentNodeService;
    
    
    
    
    /**
     * 聚合节点处理器
     *  1 保存当前已通过会签数目
     *  2 判断当前节点是否达到执行下一步的条件，达到则继续执行流程
     */
    @Override
    public void process(IflowScheduleService scheduleService, IflowInstance instance, IflowComponent current) throws Exception {
        
        String type = current.getComponentType();
        if(!IflowConstant.COMPONENT_TYPE_JOIN.equals(type)){
            throw new IflowException("该节点不是聚合节点");
        }
        
        // 查询对应的分支节点
        String lastFork = current.getLastFork();
        if(StringUtils.isBlank(lastFork)){
            throw new IflowException("该聚合节点对应的分支节点lastFork不能为空");
        }
        int index = lastFork.lastIndexOf("-");
        index = index == -1 ? 0 : index + 1;
        String forkId = lastFork.substring(index, lastFork.length());
        IflowComponent fork = componentService.findById(forkId);
        if(fork == null){
            throw new IflowException("该聚合节点对应的分支节点未找到");
        }
        
        // 当前聚合节点会签通过数 +1
        int finishCount = current.getFinishCount() == null ? 1 : current.getFinishCount() + 1;
        current.setFinishCount(finishCount);
        componentService.saveIflowComponent(current);
        
        // 会签需要达到的数目
        int signWay = fork.getSignWay();
        int matchCount = fork.getMatchCount();
        if(signWay == IflowConstant.SIGN_WAY_BY_COUNT){
            matchCount = fork.getNeedSignCount();
        }
        
        // 判断已通过数目与分支定义的数目是否相等
        if(finishCount == matchCount){
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

}
