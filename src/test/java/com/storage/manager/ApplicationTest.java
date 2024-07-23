package com.storage.manager;

import com.google.cloud.storage.Storage;
import com.storage.manager.config.GoogleCloudStorageConfiguration;
import com.storage.manager.config.properties.GoogleCloudProperties;
import com.storage.manager.config.properties.GoogleStorageProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class ApplicationTest {

    @MockBean
    private GoogleCloudStorageConfiguration googleCloudStorageConfiguration;
    @MockBean
    private Storage storage;
    @MockBean
    private GoogleCloudProperties googleCloudProperties;
    @MockBean
    private GoogleStorageProperties googleStorageProperties;
}
