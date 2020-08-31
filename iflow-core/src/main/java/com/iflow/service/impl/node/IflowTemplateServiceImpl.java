package com.iflow.service.impl.node;

import com.iflow.dao.DaoService;
import com.iflow.entity.IflowTemplate;
import com.iflow.json.Page;
import com.iflow.service.node.IflowTemplateService;
import com.iflow.util.SqlUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


@Service
public class IflowTemplateServiceImpl implements IflowTemplateService {

    @Resource(name = "iflowDao")
    private DaoService iflowDao;


    
    /**
     * 根据ID，查询IflowTemplate
     * @return
     */
    @Override
    public IflowTemplate findById(String id) throws Exception {
        String sql = "select * from iflow_template where template_id = ?";
        return iflowDao.findUnique(IflowTemplate.class, sql, id);
    }


    /**
     * 分页查询IflowTemplate
     * 
     * @param param
     * @return
     */
    @Override
    public Page findByPage(Page page, Map<String, Object> param) throws Exception {
        if(page == null){
            page = new Page();
        }

        // 组装HQL
        StringBuilder sql = new StringBuilder("select * from iflow_template t where 1=1 ");
        List<Object> params = new LinkedList<Object>();

        Object templateCode = param.get("templateCode");
        if (templateCode != null && StringUtils.isNotBlank(templateCode.toString())) {
            sql.append(" and template_code like ? ");
            params.add(SqlUtil.getLikeKeyword(templateCode.toString()));
        }
        Object templateName = param.get("templateName");
        if (templateName != null && StringUtils.isNotBlank(templateName.toString())) {
            sql.append(" and template_name like ? ");
            params.add(SqlUtil.getLikeKeyword(templateName.toString()));
        }

        sql.append(" order by template_id asc ");
        return iflowDao.findByPage(IflowTemplate.class, page, sql.toString(), params.toArray());
    }

    
    /**
     * 保存流程模板
     * @param template
     */
    @Override
    public IflowTemplate saveIflowTemplate(IflowTemplate template) throws Exception {
        if(template == null){
            return null;
        }

        template.setCreatedDate(new Date());
        iflowDao.saveOrUpdate(template);

        return this.findById(template.getTemplateId());
    }

    
    /**
     * 删除流程模板
     * @param id
     * @return
     */
    @Override
    public Boolean deleteById(String id) throws SQLException {
        if(StringUtils.isBlank(id)){
            return false;
        }
        String sql = "delete from iflow_template where template_id = ?";
        iflowDao.executeUpdate(sql, id);
        return true;
    }


    /**
     * 校验流程模板CODE 唯一性
     * 唯一返回true
     * @return
     */
    @Override
    public boolean uniqueValidate(String code) throws Exception {
        String sql = "select template_id from iflow_template where template_code = ?";
        IflowTemplate tmp = iflowDao.findUnique(IflowTemplate.class, sql, code);
        return tmp == null;
    }


}
