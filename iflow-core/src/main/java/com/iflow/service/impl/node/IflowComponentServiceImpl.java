package com.iflow.service.impl.node;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iflow.dao.DaoService;
import com.iflow.entity.IflowComponent;
import com.iflow.service.node.IflowComponentService;
import com.iflow.util.IflowConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IflowComponentServiceImpl implements IflowComponentService {

    @Resource(name = "iflowDao")
    private DaoService iflowDao;
    
    
    
    /**
     * 根据ID，查询IflowTemplate
     * @return
     */
    @Override
    public IflowComponent findById(String id) throws Exception {
        String sql = "select * from iflow_component where component_id = ?";
        return iflowDao.findUnique(IflowComponent.class, sql, id);
    }


    /**
     * 根据模板ID，查询所有组件
     * @param templateId
     * @return
     * @throws Exception
     */
    @Override
    public List<IflowComponent> findByTemplateId(String templateId) throws Exception {
        String sql = "select * from iflow_component where template_id = ?";
        return iflowDao.find(IflowComponent.class, sql, templateId);
    }


    /**
     * 根据模板ID，查询IflowComponent
     * @param templateId
     * @return
     */
    @Override
    public Map<String, Object> findMapByTemplateId(String templateId) throws Exception {

        List<IflowComponent> cmps = this.findByTemplateId(templateId);
        if(cmps == null){
            return null;
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        for(IflowComponent cmp : cmps){
            String type = cmp.getComponentType();
            if(IflowConstant.COMPONENT_TYPE_ARROW.indexOf(type) >= 0){
                // 连接线
                Map<String, Object> lineMap = (Map<String, Object>)resultMap.get("lines");
                if(lineMap == null){
                    lineMap = new HashMap<String, Object>();
                }
                lineMap.put(cmp.getComponentId(), cmp);
                resultMap.put("lines", lineMap);
            }else{
                Map<String, Object> nodeMap = (Map<String, Object>)resultMap.get("nodes");
                if(nodeMap == null){
                    nodeMap = new HashMap<String, Object>();
                }
                nodeMap.put(cmp.getComponentId(), cmp);
                resultMap.put("nodes", nodeMap);
            }
        }
        return resultMap;
    }

    
    /**
     * 保存流程模板
     * @param cmp
     */
    @Override
    public IflowComponent saveIflowComponent(IflowComponent cmp) throws Exception {
        if(cmp == null){
            return null;
        }

        iflowDao.saveOrUpdate(cmp);

        return this.findById(cmp.getComponentId());
    }

    
    /**
     * 删除流程模板
     * @param id
     * @return
     */
    @Override
    public Boolean deleteIflowComponentById(String id) throws SQLException {
        if(StringUtils.isBlank(id)){
            return false;
        }

        String sql = "delete from iflow_component where component_id = ?";
        iflowDao.executeUpdate(sql, id);
        return true;
    }


    /**
     * 保存流程模板
     * @param param
     */
    @Override
    public Boolean saveIflowComponentList(Map<String, Object> param) {
        if(param == null){
            return false;
        }
        
        // 解析json
        JSONObject json = JSONObject.parseObject(JSON.toJSONString(param));
        if(json == null){
            return false;
        }
        
        Object templateId = json.get("templateId");
        Object nodeData = json.get("nodeData");
        Object lineData = json.get("lineData");
        Object deleteItem = json.get("deleteItem");
        if(templateId == null || nodeData == null || lineData == null){
            return false;
        }
        
        try {
            JSONArray nodeArr = JSONArray.parseArray(JSON.toJSONString(nodeData));
            JSONArray lineArr = JSONArray.parseArray(JSON.toJSONString(lineData));
            
            IflowComponent cmp = null;
            List<IflowComponent> nodeList = new ArrayList<IflowComponent>();
            
            // 遍历组价
            nodeArr.addAll(lineArr);
            for(Object obj : nodeArr){
                JSONObject node = (JSONObject) obj;
                cmp = new IflowComponent();
                Object idObj = node.get("componentId");
                if(idObj != null){
                    cmp.setComponentId(idObj.toString());
                }
                cmp.setTemplateId(Integer.parseInt(templateId.toString()));
                cmp.setComponentName(node.get("name") == null ? null : node.get("name").toString());
                cmp.setComponentType(node.get("type") == null ? null : node.get("type").toString());
                cmp.setWhodoWay((node.get("whodoWay") == null || "".equals(node.get("whodoWay").toString())) ? null : node.getInteger("whodoWay"));
                cmp.setWhodoId(node.get("whodoId") == null ? null : node.get("whodoId").toString());
                cmp.setEmail(node.get("email") == null ? null : node.get("email").toString());
                cmp.setWhodoBean(node.get("whodoBean") == null ? null : node.get("whodoBean").toString());
                
                cmp.setSignWay((node.get("signWay") == null || "".equals(node.get("signWay").toString())) ? null : node.getInteger("signWay"));
                cmp.setNeedSignCount((node.get("needSignCount") == null || "".equals(node.get("needSignCount").toString())) ? null : node.getInteger("needSignCount"));
                cmp.setActionBean(node.get("actionBean") == null ? null : node.get("actionBean").toString());
                cmp.setArrowType((node.get("arrowType") == null || "".equals(node.get("arrowType").toString())) ? null : node.getInteger("arrowType"));
                cmp.setArrowExpression(node.get("arrowExpression") == null ? null : node.get("arrowExpression").toString());
                cmp.setFromNode(node.get("from") == null ? null : node.get("from").toString());
                cmp.setToNode(node.get("to") == null ? null : node.get("to").toString());
                cmp.setLeft((node.get("left") == null || "".equals(node.get("left").toString())) ? null : node.getInteger("left"));
                cmp.setTop((node.get("top") == null || "".equals(node.get("top").toString())) ? null : node.getInteger("top"));
                cmp.setWidth((node.get("width") == null || "".equals(node.get("width").toString())) ? null : node.getInteger("width"));
                cmp.setHeight((node.get("height") == null || "".equals(node.get("height").toString())) ? null : node.getInteger("height"));
                cmp.setMarked(node.get("marked") == null ? false : node.getBoolean("marked"));
                cmp.setRelationId(node.get("relationId") == null ? null : node.get("relationId").toString());
                
                // 判断处理人类型，这些类型是互斥的
                if(cmp.getWhodoWay() != null && cmp.getWhodoWay() == IflowConstant.WHODO_WAY_BY_WHODO_ID){
                    cmp.setWhodoBean(null);
                }else if(cmp.getWhodoWay() != null && cmp.getWhodoWay() == IflowConstant.WHODO_WAY_BY_WHODO_BEAN){
                    cmp.setWhodoId(null);
                    cmp.setEmail(null);
                }
                // 判断会签类型
                if(cmp.getSignWay() != null && cmp.getSignWay() != IflowConstant.SIGN_WAY_BY_COUNT){
                    cmp.setNeedSignCount(null);
                }
                // 判断连接类型
                if(cmp.getArrowType() != null && cmp.getArrowType() == IflowConstant.ARROW_TYPE_BY_NONE){
                    cmp.setArrowExpression(null);
                }
                
                nodeList.add(cmp);
            }
            
            // 删除页面已删除的组件
            if(deleteItem != null){
                String[] arr = deleteItem.toString().split(",");
                for(String id : arr){
                    this.deleteIflowComponentById(id);
                }
            }
            
            // 先保存，生成组件ID
            nodeList.forEach(node -> {
                try {
                    iflowDao.saveOrUpdate(node);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            
            // 替换fromNode toNode的ID为真正的ComponentId
            StringBuilder flushSql = new StringBuilder();
            flushSql.append(" update iflow_component t");
            flushSql.append("   left join iflow_component t1 on t1.relation_id = t.from_node");
            flushSql.append("   left join iflow_component t2 on t2.relation_id = t.to_node");
            flushSql.append(" set t.from_node = t1.component_id, t.to_node = t2.component_id");
            flushSql.append(" where t.template_id = ?");
            flushSql.append(" and t1.template_id = t.template_id");
            flushSql.append(" and t2.template_id = t.template_id");
            iflowDao.executeUpdate(flushSql.toString(), templateId.toString());
            
            // 刷新forkPath
            this.saveForkPath(templateId.toString());
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 保存分支路径
     * @param templateId
     */
    private void saveForkPath(String templateId) throws Exception {
        // 当前模板的所有节点
        List<IflowComponent> newComps = this.findByTemplateId(templateId);
        // 开始节点
        IflowComponent current = this.findStart(newComps);
        // 连线
        List<IflowComponent> lines = this.getLines(newComps);
        // 下一级节点
        List<IflowComponent> nexts = this.getNexts(current.getComponentId(), newComps, lines);
        // 结果列表
        List<IflowComponent> resultList = new ArrayList<IflowComponent>();
        
        // 递归生成分支路径
        try {
            this.generateForkPath(current, nexts, newComps, lines, resultList);

            // 保存
            resultList.forEach(result -> {
                try {
                    iflowDao.saveOrUpdate(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    /**
     * 根据模板ID，查询开始节点
     * @param templateId
     * @return
     */
    @Override
    public IflowComponent findStartByTemplateId(String templateId) throws Exception {
        if(templateId == null){
            return null;
        }

        StringBuilder sql = new StringBuilder("select * ");
        sql.append(" from iflow_component");
        sql.append(" where (component_type = 'start round' or component_type = 'start')");
        sql.append("   and template_id = ?");
        return iflowDao.findUnique(IflowComponent.class, sql.toString(), templateId);
    }


    /**
     * 根据模板ID，查询结束节点
     * @param templateId
     * @return
     */
    @Override
    public IflowComponent findEndByTemplateId(String templateId) throws Exception {
        if(templateId == null){
            return null;
        }

        StringBuilder sql = new StringBuilder("select * ");
        sql.append(" from iflow_component");
        sql.append(" where (component_type = 'end round' or component_type = 'end')");
        sql.append("   and template_id = ?");
        return iflowDao.findUnique(IflowComponent.class, sql.toString(), templateId);
    }


    /**
     * 查询给定节点之前的节点
     * @param component
     * @return
     */
    @Override
    public IflowComponent findNext(IflowComponent component) throws Exception {
        if(component == null 
                || StringUtils.isBlank(component.getComponentId())){
            return null;
        }

        StringBuilder sql = new StringBuilder("select *");
        sql.append(" from iflow_component");
        sql.append(" where component_id = (");
        sql.append("   select to_node from iflow_component where template_id=? and from_node=?");
        sql.append(" )");
        return iflowDao.findUnique(IflowComponent.class, sql.toString(), component.getTemplateId(), component.getComponentId());
    }


    /**
     * 获取分支节点上下一级所有的箭头连线
     * @param component
     * @return
     */
    @Override
    public List<IflowComponent> findArrowsByFork(IflowComponent component) throws Exception {
        if(component == null 
                || StringUtils.isBlank(component.getComponentId())){
            return null;
        }

        StringBuilder sql = new StringBuilder("select * ");
        sql.append(" from iflow_component");
        sql.append(" where component_type = 'fork round'");
        sql.append("   and from_node = ? and template_id = ?");
        return iflowDao.find(IflowComponent.class, sql.toString(), component.getComponentId(), component.getTemplateId());
    }


    /**
     * 计算，生成节点分支路径 forkPath
     *
     * @param current
     * @param nexts
     * @param comps
     * @param lines
     * @param resultList
     * @throws Exception
     */
    private void generateForkPath(IflowComponent current, List<IflowComponent> nexts, 
            List<IflowComponent> comps, List<IflowComponent> lines, List<IflowComponent> resultList) throws Exception {
        String id = current.getComponentId();
        String type = current.getComponentType();
        String forkPath = current.getForkPath();
        // 判断当前节点类型
        if(nexts == null 
                || StringUtils.isBlank(id)
                || IflowConstant.COMPONENT_TYPE_END.equals(type)){
            return;
        }
        for(IflowComponent next : nexts){
            boolean changed = false;
            String nextId = next.getComponentId();
            String nextType = next.getComponentType();
            if(IflowConstant.COMPONENT_TYPE_FORK.equals(type) || StringUtils.isNotBlank(forkPath)){
                next.setForkPath(forkPath);
                changed = true;
            }
            if(IflowConstant.COMPONENT_TYPE_FORK.equals(nextType)){
                // 遇分支则追加path
                String tempPath = next.getForkPath();
                tempPath = StringUtils.isBlank(tempPath) ? nextId : tempPath +"-"+ nextId;
                next.setForkPath(tempPath);
                changed = true;
            }else if(IflowConstant.COMPONENT_TYPE_JOIN.equals(nextType)){
                // 遇会合则从后递减path
                String path = next.getForkPath();
                if(StringUtils.isBlank(path)){
                    throw new Exception("聚合节点的上一级节点forkPath不应该为空，前面应该有分支节点");
                }
                int index = path.lastIndexOf("-");
                path = index >= 0 ? path.substring(0, index) : null;
                next.setForkPath(path);
                next.setLastFork(forkPath);
                changed = true;
            }
            
            // 加入结果集
            if(changed){
                resultList.add(next);
            }
            
            // 递归调用
            List<IflowComponent> nnexts = this.getNexts(nextId, comps, lines);
            this.generateForkPath(next, nnexts, comps, lines, resultList);
        }
    }


    /**
     * 获取当前节点的下一级节点
     *
     * @param id
     * @param comps
     * @param lines
     * @return
     */
    private List<IflowComponent> getNexts(String id, List<IflowComponent> comps, List<IflowComponent> lines) {
        if(StringUtils.isBlank(id) || comps == null || lines == null){
            return null;
        }
        List<IflowComponent> nexts = new ArrayList<IflowComponent>();
        for(IflowComponent line : lines){
            if(line != null && id.equals(line.getFromNode())){
                nexts.add(this.find(line.getToNode(), comps));
            }
        }
        return nexts;
    }


    /**
     * 获取模板的所有连线
     * @param comps
     * @return
     */
    private List<IflowComponent> getLines(List<IflowComponent> comps) {
        if(comps == null){
            return null;
        }
        List<IflowComponent> list = new ArrayList<IflowComponent>();
        for(IflowComponent item : comps){
            if(item != null && IflowConstant.COMPONENT_TYPE_ARROW.indexOf(item.getComponentType()) >= 0){
                list.add(item);
            }
        }
        return list;
    }


    /**
     * 获取开始节点
     *
     * @param comps
     * @return
     */
    private IflowComponent findStart(List<IflowComponent> comps) {
        if(comps == null){
            return null;
        }
        for(IflowComponent item : comps){
            if(item != null && IflowConstant.COMPONENT_TYPE_START.equals(item.getComponentType())){
                return item;
            }
        }
        return null;
    }


    /**
     * 获取开始节点
     *
     * @param id
     * @param comps
     * @return
     */
    private IflowComponent find(String id, List<IflowComponent> comps) {
        if(StringUtils.isBlank(id) || comps == null){
            return null;
        }
        for(IflowComponent item : comps){
            if(id.equals(item.getComponentId())){
                return item;
            }
        }
        return null;
    }
    
}
