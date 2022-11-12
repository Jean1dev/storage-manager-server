package com.storage.manager.local.service;

import com.storage.manager.local.api.dto.UploadResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class LocalStorageService {

    public UploadResponseDto uploadFile(MultipartFile file) {
        try {
            String separator = FileSystems.getDefault().getSeparator();
            URI uri = this.getClass().getResource(separator).toURI();
            String path = Paths.get(uri).toString();
            String filename = UUID.randomUUID().toString();
            Path copyLocation = Paths.get(path + File.separator + filename);
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
            return new UploadResponseDto(filename, "local-bucket");
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("Não foi possivel salvar o arquivo no disco");
        }
    }

    public UploadResponseDto performUpload(MultipartFile file) throws IOException, URISyntaxException {
        System.out.println(file.getContentType());

        return new UploadResponseDto("", "");
    }
}
