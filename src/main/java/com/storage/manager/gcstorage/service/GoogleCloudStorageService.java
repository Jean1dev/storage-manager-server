package com.storage.manager.gcstorage.service;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.*;
import com.storage.manager.gcstorage.GoogleCloudResourceOuput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GoogleCloudStorageService {

    private static final Logger log = LoggerFactory.getLogger(GoogleCloudStorageService.class);

    @Autowired
    private Storage storage;

    public String uploadFile(MultipartFile multipartFile, String bucketName) throws IOException {
        var fileName = UUID.randomUUID() + multipartFile.getName();
        final var info = BlobInfo.newBuilder(bucketName, fileName)
                .setContentType(multipartFile.getContentType())
                .build();

        log.info("uploading file: {}", multipartFile.getName());
        Blob from = storage.createFrom(info, multipartFile.getInputStream());
        return from.getSelfLink();
    }

    public GoogleCloudResourceOuput get(String bucket, String id) {
        Blob blob = storage.get(bucket, id);
        String contentType = blob.getContentType();
        byte[] content = blob.getContent();
        return new GoogleCloudResourceOuput(contentType, content);
    }

    public List<String> listBuckets() {
        Page<Bucket> list = storage.list();
        var result =  new ArrayList<String>();

        list.iterateAll().forEach(bucket -> result.add(bucket.getName()));
        while (list.hasNextPage()) {
            list.getNextPage().iterateAll().forEach(bucket -> result.add(bucket.getName()));
        }

        return result;
    }

    public List<String> list(String bucket) {
        Page<Blob> pageBlobs = storage.list(bucket);
        var blobs = new ArrayList<Blob>();

        pageBlobs.iterateAll().forEach(blobs::add);
        while (pageBlobs.hasNextPage()) {
            pageBlobs.getNextPage().iterateAll().forEach(blobs::add);
        }

        log.info("quantidade de blobs recuperados :{}", blobs.size());

        return blobs.stream()
                .map(BlobInfo::getBlobId)
                .map(BlobId::getName)
                .collect(Collectors.toUnmodifiableList());
    }
}
