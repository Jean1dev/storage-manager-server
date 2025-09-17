package com.storage.manager.s3.api;

import com.storage.manager.s3.api.docs.S3ApiDocs;
import com.storage.manager.s3.metrics.S3Metrics;
import com.storage.manager.s3.service.S3Service;
import io.micrometer.core.instrument.Timer;
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

    private final S3Service s3Service;
    private final S3Metrics s3Metrics;

    public S3Api(S3Service s3Service, S3Metrics s3Metrics) {
        this.s3Service = s3Service;
        this.s3Metrics = s3Metrics;
    }

    @Override
    @GetMapping
    public List<String> listAllObjectsInBucket(@RequestParam("bucketName") String bucketName) {
        s3Metrics.incrementListCounter();
        return s3Service.listAllObjectsInBucket(bucketName);
    }

    @Override
    @GetMapping("download/{name}")
    public ResponseEntity<Resource> downloadByName(@PathVariable String name, @RequestParam("bucketName") String bucketName) throws Exception {
        s3Metrics.incrementDownloadCounter();
        Timer.Sample sample = s3Metrics.startDownloadTimer();
        try {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(s3Service.downloadByName(name, bucketName));
        } finally {
            s3Metrics.stopDownloadTimer(sample);
        }
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
        s3Metrics.incrementUploadCounter();
        Timer.Sample sample = s3Metrics.startUploadTimer();
        try {
            return s3Service.uploadFile(multipartFile, bucket);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        } finally {
            s3Metrics.stopUploadTimer(sample);
        }
    }

    @Override
    @DeleteMapping
    public void removeFile(@RequestParam("file") String name, @RequestParam("bucket") String bucketName) {
        s3Metrics.incrementDeleteCounter();
        s3Service.removeFile(name, bucketName);
    }
}
