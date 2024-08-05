package com.example.learningcenterapi.repository.integrations;

import com.example.learningcenterapi.domain.enumeration.FileUploadStatus;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageRepository {
    /**
     * Uploads a file to the file storage repository with the specified key.
     *
     * @param file the file to be uploaded
     * @param key  the key to associate with the file
     * @return the status of the file upload operation (SUCCESS or FAILED)
     */
    FileUploadStatus uploadFile(MultipartFile file, String key);

    /**
     * Generates a temporary link for reading a file.
     *
     * @param key the key associated with the file
     * @return a temporary link to read the file
     */
    String getFileAccessLink(String key);

    /**
     * Downloads a file from the file storage repository based on the given key.
     *
     * @param key the key of the file to be downloaded
     * @return a byte array representing the downloaded file content
     */
    byte[] downloadFile(String key);

    /**
     * Deletes a file with the specified key.
     *
     * @param key the key of the file to delete
     */
    void deleteFile(String key);
}
