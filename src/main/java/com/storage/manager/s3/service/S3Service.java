package com.storage.manager.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class S3Service {

    private static final Logger LOG = LoggerFactory.getLogger(S3Service.class);

    @Autowired
    private AmazonS3 s3;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public List<String> listAllObjectsInBucket() {
        ObjectListing objectListing = s3.listObjects(bucketName);
        LOG.info("quantidade de objetos recuperados" + objectListing.getObjectSummaries().size());
        return objectListing.getObjectSummaries().stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());
    }

    public Resource downloadByName(String name) throws Exception {
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
}
