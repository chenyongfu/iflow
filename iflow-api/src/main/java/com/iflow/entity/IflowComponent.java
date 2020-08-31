package com.iflow.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "iflow_component")
public class IflowComponent implements Serializable {

    private static final long serialVersionUID = 1L;

    
    @Id
    @Column(name = "component_id")
    @GeneratedValue(generator = "system-uuid")
    private String componentId;
    
    /**
     * 流程模板ID
     */
    @ManyToOne
    @JoinColumn(name="template_id")
    private Integer templateId;

    /**
     * 组件名称
     */
    @Column(name = "component_name")
    private String componentName;
    
    /**
     * 组件类型： 
     *  start round : 开始； 
     *  end round   : 结束； 
     *  ntask       : 人工任务； 
     *  node        : 处理动作；  
     *  fork round  : 分支； 
     *  join round  : 聚合；  
     *  sl          : 箭头；
     */
    @Column(name = "component_type")
    private String componentType;
    
    /**
     * 处理人方式： 
     * 1 指定处理人ID； 
     * 2 指定bean,运行该方法返回处理人；
     */
    @Column(name = "whodo_way")
    private Integer whodoWay;
    
    /**
     * 处理人ID
     */
    @Column(name = "whodo_id")
    private String whodoId;
    
    /**
     * 执行bean方法（返回处理人ID）
     */
    @Column(name = "whodo_bean")
    private String whodoBean;
    
    /**
     * 执行whodoBean，得到的处理人ID
     */
    @Column(name = "whodo_bean_resolve")
    private String whodoBeanResolve;
    
    /**
     * 页面填写的 处理人email
     * 或者 
     * 执行whodoBean，得到的处理人email
     */
    @Column(name = "email")
    private String email;
    
    /**
     * 动作节点beanId
     */
    @Column(name = "action_bean")
    private String actionBean;
    
    /**
     * 连接类型： 
     * 1 无条件； 
     * 2 条件表达式；
     */
    @Column(name = "arrow_type")
    private Integer arrowType;
    
    /**
     * 条件表达式内容
     */
    @Column(name = "arrow_expression")
    private String arrowExpression;
    
    /**
     * 分支节点-会签方式: 1 按条件选择； 2 全部通过；3 指定数目；
     */
    @Column(name = "sign_way")
    private Integer signWay;
    
    /**
     * 分支节点-根据会签方式 1,2 : 符合的条件个数
     */
    @Column(name = "match_count")
    private Integer matchCount;
    
    /**
     * 分支节点-根据会签方式 3 ，指定数目，达到该数目才走下一步流程
     */
    @Column(name = "need_sign_count")
    private Integer needSignCount;
    
    /**
     * 聚合节点: 已完成的分支节点数
     */
    @Column(name = "finish_count")
    private Integer finishCount;
    
    /**
     * 左偏移
     */
    @Column(name = "offset_left")
    private Integer left;
    
    /**
     * 上偏移
     */
    @Column(name = "offset_top")
    private Integer top;
    
    /**
     * 宽度
     */
    @Column(name = "width")
    private Integer width;
    
    /**
     * 高度
     */
    @Column(name = "height")
    private Integer height;
    
    /**
     * 箭头的起始节点
     */
    @Column(name = "from_node")
    private String fromNode;
    
    /**
     * 箭头指向的节点ID
     */
    @Column(name = "to_node")
    private String toNode;

    /**
     * 箭头指向的节点ID
     */
    @Column(name = "m_position")
    private Integer m;
    
    /**
     * 箭头指向的节点ID
     */
    @Column(name = "marked")
    private boolean marked;
    
    /**
     * 分支路径
     * 记录分支的路径，
     * 遇分支则追加“-路径”，遇会合则从后面删除回来，这样根据路径能看出节点在哪一个分支的范围内
     */
    @Column(name = "fork_path")
    private String forkPath;
    
    /**
     * 上一个分支路径
     *    = 聚合节点 对应的分支节点的 forkPath
     */
    @Column(name = "last_fork")
    private String lastFork;
    
    /**
     * 关系ID，保存之前，页面暂时生成的ID，关联上下级
     */
    @Column(name = "relation_id")
    private String relationId;
    
    @Transient
    private String name;
    
    @Transient
    private String type;
    
    @Transient
    private String from;
    
    @Transient
    private String to;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public Integer getWhodoWay() {
        return whodoWay;
    }

    public void setWhodoWay(Integer whodoWay) {
        this.whodoWay = whodoWay;
    }

    public String getWhodoId() {
        return whodoId;
    }

    public void setWhodoId(String whodoId) {
        this.whodoId = whodoId;
    }

    public String getWhodoBean() {
        return whodoBean;
    }

    public void setWhodoBean(String whodoBean) {
        this.whodoBean = whodoBean;
    }

    public String getWhodoBeanResolve() {
        return whodoBeanResolve;
    }

    public void setWhodoBeanResolve(String whodoBeanResolve) {
        this.whodoBeanResolve = whodoBeanResolve;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getActionBean() {
        return actionBean;
    }

    public void setActionBean(String actionBean) {
        this.actionBean = actionBean;
    }

    public Integer getArrowType() {
        return arrowType;
    }

    public void setArrowType(Integer arrowType) {
        this.arrowType = arrowType;
    }

    public String getArrowExpression() {
        return arrowExpression;
    }

    public void setArrowExpression(String arrowExpression) {
        this.arrowExpression = arrowExpression;
    }

    public Integer getSignWay() {
        return signWay;
    }

    public void setSignWay(Integer signWay) {
        this.signWay = signWay;
    }

    public Integer getMatchCount() {
        return matchCount;
    }

    public void setMatchCount(Integer matchCount) {
        this.matchCount = matchCount;
    }

    public Integer getNeedSignCount() {
        return needSignCount;
    }

    public void setNeedSignCount(Integer needSignCount) {
        this.needSignCount = needSignCount;
    }

    public Integer getFinishCount() {
        return finishCount;
    }

    public void setFinishCount(Integer finishCount) {
        this.finishCount = finishCount;
    }

    public Integer getLeft() {
        return left;
    }

    public void setLeft(Integer left) {
        this.left = left;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getFromNode() {
        return fromNode;
    }

    public void setFromNode(String fromNode) {
        this.fromNode = fromNode;
    }

    public String getToNode() {
        return toNode;
    }

    public void setToNode(String toNode) {
        this.toNode = toNode;
    }

    public Integer getM() {
        return m;
    }

    public void setM(Integer m) {
        this.m = m;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public String getForkPath() {
        return forkPath;
    }

    public void setForkPath(String forkPath) {
        this.forkPath = forkPath;
    }

    public String getLastFork() {
        return lastFork;
    }

    public void setLastFork(String lastFork) {
        this.lastFork = lastFork;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
