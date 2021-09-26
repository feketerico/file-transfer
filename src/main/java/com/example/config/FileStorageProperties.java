package com.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {

    private String uploadDir;

    private String fileIds;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public String getFileIds(){
        return fileIds;
    }

    public void setFileIds(String fileIds) {this.fileIds = fileIds;}
}
