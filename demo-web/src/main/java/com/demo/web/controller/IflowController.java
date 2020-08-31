package com.demo.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.iflow.entity.IflowApproveRecord;
import com.iflow.entity.IflowTemplate;
import com.iflow.json.IflowParam;
import com.iflow.json.Page;
import com.iflow.service.node.IflowComponentService;
import com.iflow.service.node.IflowInstanceService;
import com.iflow.service.node.IflowTemplateService;
import com.iflow.service.schedule.IflowScheduleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


/**
 * @ClassName: IflowController
 * @Description: IFLOW 流程控制器
 * @author chenyf
 * @date 2015年10月12日 下午5:26:27
 */
@Controller
@RequestMapping("/iflow")
public class IflowController {
    
    @Autowired
    private IflowTemplateService templateService;
    
    @Autowired
    private IflowInstanceService instanceService;
    
    @Autowired
    private IflowComponentService componentService;
    
    @Autowired
    private IflowScheduleService scheduleService;


    public static final String SUCCESS_FLAG = "{\"flag\": true}";
    public static final String ERROR_FLAG = "{\"flag\": false}";


    
    /**
     * 显示流程模板管理页面
     * 
     * @return
     */
    @RequestMapping("/showTemplate.html")
    public ModelAndView showTemplate() {
        ModelAndView mav = new ModelAndView("iflow/iflowTemplate.jsp");
        return mav;
    }

    
    /**
     * 显示模板设计器页面
     * 
     * @param request
     * @return
     */
    @RequestMapping("/showDesigner.html")
    public ModelAndView showDesigner(HttpServletRequest request) throws Exception {
        String queryStr = request.getQueryString();
        if(StringUtils.isNotBlank(queryStr)){
            queryStr = "?"+ queryStr;
        }
        String url = "iflow/iflowDesigner.jsp"+ queryStr;
        ModelAndView mav = new ModelAndView(url);
        
        // 取出模板ID，查出已有的模板数据
        String templateId = request.getParameter("templateId");
        if(StringUtils.isNotBlank(templateId)){
            Map<String, Object> map = componentService.findMapByTemplateId(templateId);
            mav.addObject("componentMap", map);
        }
        return mav;
    }

    
    /**
     * 分页查询IflowTemplate
     * 
     * @param param
     * @return
     */
    @RequestMapping(value = "findIflowTemplateByPage", method = RequestMethod.POST)
    @ResponseBody
    public Page findIflowTemplateByPage(Page page, Map<String, Object> param) throws Exception {
        return templateService.findByPage(page, param);
    }

    
    /**
     * 保存流程模板
     * @param template
     * @return
     */
    @RequestMapping(value = "saveIflowTemplate", method = RequestMethod.POST)
    @ResponseBody
    public String saveIflowTemplate(HttpServletResponse response, IflowTemplate template) throws Exception {
        IflowTemplate res = templateService.saveIflowTemplate(template);

        JSONObject resultJson = new JSONObject();
        resultJson.put("flag", true);
        resultJson.put("data", res);
        return resultJson.toJSONString();
    }

    
    /**
     * 保存流程模板
     * @param id
     * @return
     */
    @RequestMapping(value = "deleteIflowTemplate", method = RequestMethod.POST)
    @ResponseBody
    public Boolean deleteIflowTemplate(String id) throws Exception {
        Boolean res = templateService.deleteById(id);
        return res;
    }

    
    /**
     * 保存流程模板设计器内容
     * @return
     */
    @RequestMapping(value = "saveIflowDesigner", method = RequestMethod.POST)
    @ResponseBody
    public String saveIflowDesigner(HttpServletResponse response, @RequestParam Map<String, Object> param) throws Exception {
        if(param == null){
            return ERROR_FLAG;
        }

        // 保存
        Boolean res = componentService.saveIflowComponentList(param);
        
        // 取出模板ID，刷新模板数据
        Object templateIdObj = param.get("templateId");
        if(templateIdObj == null){
            return ERROR_FLAG;
        }
        String templateId = templateIdObj.toString();
        Map<String, Object> map = componentService.findMapByTemplateId(templateId);

        JSONObject resultJson = new JSONObject();
        resultJson.put("flag", true);
        resultJson.put("data", map);
        return resultJson.toJSONString();
    }

    
    /**
     * 任务节点审批
     *  只做2件事，保存意见 >> 告诉流程自动更新
     * @param record
     * @return
     */
    @RequestMapping(value = "approve", method = RequestMethod.POST)
    @ResponseBody
    public String approve(IflowApproveRecord record) throws Exception {
        if(record == null){
            return ERROR_FLAG;
        }

        Boolean status = instanceService.approve(record);

        JSONObject resultJson = new JSONObject();
        resultJson.put("flag", true);
        resultJson.put("data", status);
        return resultJson.toJSONString();
    }

    
    /**
     * 任务节点审批
     *  只做2件事，保存意见 >> 告诉流程自动更新
     * @param startParam
     * @return
     */
    @RequestMapping(value = "start", method = RequestMethod.POST)
    @ResponseBody
    public String start(IflowParam startParam){
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("days", 3);
        startParam.setFormMap(map);
        
        try {
            scheduleService.start(startParam);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS_FLAG;
    }

    
    /**
     * 校验流程模板CODE 唯一性
     * 唯一返回true
     * @return
     */
    @RequestMapping("uniqueValidate")
    @ResponseBody
    public boolean uniqueValidate(String code) throws Exception {
        return templateService.uniqueValidate(code);
    } 

}
