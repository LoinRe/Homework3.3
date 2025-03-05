package Homework.H3W3Database.service;

import Homework.H3W3Database.models.Avatar;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AvatarService {
    void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException;

    Avatar findAvatar(Long studentId);

    // Новые методы для пагинации
    List<Avatar> findAll(Integer pageNumber, Integer size);
    Page<Avatar> findAvatarPreviews(Integer pageNumber, Integer size);
    void deleteAvatar(Long studentId); // Добавляем для соответствия контроллеру
}
