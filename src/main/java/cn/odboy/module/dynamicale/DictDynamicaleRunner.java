package cn.odboy.module.dynamicale;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.script.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 基于字典的动态计算
 *
 * @author tianjun
 * @date 2022-04-22
 */
@Slf4j
@Component
public class DictDynamicaleRunner implements CommandLineRunner {
    @Autowired
    private JavaScriptParser scriptParser;

    @Override
    public void run(String... args) throws ScriptException {
        // 模拟字典 --> 条件表达式, 计算表达式(包含拯救js精度的函数)
        Map<String, String> conditionRegexCaleDict = new HashMap<>(2);
        conditionRegexCaleDict.put("a > b & a == 0", "(a + b).toFixed(3)");
        conditionRegexCaleDict.put("a < b", "((a + 1) * b).toFixed(3)");

        // 入参
        double a = 1, b = 4.2;
        BigDecimal bigDecimal = caleResult(conditionRegexCaleDict, a, b);

        log.info("最终结果为: {}", bigDecimal);
    }

    /**
     * 根据 条件 计算 结果
     */
    public BigDecimal caleResult(Map<String, String> conditionRegexCaleDict, double a, double b) throws ScriptException {
        Map<String, Object> bindings = new HashMap<>(2);
        bindings.put("a", a);
        bindings.put("b", b);

        for (String conditionRegex : conditionRegexCaleDict.keySet()) {
            boolean conditionResult = (boolean) scriptParser.doCompilable(bindings, conditionRegex);
            // 返回符合条件的结果
            if (conditionResult) {
                Object result = scriptParser.doCompilable(bindings, conditionRegexCaleDict.get(conditionRegex));
                return new BigDecimal((String) result);
            }
        }
        // 没有找到符合的条件, 抛出异常
        throw new RuntimeException("没有找到符合的条件, 无法计算最终结果");
    }
}
