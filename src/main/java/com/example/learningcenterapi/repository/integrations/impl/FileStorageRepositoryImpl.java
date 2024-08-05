package com.example.learningcenterapi.repository.integrations.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FileStorageRepositoryImpl{
//    private final Bucket homeworkBucket;
//    private final Storage storage;
//    @Value("${google.cloud.credentials-json-path}")
//    private String credentialsJsonPath;
//
//    @Override
//    public FileUploadStatus uploadFile(MultipartFile file, String key) {
//        try {
//            homeworkBucket.create(key, file.getInputStream());
//            return SUCCESS;
//        } catch (IOException e) {
//            return FAILED;
//        }
//    }
//
//    @Override
//    public String getFileAccessLink(String key) {
//        try {
//            final URL signedUrl = storage.signUrl(BlobInfo.newBuilder(homeworkBucket.getName(), key).build(),
//                    1, TimeUnit.DAYS, Storage.SignUrlOption.signWith(ServiceAccountCredentials
//                            .fromStream(new ClassPathResource(credentialsJsonPath).getInputStream())));
//            return signedUrl.toString();
//        } catch (final IOException e) {
//            throw new SystemException(e.getMessage(), INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @Override
//    public byte[] downloadFile(String key) {
//        return homeworkBucket.get(key).getContent();
//    }
//
//
//    @Override
//    public void deleteFile(String key) {
//        try {
//            storage.delete(BlobId.of(homeworkBucket.getName(), key));
//        } catch (RuntimeException e) {
//            throw new SystemException("File wasn't deleted.", INTERNAL_SERVER_ERROR);
//        }
//    }
}
