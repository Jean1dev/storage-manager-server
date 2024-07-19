package com.storage.manager.gcstorage.api;

import com.storage.manager.gcstorage.api.docs.GCStorageApiDocs;
import com.storage.manager.gcstorage.service.GoogleCloudStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(GCStorageApi.PATH)
public class GCStorageApi implements GCStorageApiDocs {

    public static final String PATH = "v1/cloud-storage";

    @Autowired
    private GoogleCloudStorageService googleCloudStorageService;

    @Override
    @PostMapping
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile multipartFile,
            @RequestParam(value = "bucket", required = false, defaultValue = "default_bucket") String bucket) {
        try {
            var ret = googleCloudStorageService.uploadFile(multipartFile, bucket);
            return ResponseEntity.ok(ret);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @GetMapping
    public ResponseEntity<byte[]> getContent(
            @RequestParam("bucketName") String bucketName,
            @RequestParam("objectName") String objectName) {
        var ret = googleCloudStorageService.get(bucketName, objectName);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(ret.contentType()))
                .body(ret.content());
    }

    @Override
    @GetMapping("{bucketName}")
    public List<String> list(@PathVariable("bucketName") String bucketName) {
        return googleCloudStorageService.list(bucketName);
    }

    @Override
    @GetMapping("buckets")
    public List<String> listBuckets() {
        return googleCloudStorageService.listBuckets();
    }
}
