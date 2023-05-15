package com.storage.manager.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.storage.manager.ApplicationTest;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.tomcat.util.digester.ArrayStack;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("S3ServiceTest")
public class S3ServiceTest extends ApplicationTest {

    @Autowired
    private S3Service s3Service;

    @MockBean
    private AmazonS3 amazonS3;

    @Test
    @DisplayName("deve fazer upload de 1 arquivo")
    public void uploadFile() throws IOException, URISyntaxException {
        String bucketName = "name";
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getName()).thenReturn("nome qlqr");

        File file = new File(UUID.randomUUID().toString());
        file.createNewFile();

        InputStream initialStream = new FileInputStream(file);
        when(multipartFile.getInputStream()).thenReturn(initialStream);

        URL url = new URL("http://localhost:3000");
        when(amazonS3.getUrl(anyString(), anyString())).thenReturn(url);

        String uploadFile = s3Service.uploadFile(multipartFile, bucketName);
        Assertions.assertEquals("http://localhost:3000", uploadFile);

        file.delete();
    }

//    @Test
//    @DisplayName("deve listar todos os buckets")
//    public void availableBuckets() {
//        Bucket bucketMock = mock(Bucket.class);
//        String bucketName = "mock name";
//        when(bucketMock.getName()).thenReturn(bucketName);
//        List<Bucket> buckets = Collections.singletonList(bucketMock);
//        when(amazonS3.listBuckets()).thenReturn(buckets);
//
//        List<String> availableBuckets = s3Service.availableBuckets();
//        Assertions.assertEquals(1, availableBuckets.size());
//        Assertions.assertEquals(bucketName, availableBuckets.get(0));
//    }

    @Test
    @DisplayName("deve listar todos os objetos do bucket")
    public void listAllObjectsInBucket() {
        ObjectListing objectListing = mock(ObjectListing.class);
        List<S3ObjectSummary> objetos = new ArrayStack<>();
        S3ObjectSummary umArquivo = mock(S3ObjectSummary.class);
        String KEY_MOCK_NAME = "arquivo.jpg";
        when(umArquivo.getKey()).thenReturn(KEY_MOCK_NAME);

        objetos.add(umArquivo);
        when(objectListing.getObjectSummaries()).thenReturn(objetos);
        when(amazonS3.listObjects(anyString())).thenReturn(objectListing);

        List<String> arquivosEncontrados = s3Service.listAllObjectsInBucket("any");

        boolean contains = arquivosEncontrados.contains(KEY_MOCK_NAME);
        Assertions.assertTrue(contains);
    }

    @Test
    @DisplayName("deve fazer o download do arquivo")
    public void downloadByName() throws Exception {
        File file = new File(UUID.randomUUID().toString());
        file.createNewFile();

        InputStream initialStream = new FileInputStream(file);

        S3Object s3Object = mock(S3Object.class);
        HttpRequestBase httpRequest = mock(HttpRequestBase.class);

        S3ObjectInputStream inputStream = new S3ObjectInputStream(initialStream, httpRequest);
        when(s3Object.getObjectContent()).thenReturn(inputStream);

        when(amazonS3.getObject(anyString(), anyString())).thenReturn(s3Object);

        Resource resource = s3Service.downloadByName(file.getName(), "any");

        Assertions.assertNotNull(resource);

        file.delete();
    }

    @Test
    @DisplayName("deve baixar todos os arquivos do bucket")
    public void backupFiles() throws IOException {
        ObjectListing objectListing = mock(ObjectListing.class);
        List<S3ObjectSummary> objetos = new ArrayStack<>();
        S3ObjectSummary umArquivo = mock(S3ObjectSummary.class);
        String KEY_MOCK_NAME = "arquivo.jpg";
        when(umArquivo.getKey()).thenReturn(KEY_MOCK_NAME);

        objetos.add(umArquivo);
        when(objectListing.getObjectSummaries()).thenReturn(objetos);
        when(amazonS3.listObjects(anyString())).thenReturn(objectListing);

        File file = new File(UUID.randomUUID().toString());
        file.createNewFile();

        InputStream initialStream = new FileInputStream(file);

        S3Object s3Object = mock(S3Object.class);
        HttpRequestBase httpRequest = mock(HttpRequestBase.class);

        S3ObjectInputStream inputStream = new S3ObjectInputStream(initialStream, httpRequest);
        when(s3Object.getObjectContent()).thenReturn(inputStream);

        when(amazonS3.getObject(anyString(), anyString())).thenReturn(s3Object);

        byte[] backupFiles = s3Service.backupFiles("any");
        Assertions.assertNotNull(backupFiles);

        file.delete();

    }
}
