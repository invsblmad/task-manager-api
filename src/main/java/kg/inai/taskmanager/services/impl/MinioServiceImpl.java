package kg.inai.taskmanager.services.impl;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import kg.inai.taskmanager.configs.properties.MinioProperties;
import kg.inai.taskmanager.exceptions.MinioException;
import kg.inai.taskmanager.services.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private final MinioClient client;
    private final MinioProperties properties;

    @Override
    public String uploadFile(MultipartFile file, String path) {
        String filePath = buildFullPath(file.getOriginalFilename(), path);
        try (InputStream is = file.getInputStream()) {
            client.putObject(
                    PutObjectArgs.builder()
                            .bucket(properties.getBucket())
                            .object(filePath)
                            .stream(is, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return filePath;
        } catch (Exception e) {
            throw new MinioException(String.format("Не удалось загрузить файл, ошибка: %s", e.getMessage()));
        }
    }

    @Override
    public String getPublicUrl(String filePath) {
        return filePath == null ? null : String.format("%s/%s/%s",
                        properties.getUrl(),
                        properties.getBucket(),
                        filePath.replaceAll("^/+", "")
        );
    }

    @Override
    public void deleteFile(String filePath) {
        try {
            client.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(properties.getBucket())
                            .object(filePath)
                            .build()
            );
        } catch (Exception e) {
            throw new MinioException(String.format("Не удалось удалить файл, ошибка: %s", e.getMessage()));
        }
    }

    private String buildFullPath(String originalFileName, String path) {
        String today = LocalDate.now().format(DATE_FORMATTER);
        String fileName = UUID.randomUUID() + getExtension(originalFileName);
        return String.format("%s%s/%s", formatPath(path), today, fileName);
    }

    private String getExtension(String name) {
        int dot = (name == null) ? -1 : name.lastIndexOf('.');
        return (dot >= 0) ? name.substring(dot) : "";
    }

    private String formatPath(String path) {
        if (path == null || path.isBlank()) return "";
        return path.replaceAll("^/+|/+$", "") + '/';
    }

}
