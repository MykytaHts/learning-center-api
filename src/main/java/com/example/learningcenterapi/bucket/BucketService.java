package com.example.learningcenterapi.bucket;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class BucketService {

    private final Storage storage;
    private final Bucket homeworkBucket;

    public void uploadFile(String key, byte[] content) {
        Blob blob = homeworkBucket.create(key, content);
        System.out.println("File " + key + " uploaded to bucket " + homeworkBucket.getName());
    }

    public String generateTemporaryLinkForReadingFile(String key) {
        URL signedUrl = storage.signUrl(
                BlobInfo.newBuilder(homeworkBucket.getName(), key).build(),
                1, TimeUnit.DAYS
        );
        return signedUrl.toString();
    }

    public void deleteFile(String key) {
        boolean deleted = homeworkBucket.get(key).delete();
        if (deleted) {
            System.out.println("File " + key + " deleted from bucket " + homeworkBucket.getName());
        } else {
            System.out.println("Failed to delete file " + key);
        }
    }
}
