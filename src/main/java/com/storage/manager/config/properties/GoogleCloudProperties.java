package com.storage.manager.config.properties;

public class GoogleCloudProperties {
    private String credentials;
    private String projectId;

    public GoogleCloudProperties() {
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
