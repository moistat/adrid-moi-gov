package com.example.pentaho.cofig;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
public class StaticResourcesWebConfiguration  implements WebMvcConfigurer {


    protected static final String[] RESOURCE_LOCATIONS = new String[] {
            "classpath:/static/",
            "classpath:/static/css/",
            "classpath:/static/js/",
    };
    protected static final String[] RESOURCE_PATHS = new String[] {
            "/*.js",
            "/*.css",
            "/*.svg",
            "/*.png",
            "/*.jpg",
            "*.jpg",
            "*.ico",
            "/css/**",
            "/js/*",
    };


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        ResourceHandlerRegistration resourceHandlerRegistration = appendResourceHandler(registry);
        initializeResourceHandler(resourceHandlerRegistration);
    }

    protected ResourceHandlerRegistration appendResourceHandler(ResourceHandlerRegistry registry) {
        return registry.addResourceHandler(RESOURCE_PATHS);
    }

    protected void initializeResourceHandler(ResourceHandlerRegistration resourceHandlerRegistration) {
        resourceHandlerRegistration.addResourceLocations(RESOURCE_LOCATIONS).setCacheControl(getCacheControl());
    }

    protected CacheControl getCacheControl() {
        return CacheControl.maxAge(14400, TimeUnit.DAYS).cachePublic();
    }
}
