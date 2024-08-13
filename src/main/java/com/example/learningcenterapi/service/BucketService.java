package com.example.learningcenterapi.service;

import java.io.IOException;

/**
 * Interface for managing files in a Google Cloud Storage bucket.
 * Provides methods for uploading, downloading, deleting files, and generating temporary access links.
 */
public interface BucketService {
    /**
     * Uploads a file to the Google Cloud Storage bucket.
     * The file is stored under the specified key.
     *
     * @param fileKey  The path of the file to be stored.
     * @param content   The content of the file as a byte array.
     * @throws IOException If an error occurs during the file upload process.
     */
    void uploadFile(String fileKey, byte[] content) throws IOException;

    /**
     * Generates a temporary URL for accessing a file stored in the Google Cloud Storage bucket.
     * The link is valid for a specified duration and allows the file to be accessed without authentication.
     *
     * @param fileKey  The path of the file for which the link is being generated.
     * @return A temporary URL that provides access to the specified file.
     * @throws IOException If an error occurs while generating the temporary link.
     */
    String generateTemporaryLinkForReadingFile(String fileKey) throws IOException;

    /**
     * Downloads a file from the Google Cloud Storage bucket.
     * The file is retrieved based on the specified key and returned as a byte array.
     *
     * @param fileKey  The path of the file to be downloaded.
     * @return The content of the file as a byte array.
     * @throws IOException If an error occurs during the file download process or if the file does not exist.
     */
    byte[] downloadFile(String fileKey) throws IOException;

    /**
     * Deletes a file from the Google Cloud Storage bucket.
     * The file is identified by the specified key and removed from the storage.
     *
     * @param fileKey  The path of the file to be deleted.
     * @throws IOException If an error occurs during the file deletion process or if the file does not exist.
     */
    void deleteFile(String fileKey) throws IOException;
}
