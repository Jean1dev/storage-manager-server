package com.storage.manager.local;

import org.springframework.web.multipart.MultipartFile;

public class FileTypesHandler {

    public static boolean isImage(String contentType) {
        return contentType.contains("image");
    }

    public static boolean isTxt(String contentType) {
        return contentType.contains("text/plain");
    }

    public static boolean isATypeResolvedByApplication(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null)
            return false;


        return isImage(contentType) || isTxt(contentType);
    }
}
