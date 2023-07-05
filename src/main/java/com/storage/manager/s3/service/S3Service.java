package com.storage.manager.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class S3Service {

    private static final Logger LOG = LoggerFactory.getLogger(S3Service.class);

    private List<String> bucketsCreated = new ArrayList<>();

    @Autowired
    private AmazonS3 s3;

    public void getBucketsCreated() {
        LOG.info("recuperando listagem de buckets");
        bucketsCreated = s3.listBuckets().stream().map(Bucket::getName).collect(Collectors.toList());
        LOG.info(bucketsCreated.size() + " total de buckets recuperados");
    }

    public String uploadFile(MultipartFile multipartFile, String bucketName) throws IOException, URISyntaxException {
        if (!bucketsCreated.contains(bucketName)) {
            s3.createBucket(bucketName);
            bucketsCreated.add(bucketName);
        }

        String fileName = UUID.randomUUID() + multipartFile.getName();
        File file = new File(fileName);

        Path copyLocation = Paths.get(file.getAbsolutePath());
        Files.copy(multipartFile.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
        s3.putObject(bucketName, fileName, new File(fileName));
        s3.setObjectAcl(bucketName, fileName, CannedAccessControlList.PublicRead);
        LOG.info("upload finalizado");

        file.delete();
        return s3.getUrl(bucketName, fileName).toURI().toString();
    }

    public List<String> listAllObjectsInBucket(String bucketName) {
        ObjectListing objectListing = s3.listObjects(bucketName);
        LOG.info("quantidade de objetos recuperados" + objectListing.getObjectSummaries().size());
        return objectListing.getObjectSummaries().stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());
    }

    public Resource downloadByName(String name, String bucketName) throws Exception {
        S3Object s3Object = s3.getObject(bucketName, name);

        if (s3Object == null) {
            throw new Exception("Object not found");
        }

        File file = new File(UUID.randomUUID().toString());
        Files.copy(s3Object.getObjectContent(), file.toPath());

        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(Paths.get(file.getAbsolutePath())));

        file.delete();

        return resource;
    }

    public byte[] backupFiles(String bucketName) {
        File backup = new File("backup");
        if (!backup.exists()) {
            backup.mkdirs();
        }

        s3.listObjects(bucketName).getObjectSummaries()
                .stream()
                .map(S3ObjectSummary::getKey)
                .forEach(key -> {
                    S3ObjectInputStream inputStream = s3.getObject(bucketName, key).getObjectContent();
                    File file = new File(backup + "/" + UUID.randomUUID());
                    LOG.info("downloaded" + file.getAbsolutePath());
                    try {
                        Files.copy(inputStream, file.toPath());
                    } catch (IOException e) {
                        LOG.error("erro ao criar o arquivo", e);
                    }
                });

        try {
            return zipFiles(backup);
        } catch (IOException e) {
            LOG.error("erro ao zipar os arquivos", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    private byte[] zipFiles(File pathToZip) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        File[] files = pathToZip.listFiles();

        for (File xfile : files) {
            if (!xfile.isFile())
                continue;

            ZipEntry entry = new ZipEntry(xfile.getName());
            zipOutputStream.putNextEntry(entry);
            zipOutputStream.write(IOUtils.toByteArray(new FileInputStream(xfile)));
            zipOutputStream.closeEntry();
        }

        zipOutputStream.close();

        for (File file : files) {
            file.delete();
        }

        return outputStream.toByteArray();
    }

    public List<String> availableBuckets() {
        if (bucketsCreated.isEmpty()) {
            getBucketsCreated();
        }

        return bucketsCreated;
    }
}
