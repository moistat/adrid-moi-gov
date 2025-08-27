package com.example.pentaho.component;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="security-config-component")
public class SecurityConfigComponet {


    private String contentSecurityPolicy;


    private String permissionsPolicy;


    public String getContentSecurityPolicy() {
        return contentSecurityPolicy;
    }

    public void setContentSecurityPolicy(String contentSecurityPolicy) {
        this.contentSecurityPolicy = contentSecurityPolicy;
    }

    public String getPermissionsPolicy() {
        return permissionsPolicy;
    }

    public void setPermissionsPolicy(String permissionsPolicy) {
        this.permissionsPolicy = permissionsPolicy;
    }

    @Override
    public String toString() {
        return "ResponseHeaderConfiguration{" +
                "contentSecurityPolicy='" + contentSecurityPolicy + '\'' +
                ", permissionsPolicy='" + permissionsPolicy + '\'' +
                '}';
    }
}
