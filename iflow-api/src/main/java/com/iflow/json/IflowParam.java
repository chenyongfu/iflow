package com.iflow.json;

import java.io.Serializable;
import java.util.Map;


/**
 * 流程节点运行时的参数
 */
public class IflowParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流程实例名称
     */
    private String instanceName;
    
    /**
     * 流程模板ID
     */
    private String templateId;
    
    /**
     * 业务ID
     */
    private String businessId;
    
    /**
     * 表单信息，只需要模板可能用到的参数
     * formMap里面如果包含：
     *      SUCCESS_NOTIFY_EMAIL: 流程处理完成之后，会给这个邮箱发通知
     */
    private Map<String, Object> formMap;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public Map<String, Object> getFormMap() {
        return formMap;
    }

    public void setFormMap(Map<String, Object> formMap) {
        this.formMap = formMap;
    }
}
