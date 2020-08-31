package com.iflow.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;


@Entity
@Table(name = "iflow_instance_param")
public class IflowInstanceParam implements Serializable {

    private static final long serialVersionUID = 1L;

    
    @Id
    @Column(name = "param_id")
    @GeneratedValue(generator = "system-uuid")
    private String paramId;
    
    /**
     * 流程实例
     */
    @Column(name = "instance_id")
    private String instanceId;

    /**
     * 参数byte[]，操作的时候可以反向生成java对象
     */
    @Lob
    @Column(name = "param_byte", columnDefinition = "BLOB",nullable=true)
    private byte[] paramByte;
    
    /**
     * 创建时间
     */
    @Column(name = "created_date")
    private Date createdDate;
    
    /**
     * paramByte转换的Map
     */
    @Transient
    private Map<String, Object> formMap;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getParamId() {
        return paramId;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public byte[] getParamByte() {
        return paramByte;
    }

    public void setParamByte(byte[] paramByte) {
        this.paramByte = paramByte;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Map<String, Object> getFormMap() {
        return formMap;
    }

    public void setFormMap(Map<String, Object> formMap) {
        this.formMap = formMap;
    }
}
