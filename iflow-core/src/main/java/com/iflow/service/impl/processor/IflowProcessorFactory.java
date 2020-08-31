package com.iflow.service.impl.processor;


import com.iflow.entity.IflowComponent;
import com.iflow.service.processor.IflowProcessor;
import com.iflow.util.IflowConstant;
import com.iflow.util.SpringUtil;
import org.apache.commons.lang3.StringUtils;


/**
 * 流程处理器工厂
 */
public class IflowProcessorFactory {

    private static IflowProcessorFactory instance;
    
    private IflowProcessorFactory(){
        // null
    }


    
    /**
     * 返回factory实例
     * @return
     */
    public static IflowProcessorFactory getInstance(){
        if(instance == null){
            instance = new IflowProcessorFactory();
        }
        return instance;
    }

    
    /**
     * 根据节点类型，获取对应的处理器
     *  根据IflowComponent-componentType
     *  返回 (iflow + componentType + Processor) 的bean
     * 
     *  componentType里面的" round" 会被替换为 "Round"
     *  因此componentType与IflowProcessor实现类的命名请按照这个规则来，则可以自动根据componentType获取处理器
     *  
     *  比如: 开始节点 componentType = "start round"  >> beanId = iflowStartRoundProcessor
     *       任务        componentType = "task"         >> beanId = iflowTaskProcessor
     * @param component
     * @return
     */
    public IflowProcessor getProcessor(IflowComponent component){
        String type = component.getComponentType();
        if(StringUtils.isBlank(type)){
            return null;
        }
        type = type.replaceAll(" round", "Round");
        String first = type.substring(0, 1).toUpperCase();
        String beanId = IflowConstant.IFLOW_KEY + first + type.substring(1) +"Processor";
        IflowProcessor processor = (IflowProcessor) SpringUtil.getBean(beanId);
        return processor;
    }

    
}
