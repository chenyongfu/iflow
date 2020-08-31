package com.iflow.service.impl.node;

import com.iflow.dao.DaoService;
import com.iflow.entity.IflowApproveRecord;
import com.iflow.service.node.IflowApproveRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class IflowApproveRecordServiceImpl implements IflowApproveRecordService {

    @Resource(name = "iflowDao")
    private DaoService iflowDao;



    /**
     * 根据ID，查询审核记录
     * @return
     */
    @Override
    public IflowApproveRecord findById(String id) throws Exception {
        String sql = "select * from iflow_approve_record where approve_id = ?";
        return iflowDao.findUnique(IflowApproveRecord.class, sql, id);
    }


    /**
     * 保存审核记录
     * @param record
     */
    public IflowApproveRecord saveIflowApproveRecord(IflowApproveRecord record) throws Exception {
        if(record == null){
            return null;
        }
        iflowDao.saveOrUpdate(record);

        return this.findById(record.getApproveId());
    }


}
