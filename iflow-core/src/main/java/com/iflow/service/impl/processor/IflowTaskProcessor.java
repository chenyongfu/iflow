package com.iflow.service.impl.processor;

import com.iflow.entity.IflowComponent;
import com.iflow.entity.IflowInstance;
import com.iflow.entity.IflowInstanceParam;
import com.iflow.exception.IflowException;
import com.iflow.json.HandlerInfo;
import com.iflow.service.node.IflowComponentService;
import com.iflow.service.node.IflowInstanceParamService;
import com.iflow.service.processor.IflowHandler;
import com.iflow.service.processor.IflowProcessor;
import com.iflow.service.schedule.IflowScheduleService;
import com.iflow.util.IflowConstant;
import com.iflow.util.SpringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * 任务节点处理器
 */
@Service
public class IflowTaskProcessor implements IflowProcessor {
    
    @Autowired
    private IflowComponentService componentService;
    
    @Autowired
    private IflowInstanceParamService instanceParamService;
    
    
    
    /**
     * 任务节点处理器
     *  通知相关人处理
     */
    @Override
    public void process(IflowScheduleService scheduleService, IflowInstance instance, IflowComponent current) throws Exception {

        String type = current.getComponentType();
        if(!IflowConstant.COMPONENT_TYPE_TASK.equals(type)){
            throw new IflowException("该节点不是任务节点");
        }
        
        Integer whodoType = current.getWhodoWay();
        if(whodoType == null){
            return;
        }
        
        // 解析处理人，发送流程提醒邮件
        String email = current.getEmail();;
        if(current.getWhodoWay() == IflowConstant.WHODO_WAY_BY_WHODO_BEAN){
            // 解析配置的bean, 执行它得到处理人与邮箱
            String beanId = current.getWhodoBean();
            if(StringUtils.isBlank(beanId)){
                throw new IflowException("执行出错：节点: "+ current.getComponentId() +" 配置的beanId不能为空");
            }
            IflowHandler handlerInterface = (IflowHandler) SpringUtil.getBean(beanId);
            IflowInstanceParam instanceParam = instanceParamService.findByInstanceId(instance.getInstanceId());
            Map<String, Object> formMap = instanceParam.getFormMap();
            HandlerInfo handler = handlerInterface.getHandler(instance, formMap);
            email = handler.getEmail();
            String whodoId = handler.getId();
            if(StringUtils.isBlank(whodoId)){
                throw new IflowException("执行节点配置的bean："+ beanId +", 未获取到处理人ID");
            }
            
            // 保存处理人与邮件
            current.setWhodoBeanResolve(whodoId);
            current.setEmail(email);
            componentService.saveIflowComponent(current);
        }
        if(StringUtils.isNotBlank(email)){
//            // 从缓存读邮件服务器配置
//            String host = McfCache.getValue("SYSTEM_EMAIL_CONFIG_HOST") +"";
//            Object portObj = McfCache.getValue("SYSTEM_EMAIL_CONFIG_PORT");
//            int port = portObj == null ? 25 : Integer.parseInt(portObj.toString());
//            String user = McfCache.getValue("SYSTEM_EMAIL_CONFIG_USERNAME") +"";
//            String pswd = McfCache.getValue("SYSTEM_EMAIL_CONFIG_PASSWORD") +"";
//            // 邮件内容
//            String subject = "您有流程需要处理-"+ instance.getInstanceName();
//            String content = "尊敬的用户：</br>"+ instance.getInstanceName() +"需要您处理, 请尽快登录系统处理，谢谢</br></br>该邮件提醒来自流程系统，请勿回复";
//            IflowUtils.sendMail(host, port, user, pswd, email, subject, content, null);
        }
    }

}
