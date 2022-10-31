package com.redhat.camel.route.coverage.process;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.Map;

public class TemplateRenderer {

    public static String render(String templateName, Map<String, Object> data) {

        TemplateEngine templateEngine = new TemplateEngine();
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setPrefix("/templates/");
        resolver.setSuffix(".html");
        templateEngine.setTemplateResolver(resolver);

        Context context = new Context();
        data.entrySet().forEach(entry -> {
            context.setVariable(entry.getKey(), entry.getValue());
        });

        return templateEngine.process(templateName, context);
    }
}
