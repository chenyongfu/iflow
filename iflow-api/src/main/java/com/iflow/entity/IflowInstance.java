package com.iflow.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "iflow_instance")
public class IflowInstance implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * 流程实例ID
     */
    @Id
    @Column(name = "instance_id")
    @GeneratedValue(generator = "system-uuid")
    private String instanceId;
    
    /**
     * 流程模板ID
     */
    @Column(name = "template_id")
    private String templateId;
    
    /**
     * 流程实例名称： （如 XXX请假申请）
     */
    @Column(name = "instance_name")
    private String instanceName;
    
    /**
     * 业务系统-业务ID：工作流表单由用户自定义，但是绑定到这里的必须是唯一的ID
     */
    @Column(name = "business_id")
    private String businessId;
    
    /**
     * 状态 
     *  1：审批中
     *  2：已通过
     *  3：已驳回
     *  4：重新审批
     */
    @Column(name = "state")
    private Integer state;
    
    /**
     * 创建人
     */
    @Column(name = "created_user")
    private String createdUser;
    
    /**
     * 创建时间
     */
    @Column(name = "created_date")
    private Date createdDate;

    /**
     * 修改时间
     */
    @Column(name = "last_updated_date")
    private Date lastUpdatedDate;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

}
