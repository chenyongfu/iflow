package com.iflow.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "iflow_approve_record")
public class IflowApproveRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "approve_id")
    @GeneratedValue(generator = "system-uuid")
    private String approveId;
    
    /**
     * 流程实例ID
     */
    @Column(name="instance_id")
    private String instanceId;
    
    /**
     * 组件ID
     */
    @Column(name="component_id")
    private String componentId;
    
    /**
     * 审批状态： 1 自动通过 ； 2 通过； 3 驳回；
     */
    @Column(name = "state")
    private Integer state;
    
    /**
     * 审批人ID
     */
    @Column(name = "approve_user")
    private String approveUser;
    
    /**
     * 审批意见
     */
    @Column(name = "opinion")
    private String opinion;
    
    /**
     * 审批时间
     */
    @Column(name = "created_date")
    private Date createdDate;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getApproveId() {
        return approveId;
    }

    public void setApproveId(String approveId) {
        this.approveId = approveId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getApproveUser() {
        return approveUser;
    }

    public void setApproveUser(String approveUser) {
        this.approveUser = approveUser;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
