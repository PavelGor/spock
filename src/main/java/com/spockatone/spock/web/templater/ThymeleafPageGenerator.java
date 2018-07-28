package com.spockatone.spock.web.templater;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class ThymeleafPageGenerator {
    private final static ThymeleafPageGenerator instance = new ThymeleafPageGenerator();
    private TemplateEngine templateEngine;

    private ThymeleafPageGenerator() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheTTLMs(3600000L);
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
    }

    public static ThymeleafPageGenerator getInstance() {
        return instance;
    }

    public TemplateEngine getTemplateEngine() {
        return templateEngine;
    }
}
