package kg.inai.taskmanager.services;

import org.springframework.web.multipart.MultipartFile;

public interface MinioService {

    String uploadFile(MultipartFile file, String path);

    String getPublicUrl(String filePath);

    void deleteFile(String filePath);
}
