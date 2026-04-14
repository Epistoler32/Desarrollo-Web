package com.seaside.config;

import jakarta.servlet.Servlet;
import org.h2.server.web.JakartaWebServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "spring.h2.console", name = "enabled", havingValue = "true")
public class H2ConsoleConfig {

    @Bean
    public ServletRegistrationBean<Servlet> h2ConsoleServlet(
            @Value("${spring.h2.console.path:/h2-console}") String h2ConsolePath) {
        String mapping = h2ConsolePath.endsWith("/") ? h2ConsolePath + "*" : h2ConsolePath + "/*";

        ServletRegistrationBean<Servlet> registrationBean =
                new ServletRegistrationBean<>(new JakartaWebServlet(), mapping);
        registrationBean.addInitParameter("webAllowOthers", "true");
        registrationBean.addInitParameter("trace", "false");
        return registrationBean;
    }
}
