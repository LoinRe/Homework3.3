package Homework.H3W3Database.controller;

import Homework.H3W3Database.models.Avatar;
import Homework.H3W3Database.service.AvatarService;
import Homework.H3W3Database.service.AvatarServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("avatar")
public class AvatarController {
    private final AvatarService avatarService;

    public AvatarController(AvatarServiceImpl avatarServiceImpl) {
        this.avatarService = avatarServiceImpl;
    }

    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long id, @RequestParam MultipartFile avatar) throws IOException {
        avatarService.uploadAvatar(id, avatar);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/avatar/preview")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id) {
        Avatar avatar = avatarService.findAvatar(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(avatar.getData());
    }

    @GetMapping(value = "/{id}/avatar")
    public void downloadAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Avatar avatar = avatarService.findAvatar(id);

        Path path = Path.of(avatar.getFilePath());

        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream()) {
            response.setStatus(200);
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            is.transferTo(os);
        }
    }

    @DeleteMapping("/delete/{studentId}")
    public ResponseEntity<Void> deleteAvatar(@PathVariable long studentId) {
        if (avatarService.findAvatar(studentId).getData() != null) {
            avatarService.deleteAvatar(studentId);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    //эндпоинт с пагинацией
    @GetMapping("/paging")
    public ResponseEntity<List<Avatar>> getAllPaging(@RequestParam Integer pageNumber, @RequestParam Integer size) {
        if (pageNumber < 0 || size <= 0) {
            return ResponseEntity.badRequest().build(); // Возвращаем 400 Bad Request в случае некорректных параметров
        }
        List<Avatar> avatars = avatarService.findAll(pageNumber, size);
        if (avatars.isEmpty()) {
            return ResponseEntity.noContent().build(); // Возвращаем 204 No Content, если список пуст
        }
        return ResponseEntity.ok(avatars);
    }

    //эндпоинт для предварительного просмотра с пагинацией
    @GetMapping("/preview-paging")
    public ResponseEntity<byte[]> getPagingPreview(@RequestParam Integer pageNumber,
                                                   @RequestParam(defaultValue = "1") Integer size) {
        // Проверка на отрицательные значения pageNumber и size
        if (pageNumber < 0 || size <= 0) {
            return ResponseEntity.badRequest().build();
        }
        // Получение пагинированного списка аватаров
        Page<Avatar> avatarPreviews = avatarService.findAvatarPreviews(pageNumber, size);
        // Получение предварительного просмотра из первого элемента списка (первого элемента текущей страницы)
        if (avatarPreviews.hasContent()) {
            Avatar preview = avatarPreviews.getContent().get(0);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentLength(preview.getData().length);
            headers.setContentType(MediaType.parseMediaType(preview.getMediaType()));
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(preview.getData());
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}

