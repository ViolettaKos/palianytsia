package com.example.palianytsia.config;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SessionConfig {
    @Bean
    public HttpSessionListener httpSessionListener() {
        return new SessionListener();
    }

    private class SessionListener implements HttpSessionListener {

        @Override
        public void sessionCreated(HttpSessionEvent se) {
            se.getSession().setAttribute("cartCount", 0);
        }

    }
}
