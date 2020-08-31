package com.iflow.service.impl.processor;

import com.iflow.entity.IflowComponent;
import com.iflow.entity.IflowCurrentNode;
import com.iflow.entity.IflowInstance;
import com.iflow.entity.IflowInstanceParam;
import com.iflow.exception.IflowException;
import com.iflow.service.node.IflowComponentService;
import com.iflow.service.node.IflowCurrentNodeService;
import com.iflow.service.node.IflowInstanceParamService;
import com.iflow.service.processor.IflowProcessor;
import com.iflow.service.schedule.IflowScheduleService;
import com.iflow.util.IflowConstant;
import com.iflow.util.ScriptEngineUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 分支节点处理器
 */
@Service
public class IflowForkRoundProcessor implements IflowProcessor {
    
    @Autowired
    private IflowComponentService componentService;
    
    @Autowired
    private IflowInstanceParamService instanceParamService;
    
    @Autowired
    private IflowCurrentNodeService currentNodeService;
    
    
    
    
    /**
     * 分支节点处理器
     *  根据会签设置，设置下一批执行的节点，继续执行流程
     */
    @Override
    public void process(IflowScheduleService scheduleService, IflowInstance instance, IflowComponent current) throws Exception {
        
        String type = current.getComponentType();
        if(!IflowConstant.COMPONENT_TYPE_FORK.equals(type)){
            throw new IflowException("该节点不是分支节点");
        }
        
        // 获取会签方式: 1 按条件选择； 2 全部通过；3 指定数目；
        Integer signWay = current.getSignWay();
        if(signWay == null){
            return;
        }
        
        // 即将更新的当前节点
        List<IflowCurrentNode> currentNodes = new ArrayList<IflowCurrentNode>();
        // 获取该分支上所有的连线
        List<IflowComponent> arrows = componentService.findArrowsByFork(current);
        // 判断分支方式
        Date now = new Date();
        int matchCount = 0;
        if(signWay == IflowConstant.SIGN_WAY_BY_EXPRESSION){
            // 1 按条件选择，符合条件的作为下一步节点
            IflowInstanceParam instanceParam = instanceParamService.findByInstanceId(instance.getInstanceId());
            Map<String, Object> formMap = instanceParam.getFormMap();
            for(IflowComponent arrow : arrows){
                if(arrow == null || StringUtils.isBlank(arrow.getTo())){
                    continue;
                }
                String compId = null;
                IflowCurrentNode cn = null;
                IflowComponent cmp = null;
                Integer arrowType = arrow.getArrowType();
                if(arrowType == IflowConstant.ARROW_TYPE_BY_NONE){
                    // 无条件通过
                    compId = arrow.getTo();
                    cn = new IflowCurrentNode();
                    cn.setInstanceId(instance.getInstanceId());
                    cn.setComponentId(compId);
                    cn.setCreatedDate(now);
                    
                    currentNodes.add(cn);
                    matchCount ++;
                } else if(arrowType == IflowConstant.ARROW_TYPE_BY_EXPRESSION){
                    // 替换条件表达式
                    String script = arrow.getArrowExpression();
                    if(StringUtils.isBlank(script)){
                        continue;
                    }
                    String temp = script;
                    int index = temp.indexOf("${");
                    while(index >= 0){
                        temp = temp.substring(index);
                        int end = temp.indexOf("}") + 1;
                        // 目标
                        String el = temp.substring(0, end);
                        String key = temp.substring(2, end - 1).trim();
                        Object value = formMap.get(key);
                        script = script.replaceAll(el, value.toString());
                        // 继续查找
                        temp = temp.substring(end);
                        index = temp.indexOf("${");
                    }
                    // 执行脚本
                    boolean match = (Boolean) ScriptEngineUtil.eval(script);
                    if(match){
                        compId = arrow.getTo();
                        cn = new IflowCurrentNode();
                        cn.setInstanceId(instance.getInstanceId());
                        cn.setComponentId(compId);
                        cn.setCreatedDate(now);
                        
                        currentNodes.add(cn);
                        matchCount ++;
                    }
                }
            }
        }else{
            // 2 全部通过；3 指定数目；所有箭头指向的节点都作为下一节点
            for(IflowComponent arrow : arrows){
                if(arrow == null || StringUtils.isBlank(arrow.getTo())){
                    continue;
                }
                String compId = arrow.getTo();
                IflowCurrentNode cn = new IflowCurrentNode();
                cn.setInstanceId(instance.getInstanceId());
                cn.setComponentId(compId);
                cn.setCreatedDate(now);
                
                currentNodes.add(cn);
                matchCount ++;
            }
        }
        // 保存匹配的总数
        current.setMatchCount(matchCount);
        componentService.saveIflowComponent(current);
        
        // 设置下一批要执行的节点为流程当前节点
        for(IflowCurrentNode item : currentNodes){
            currentNodeService.updateIflowCurrentNode(current.getComponentId(), item);
        }
        
        // 继续执行流程
        for(IflowCurrentNode item : currentNodes){
            if(item != null && item.getComponentId() != null){
                IflowComponent component = componentService.findById(item.getComponentId());
                scheduleService.process(instance, component);
            }
        }
    }

}
