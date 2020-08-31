package com.iflow.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "iflow_template")
public class IflowTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "template_id")
    @GeneratedValue(generator = "system-uuid")
    private String templateId;
    
    /**
     * 模板编码
     */
    @Column(name = "template_code")
    private String templateCode;
    
    /**
     * 模板名称
     */
    @Column(name = "template_name")
    private String templateName;
    
    /**
     * 状态  1： 就绪； 2： 运行中；
     */
    @Column(name = "state")
    private Integer state;
    
    /**
     * 该模板的业务表单显示地址
     * 运行时会传入businessId,这个ID是在启动流程的时候传入的
     */
    @Column(name = "business_form_url")
    private String businessFormUrl;
    
    /**
     * 说明
     */
    @Column(name = "remark")
    private String remark;
    
    /**
     *  创建时间
     */
    @Column(name = "created_date")
    private Date createdDate;

    /**
     * 构造函数
     */
    public IflowTemplate() {
        // null
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getBusinessFormUrl() {
        return businessFormUrl;
    }

    public void setBusinessFormUrl(String businessFormUrl) {
        this.businessFormUrl = businessFormUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
