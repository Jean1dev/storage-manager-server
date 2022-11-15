package com.storage.manager.local.service;

import com.storage.manager.local.Huffman;
import com.storage.manager.local.api.dto.UploadResponseDto;
import com.storage.manager.s3.service.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ValidationException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.UUID;

import static com.storage.manager.local.FileTypesHandler.isATypeResolvedByApplication;
import static com.storage.manager.local.FileTypesHandler.isImage;

@Service
public class LocalStorageService {

    private static final Logger LOG = LoggerFactory.getLogger(LocalStorageService.class);

    public UploadResponseDto uploadFile(MultipartFile file) {
        try {
            String filename = salvarArquivoNoDisco(file);
            return new UploadResponseDto(filename, "local-bucket");
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("Não foi possivel salvar o arquivo no disco");
        }
    }

    private String salvarArquivoNoDisco(MultipartFile file) throws IOException, URISyntaxException {
        String separator = FileSystems.getDefault().getSeparator();
        URI uri = this.getClass().getResource(separator).toURI();
        String path = Paths.get(uri).toString();
        String filename = UUID.randomUUID().toString();
        Path copyLocation = Paths.get(path + File.separator + filename);
        Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
        return copyLocation.toString();
    }

    public UploadResponseDto performUpload(MultipartFile file) throws IOException, URISyntaxException {
        if (!isATypeResolvedByApplication(file)) {
            LOG.info("Tipo de arquivo nao resolvido pela aplicacao");
            throw new ValidationException("tipo de arquivo nao suportado");
        }

        if (isImage(file.getContentType())) {
            // do somthing
        } else {
            // is text then do somenthing
            String nomeArquivoSalvo = salvarArquivoNoDisco(file);
            File arquivoDeFato = new File(nomeArquivoSalvo);
            LOG.info("tamanho do arquivo salvo " + arquivoDeFato.getTotalSpace());
            LOG.info("tamanho usado " + arquivoDeFato.getUsableSpace());
            new Huffman().encoding(nomeArquivoSalvo, 1);
            new Huffman().encoding(nomeArquivoSalvo, 2);
            new Huffman().encoding(nomeArquivoSalvo, 3);
        }

        return new UploadResponseDto("", "");
    }
}
