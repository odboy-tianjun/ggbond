package cn.odboy.module.dynamicale;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.script.*;
import java.util.Map;

/**
 * 脚本解析器
 *
 * @author tianjun
 * @date 2022-04-22
 */
@Component
public class JavaScriptParser {
    @Autowired
    private ScriptEngine scriptEngine;

    /**
     * 绑定参数，解析表达式
     *
     * @param bindingVars 绑定的变量和值
     * @param scriptRegex 脚本表达式
     * @throws ScriptException
     */
    public Object doCompilable(Map<String, Object> bindingVars, String scriptRegex) throws ScriptException {
        Compilable compilable = (Compilable) scriptEngine;
        Bindings bindings = scriptEngine.createBindings();
        CompiledScript compiledScript = compilable.compile(scriptRegex);

        // 绑定参数
        if (bindingVars.size() > 0) {
            for (String varName : bindingVars.keySet()) {
                bindings.put(varName, bindingVars.get(varName));
            }
        }

        // 代入参数, 解析表达式, 返回结果
        // 这里的结果虽然是个object, 但是和你输入的这个表达式有直接的关系
        // 如果你的表达式是个逻辑表达式, 那么这里的object类型一定是布尔值
        // 如果你的表达式是个计算表达式, 那么这里的object类型一定是双精度小数值
        return compiledScript.eval(bindings);
    }
}
