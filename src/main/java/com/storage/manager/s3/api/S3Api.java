package com.storage.manager.s3.api;

import com.storage.manager.s3.api.docs.S3ApiDocs;
import com.storage.manager.s3.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping(S3Api.PATH)
public class S3Api implements S3ApiDocs {

    public static final String PATH = "v1/s3";

    @Autowired
    private S3Service s3Service;

    @Override
    @GetMapping
    public List<String> listAllObjectsInBucket(@RequestParam("bucketName") String bucketName) {
        return s3Service.listAllObjectsInBucket(bucketName);
    }

    @Override
    @GetMapping("download/{name}")
    public ResponseEntity<Resource> downloadByName(@PathVariable String name, @RequestParam("bucketName") String bucketName) throws Exception {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(s3Service.downloadByName(name, bucketName));
    }

    @Override
    @GetMapping("backup")
    public byte[] backupFiles(@RequestParam("bucketName") String bucketName) {
        return s3Service.backupFiles(bucketName);
    }

    @Override
    @GetMapping("buckets")
    public List<String> availableBuckets() {
        return s3Service.availableBuckets();
    }

    @Override
    @PostMapping
    public String uploadFile(
            @RequestParam("file") MultipartFile multipartFile,
            @RequestParam(value = "bucket", required = false, defaultValue = "") String bucket) {
        try {
            return s3Service.uploadFile(multipartFile, bucket);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @DeleteMapping
    public void removeFile(@RequestParam("file") String name, @RequestParam("bucket") String bucketName) {
        s3Service.removeFile(name, bucketName);
    }
}
