package com.example.learningcenterapi.service.impl;

import com.example.learningcenterapi.service.BucketService;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class BucketServiceImpl implements BucketService  {
    private final Storage storage;
    private final Bucket homeworkBucket;

    public void uploadFile(String key, byte[] content) throws IOException {
        log.info("Starting upload file with key: " + key);
        try {
            homeworkBucket.create(key, content);
        } catch (StorageException e) {
            log.error("Error during upload file with key: " + key, e);
            throw new IOException(e);
        }
        log.info("Successful upload file with key: " + key);
    }

    public String generateTemporaryLinkForReadingFile(String key) throws IOException {
        log.info("Generating temporary link for file with key: " + key);
        try {
            URL signedUrl = storage.signUrl(
                    BlobInfo.newBuilder(homeworkBucket.getName(), key).build(),
                    1, TimeUnit.DAYS
            );
            return signedUrl.toString();
        } catch (StorageException e) {
            log.error("Error generating temporary link for file with key: " + key, e);
            throw new IOException(e);
        }
    }

    public byte[] downloadFile(String key) throws IOException {
        log.info("Downloading file with key: " + key);
        var blob = homeworkBucket.get(key);
        if(blob != null) {
            return blob.getContent();
        } else {
            log.error("File with key: " + key + " does not exist");
            throw new IOException("The specified key does not exist in the bucket");
        }
    }

    public void deleteFile(String key) throws IOException {
        log.info("Deleting file with key: " + key);
        var blob = homeworkBucket.get(key);
        if(blob != null && !blob.delete()) {
            log.error("Failed to delete file with key: " + key);
            throw new IOException("Failed to delete file with key: " + key);
        }
        log.info("Successfully deleted file with key: " + key);
    }
}
