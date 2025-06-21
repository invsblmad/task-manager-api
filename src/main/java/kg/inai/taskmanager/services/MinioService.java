package kg.inai.taskmanager.services;

import org.springframework.web.multipart.MultipartFile;

public interface MinioService {

    String getPublicUrl(String filePath);

    String save(MultipartFile file, String oldPath, String path);

    void delete(String filePath);
}
