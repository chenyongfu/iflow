package com.iflow.service.impl.node;

import com.iflow.dao.DaoService;
import com.iflow.entity.IflowCurrentNode;
import com.iflow.service.node.IflowCurrentNodeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;

@Service
public class IflowCurrentNodeServiceImpl implements IflowCurrentNodeService {

    @Resource(name = "iflowDao")
    private DaoService iflowDao;
    


    /**
     * 获取流程当前执行的节点
     * @param instanceId
     * @return
     */
    @Override
    public List<IflowCurrentNode> findByInstanceId(String instanceId) throws Exception {
        if(StringUtils.isBlank(instanceId)){
            return null;
        }

        String sql = "select * from iflow_current_node where instance_id = ?";
        return iflowDao.find(IflowCurrentNode.class, sql, instanceId);
    }


    /**
     * 更新当前节点
     * @param oldCmpId
     * @param currentNode
     */
    @Override
    public void updateIflowCurrentNode(String oldCmpId, IflowCurrentNode currentNode) throws SQLException {
        if(StringUtils.isBlank(oldCmpId)
                || currentNode == null
                || currentNode.getInstanceId() == null
                || currentNode.getComponentId() == null){
            return;
        }

        StringBuilder sql = new StringBuilder("update iflow_current_node set component_id = ? ");
        sql.append(" where instance_id = ? and component_id = ?");
        iflowDao.executeUpdate(sql.toString(), currentNode.getComponentId(), currentNode.getInstanceId(), oldCmpId);
    }

    
}
