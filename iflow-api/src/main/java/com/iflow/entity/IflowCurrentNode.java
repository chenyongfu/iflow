package com.iflow.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "iflow_current_node")
public class IflowCurrentNode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流程实例ID
     */
    @Id
    @Column(name = "relation_id")
    @GeneratedValue(generator = "system-uuid")
    private String relationId;
    
    /**
     * 流程实例
     */
    @Column(name = "instance_id")
    private String instanceId;
    
    /**
     * 流程处在的组件ID
     */
    @Column(name = "component_id")
    private String componentId;
    
    /**
     *  创建时间
     */
    @Column(name = "created_date")
    private Date createdDate;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
