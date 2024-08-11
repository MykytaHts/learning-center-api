package com.example.learningcenterapi.configuration;

import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class GoogleStorageConfig {


    private final String homeworkBucketName = "bucketerria";

    @Bean
    public Bucket getHomeworkBucket(final Storage storage) {
        return storage.get(homeworkBucketName, Storage.BucketGetOption.fields());
    }

    @Bean
    public Storage getStorage() {
        Storage storage = StorageOptions.getDefaultInstance().getService();
        log.info("Connected to Google Cloud using Application Default Credentials");
        return storage;
    }
}
