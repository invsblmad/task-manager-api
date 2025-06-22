package kg.inai.taskmanager.services.impl;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import kg.inai.taskmanager.configs.properties.MinioProperties;
import kg.inai.taskmanager.exceptions.MinioException;
import kg.inai.taskmanager.services.MinioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private static final int URL_EXPIRATION_SECONDS = 3600;

    private final MinioClient client;
    private final MinioProperties properties;

    @Override
    public String getPublicUrl(String filePath) {
        return filePath == null ? null : String.format("%s/%s/%s",
                properties.getUrl(),
                properties.getBucket(),
                filePath.replaceAll("^/+", "")
        );
    }

    @Override
    public String getDownloadUrl(String filePath, String contentType, String originalFileName) {
        try {
            return client.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(properties.getBucket())
                    .object(filePath)
                    .extraQueryParams(Map.of(
                            "response-content-type", contentType,
                            "response-content-disposition", "attachment; filename=\"" + originalFileName + "\""
                    ))
                    .expiry(URL_EXPIRATION_SECONDS)
                    .build());
        } catch (Exception e) {
            throw new MinioException(String.format("Не удалось получить файл, ошибка: %s", e.getMessage()));
        }
    }

    @Override
    public String save(MultipartFile file, String oldPath, String path) {
        if (oldPath != null) {
            try {
                delete(oldPath);
            } catch (MinioException e) {
                log.warn("Не удалось удалить изображение {}, причина: {}", oldPath, e.getMessage());
            }
        }
        return upload(file, path);
    }

    @Override
    public void delete(String filePath) {
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

    private String upload(MultipartFile file, String path) {
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
