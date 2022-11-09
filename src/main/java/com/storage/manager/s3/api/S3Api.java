package com.storage.manager.s3.api;

import com.storage.manager.s3.api.docs.S3ApiDocs;
import com.storage.manager.s3.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(S3Api.PATH)
public class S3Api implements S3ApiDocs {

    public static final String PATH = "v1/s3";

    @Autowired
    private S3Service s3Service;

    @Override
    @GetMapping
    public List<String> listAllObjectsInBucket() {
        return s3Service.listAllObjectsInBucket();
    }

    @Override
    @GetMapping("download/{name}")
    public ResponseEntity<Resource> downloadByName(@PathVariable String name) throws Exception {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(s3Service.downloadByName(name));
    }
}
