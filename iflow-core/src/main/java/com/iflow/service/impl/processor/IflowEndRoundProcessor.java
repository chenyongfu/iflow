package com.iflow.service.impl.processor;

import com.iflow.entity.IflowApproveRecord;
import com.iflow.entity.IflowComponent;
import com.iflow.entity.IflowInstance;
import com.iflow.exception.IflowException;
import com.iflow.service.node.IflowApproveRecordService;
import com.iflow.service.node.IflowInstanceParamService;
import com.iflow.service.node.IflowInstanceService;
import com.iflow.service.processor.IflowProcessor;
import com.iflow.service.schedule.IflowScheduleService;
import com.iflow.util.IflowConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * 介素节点处理器
 */
@Component
public class IflowEndRoundProcessor implements IflowProcessor {
    
    @Autowired
    private IflowInstanceService instanceService;
    
    @Autowired
    private IflowApproveRecordService approveRecordService;
    
    @Autowired
    private IflowInstanceParamService instanceParamService;
    
    
    
    /**
     * 结束节点处理器
     *  自动通过结束节点，设置流程实例为已通过，通知表单的发起人
     */
    @Override
    public void process(IflowScheduleService scheduleService, IflowInstance instance, IflowComponent current) throws Exception {

        String type = current.getComponentType();
        if(!IflowConstant.COMPONENT_TYPE_END.equals(type)){
            throw new IflowException("该节点不是结束节点");
        }
        
        // 设置结束节点审核自动通过
        IflowApproveRecord record = new IflowApproveRecord();
        record.setCreatedDate(new Date());
        record.setInstanceId(instance.getInstanceId());
        record.setComponentId(current.getComponentId());
        record.setState(IflowConstant.APPROVE_STATE_AUTO);
        approveRecordService.saveIflowApproveRecord(record);
        
        // 设置流程状态为已通过
        instance.setState(IflowConstant.INSTANCE_STATE_COMPLETE);
        instance.setLastUpdatedDate(new Date());
        instanceService.saveIflowInstance(instance);
        
//        // 通知表单的发起人
//        IflowInstanceParam instanceParam = instanceParamService.findIflowInstanceParamByInstanceId(instance.getInstanceId());
//        Map<String, Object> formMap = instanceParam.getFormMap();
//        if(formMap == null || formMap.isEmpty()){
//            return;
//        }
//
//        String email = formMap.get("SUCCESS_NOTIFY_EMAIL") +"";
//        if(StringUtils.isNotBlank(email)){
//            // 从缓存读邮件服务器配置
//            String host = McfCache.getValue("SYSTEM_EMAIL_CONFIG_HOST") +"";
//            Object portObj = McfCache.getValue("SYSTEM_EMAIL_CONFIG_PORT");
//            int port = portObj == null ? 25 : Integer.parseInt(portObj.toString());
//            String user = McfCache.getValue("SYSTEM_EMAIL_CONFIG_USERNAME") +"";
//            String pswd = McfCache.getValue("SYSTEM_EMAIL_CONFIG_PASSWORD") +"";
//            // 邮件内容
//            String subject = "您的-"+ instance.getInstanceName() +"已处理完成";
//            String content = "尊敬的用户：</br>"+ subject +", 详情请登录系统查看，谢谢</br></br>该邮件提醒来自流程系统，请勿回复";
//            IflowUtils.sendMail(host, port, user, pswd, email, subject, content, null);
//        }

    }



}
