package com.storage.manager.gcstorage;

public record GoogleCloudResourceOuput(
        String contentType,
        byte[] content
) {
}
