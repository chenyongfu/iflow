package com.iflow.service.impl.node;

import com.iflow.dao.DaoService;
import com.iflow.entity.IflowApproveRecord;
import com.iflow.entity.IflowComponent;
import com.iflow.entity.IflowCurrentNode;
import com.iflow.entity.IflowInstance;
import com.iflow.exception.IflowException;
import com.iflow.service.node.IflowInstanceService;
import com.iflow.service.schedule.IflowScheduleService;
import com.iflow.util.IflowConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class IflowInstanceServiceImpl implements IflowInstanceService {

    @Resource(name = "iflowDao")
    private DaoService iflowDao;

    @Autowired
    private IflowApproveRecordServiceImpl approveRecordService;
    
    @Autowired
    private IflowComponentServiceImpl componentService;
    
    @Autowired
    private IflowCurrentNodeServiceImpl currentNodeService;
    
    @Autowired
    private IflowScheduleService scheduleService;




    /**
     * 根据ID，查询流程ID
     * @param id
     * @return
     * @throws Exception
     */
    public IflowInstance findById(String id) throws Exception {
        if(StringUtils.isBlank(id)){
            return null;
        }

        String sql = "select * from iflow_instance where instance_id = ?";
        return iflowDao.findUnique(IflowInstance.class, sql, id);
    }


    /**
     * 保存流程实例
     * @param instance
     */
    @Override
    public IflowInstance saveIflowInstance(IflowInstance instance) throws Exception {
        if(instance == null){
            return null;
        }

        iflowDao.saveOrUpdate(instance);

        // 重新查询返回
        return this.findById(instance.getInstanceId());
    }

    
    /**
     * 任务节点审批
     *  只做2件事，保存意见 >> 告诉流程自动更新
     * @param record
     * @return
     */
    public Boolean approve(IflowApproveRecord record) throws Exception{
        if(record == null || record.getState() == null){
            return false;
        }
        
        // 保存审批记录
        record.setCreatedDate(new Date());
        approveRecordService.saveIflowApproveRecord(record);
        
        // 获取流程实例，当前节点
        String instanceId = record.getInstanceId();
        if(instanceId == null){
            throw new IflowException("审核记录中的流程实例丢失");
        }
        String componentId = record.getComponentId();
        if(componentId == null){
            throw new IflowException("审核记录中的当前节点丢失");
        }

        IflowInstance instance = this.findById(instanceId);
        IflowComponent component = componentService.findById(componentId);

        // 根据处理状态找出下一批要执行的节点
        int state = record.getState();
        if(state == IflowConstant.APPROVE_STATE_REJECT){
            // 驳回-直接终止当前流程实例，等用户修改表单后后重新发起
            instance.setState(IflowConstant.INSTANCE_STATE_REJECT);
            instance.setLastUpdatedDate(new Date());
            iflowDao.saveOrUpdate(instance);
        } else {
            // 设置下一批要执行的节点为流程当前节点
            IflowComponent next = componentService.findNext(component);
            if(next == null){
                return false;
            }
            IflowCurrentNode cn = new IflowCurrentNode();
            cn.setInstanceId(instance.getInstanceId());
            cn.setComponentId(next.getComponentId());
            cn.setCreatedDate(new Date());
            iflowDao.saveOrUpdate(cn);
            
            // 继续执行流程
            scheduleService.process(instance, next);
            return true;
        }
        return false;
    }


}
