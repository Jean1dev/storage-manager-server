package com.storage.manager.local.api.dto;

public class UploadResponseDto {

    private String filename;
    private String storageLocaion;

    public UploadResponseDto(String filename, String storageLocaion) {
        this.filename = filename;
        this.storageLocaion = storageLocaion;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getStorageLocaion() {
        return storageLocaion;
    }

    public void setStorageLocaion(String storageLocaion) {
        this.storageLocaion = storageLocaion;
    }
}
