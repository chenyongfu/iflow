package com.iflow.util;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * 脚本语言执行工具
 */
public class ScriptEngineUtil {

    /**
     * JavaScript引擎，执行JS脚本
     */
    private static ScriptEngine jsEngine = new ScriptEngineManager().getEngineByName("JavaScript");



    /**
     * JS脚本引擎，计算表达式 例如:
     * exp： var a=10, b=5; a+(a/b); 结果: 12.0
     * exp： (2 > 1 || 2 > 3) 结果: true
     *
     * @param exp
     * @return
     */
    public static Object eval(String exp) {
        Object result = null;
        try {
            result = jsEngine.eval(exp);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return result;
    }


}
