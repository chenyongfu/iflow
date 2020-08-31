package com.iflow.util;


public class IflowConstant {

    /**
     * 流程KEY
     */
    public static final String IFLOW_KEY    =   "iflow";
    
    
    /**
     * 模板组件类型： start : 开始节点
     */
    public static final String COMPONENT_TYPE_START     =   "start round";
    /**
     * 模板组件类型： end : 结束节点
     */
    public static final String COMPONENT_TYPE_END       =   "end round";
    /**
     * 模板组件类型： task : 人工任务
     */
    public static final String COMPONENT_TYPE_TASK      =   "task";
    /**
     * 模板组件类型： node : 处理动作
     */
    public static final String COMPONENT_TYPE_ACTION    =   "node";
    /**
     * 模板组件类型： fork round : 分支
     */
    public static final String COMPONENT_TYPE_FORK      =   "fork round";
    /**
     * 模板组件类型： join round : 聚合
     */
    public static final String COMPONENT_TYPE_JOIN      =   "join round";
    /**
     * 模板组件类型： sl-lr-tb : 箭头
     */
    public static final String COMPONENT_TYPE_ARROW     =   "sl-lr-tb";
    
    
    /**
     * 处理人方式： 1 指定处理人ID；
     */
    public static final int WHODO_WAY_BY_WHODO_ID       =   1;
    /**
     * 处理人方式： 2 指定bean,运行该方法返回处理人；
     */
    public static final int WHODO_WAY_BY_WHODO_BEAN     =   2;
    
    
    /**
     * 连接类型： 1 无条件； 
     */
    public static final int ARROW_TYPE_BY_NONE          =   1;
    /**
     * 连接类型： 2 条件表达式；
     */
    public static final int ARROW_TYPE_BY_EXPRESSION    =   2;
    
    
    /**
     * 会签方式: 1 按条件选择；
     */
    public static final int SIGN_WAY_BY_EXPRESSION      =   1;
    /**
     * 会签方式: 2 全部通过；
     */
    public static final int SIGN_WAY_BY_ALL             =   2;
    /**
     * 会签方式: 3 指定数目；
     */
    public static final int SIGN_WAY_BY_COUNT           =   3;
    
    
    /**
     * 流程实例状态: 1：审批中
     */
    public static final int INSTANCE_STATE_IN_PROCESS   =   1;
    /**
     * 流程实例状态: 2：已通过
     */
    public static final int INSTANCE_STATE_COMPLETE     =   2;
    /**
     * 流程实例状态: 3：已驳回
     */
    public static final int INSTANCE_STATE_REJECT       =   3;
    /**
     * 流程实例状态: 4：重新审批中
     */
    public static final int INSTANCE_STATE_REPROCESS    =   4;
    
    
    /**
     * 审批状态: 1：自动通过
     */
    public static final int APPROVE_STATE_AUTO          =   1;
    /**
     * 审批状态: 2：通过
     */
    public static final int APPROVE_STATE_COMPLETE      =   2;
    /**
     * 审批状态: 3：驳回
     */
    public static final int APPROVE_STATE_REJECT        =   3;



    
}
