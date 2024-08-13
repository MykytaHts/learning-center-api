package com.example.learningcenterapi.configuration;

import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class GoogleStorageConfig {
    @Value("${gcp.bucket-name}")
    private String bucketName;

    @Bean
    public Bucket getHomeworkBucket(Storage storage) {
        return storage.get(bucketName, Storage.BucketGetOption.fields());
    }

    @Bean
    public Storage getStorage() {
        Storage storage = StorageOptions.getDefaultInstance().getService();
        log.info("Connected to Google Cloud using Application Default Credentials");
        return storage;
    }
}
