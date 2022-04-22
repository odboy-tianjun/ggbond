package cn.odboy.module.dynamicale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

@Configuration
public class JavaScriptConfig {
    /**
     * Spring管控javascript引擎
     *
     * @return ScriptEngine
     */
    @Bean
    public ScriptEngine scriptEngine() {
        return new ScriptEngineManager().getEngineByName("JavaScript");
    }
}
