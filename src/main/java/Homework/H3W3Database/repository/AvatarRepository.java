package Homework.H3W3Database.repository;

import Homework.H3W3Database.models.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {

    Optional<Avatar> findByStudentId(Long studentId);

    void deleteByStudentId(Long studentId);
}
