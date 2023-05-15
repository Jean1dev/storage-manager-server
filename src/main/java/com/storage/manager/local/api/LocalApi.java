package com.storage.manager.local.api;

import com.storage.manager.local.api.docs.LocalApiDocs;
import com.storage.manager.local.api.dto.UploadResponseDto;
import com.storage.manager.local.service.LocalStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping(LocalApi.PATH)
public class LocalApi implements LocalApiDocs {
    public static final String PATH = "v1/local";

    @Autowired
    private LocalStorageService service;

    @Override
    @PostMapping
    public UploadResponseDto uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        return service.uploadFile(multipartFile);
    }

    @Override
    @PostMapping("perform-upload")
    public UploadResponseDto performUpload(@RequestParam("file") MultipartFile multipartFile) {
        try {
            return service.performUpload(multipartFile);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
