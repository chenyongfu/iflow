package com.iflow.service.impl.node;

import com.iflow.dao.DaoService;
import com.iflow.entity.IflowInstanceParam;
import com.iflow.service.node.IflowInstanceParamService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class IflowInstanceParamServiceImpl implements IflowInstanceParamService {

    @Resource(name = "iflowDao")
    private DaoService iflowDao;



    /**
     * 根据实例ID，查询实例参数
     * @param instanceId
     * @return
     */
    @Override
    public IflowInstanceParam findByInstanceId(String instanceId) throws Exception {
        if(StringUtils.isBlank(instanceId)){
            return null;
        }

        String sql = "select * from iflow_instance_param where instance_id = ?";
        return iflowDao.findUnique(IflowInstanceParam.class, sql, instanceId);
    }


    /**
     * 保存流程实例参数
     * @param iparams
     */
    @Override
    public IflowInstanceParam saveIflowInstanceParam(IflowInstanceParam iparams) throws Exception {
        if(iparams == null){
            return null;
        }
        iflowDao.saveOrUpdate(iparams);

        return this.findByInstanceId(iparams.getParamId());
    }
    
}
