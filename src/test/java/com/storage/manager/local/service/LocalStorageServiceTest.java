package com.storage.manager.local.service;

import com.storage.manager.ApplicationTest;
import com.storage.manager.local.api.dto.UploadResponseDto;
import com.storage.manager.s3.service.S3Service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("LocalStorageService")
public class LocalStorageServiceTest extends ApplicationTest {

    @Autowired
    private LocalStorageService service;

    @MockBean
    private S3Service s3Service;

    @Test
    @DisplayName("deve fazer o upload de um arquivo")
    public void upload() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        File file = new File(UUID.randomUUID().toString());
        file.createNewFile();

        InputStream initialStream = new FileInputStream(file);
        when(multipartFile.getInputStream()).thenReturn(initialStream);

        UploadResponseDto uploadResponseDto = service.uploadFile(multipartFile);

        Assertions.assertEquals("local-bucket", uploadResponseDto.getStorageLocaion());
        Assertions.assertNotNull(uploadResponseDto.getFilename());

        file.delete();
    }
}
