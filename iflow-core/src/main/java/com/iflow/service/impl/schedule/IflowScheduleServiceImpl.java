package com.iflow.service.impl.schedule;


import com.iflow.dao.DaoService;
import com.iflow.entity.*;
import com.iflow.exception.IflowException;
import com.iflow.json.IflowParam;
import com.iflow.service.impl.processor.IflowProcessorFactory;
import com.iflow.service.node.IflowComponentService;
import com.iflow.service.node.IflowTemplateService;
import com.iflow.service.processor.IflowProcessor;
import com.iflow.service.schedule.IflowScheduleService;
import com.iflow.util.IflowConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;


/**
 * 流程调度器
 */
@Service
public class IflowScheduleServiceImpl implements IflowScheduleService {

    @Resource(name = "iflowDao")
    private DaoService iflowDao;

    @Autowired
    private IflowTemplateService templateService;

    @Autowired
    private IflowComponentService componentService;




    /**
     * 开始流程
     *
     * @param startParam 业务单据参数
     * @throws Exception
     */
    @Override
    public void start(IflowParam startParam) throws Exception {
        if(startParam == null){
            throw new IflowException("启动参数不能为空");
        }
        if(StringUtils.isBlank(startParam.getInstanceName())){
            throw new IflowException("启动参数: instanceName不能为空");
        }
        if(StringUtils.isBlank(startParam.getBusinessId())){
            throw new IflowException("启动参数: businessId不能为空");
        }

        // 查询流程模板
        String templateId = startParam.getTemplateId();
        IflowTemplate template = templateService.findById(templateId);
        if(template == null){
            throw new Exception("未找到templateId="+ templateId +"对应的流程模板");
        }

        // 设置当前节点,保存流程实例
        IflowInstance instance = new IflowInstance();
        instance.setTemplateId(templateId);
        instance.setBusinessId(startParam.getBusinessId());
        instance.setInstanceName(startParam.getInstanceName());
        instance.setState(IflowConstant.INSTANCE_STATE_IN_PROCESS);
        instance.setCreatedDate(new Date());
        iflowDao.saveOrUpdate(instance);

        // 开始节点
        IflowComponent start = componentService.findStartByTemplateId(templateId);
        IflowCurrentNode cn = new IflowCurrentNode();
        cn.setComponentId(start.getComponentId());
        cn.setInstanceId(instance.getInstanceId());
        cn.setCreatedDate(new Date());
        iflowDao.saveOrUpdate(cn);

        // 保存流程实例参数
        IflowInstanceParam iparams = new IflowInstanceParam();
        iparams.setInstanceId(instance.getInstanceId());
        iparams.setCreatedDate(new Date());
        iparams.setFormMap(startParam.getFormMap());
        iflowDao.saveOrUpdate(iparams);

        // 开始执行流程
        this.process(instance, start);
    }


    /**
     * 执行流程
     * 所有流程节点都要执行这个方法
     *
     * @param instance  流程实例
     * @param component 流程节点
     * @throws Exception
     */
    @Override
    public void process(IflowInstance instance, IflowComponent component) throws Exception {
        if(instance == null){
            throw new IflowException("审核记录中的流程实例不能为空");
        }
        if(component == null){
            throw new IflowException("审核记录中的当前节点不能为空");
        }

        try {
            // 获取处理器
            IflowProcessorFactory factory = IflowProcessorFactory.getInstance();
            IflowProcessor processor = factory.getProcessor(component);
            processor.process(this, instance, component);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
