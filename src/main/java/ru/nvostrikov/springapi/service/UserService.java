package ru.nvostrikov.springapi.service;

import ru.nvostrikov.springapi.dto.UserDTO;
import ru.nvostrikov.springapi.entity.Status;
import ru.nvostrikov.springapi.entity.User;
import ru.nvostrikov.springapi.mapper.UserMapper;
import ru.nvostrikov.springapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private static final String UPLOAD_DIR = "uploads";
    private final UserMapper userMapper;

    public String saveAvatar(MultipartFile file) {
        try {
            String filename = UUID.randomUUID() + ".jpg";
            Path dir = Paths.get(UPLOAD_DIR);
            Files.createDirectories(dir);
            Path filepath = dir.resolve(filename);
            file.transferTo(filepath);
            return "/images/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public Long createUser(UserDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setAvatarUri(dto.getAvatarUri());
        user.setStatus(Status.OFFLINE);
        user.setLastStatusChange(Instant.now());
        return repository.save(user).getId();
    }

    public List<UserDTO> getAllUsers() {
        return repository.findAll().stream()
                .map(userMapper::toDTO)
                .toList();
    }

    public UserDTO getUser(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return toDTO(user);
    }

    public Map<String, Object> updateStatus(Long id, Status newStatus) throws InterruptedException {
        User user = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Status oldStatus = user.getStatus();
        Thread.sleep(5000 + new Random().nextInt(5000));
        user.setStatus(newStatus);
        user.setLastStatusChange(Instant.now());
        repository.save(user);
        return Map.of("id", id, "previousStatus", oldStatus, "newStatus", newStatus);
    }

    public Map<String, Object> getStatistics(String statusStr, Long sinceMillis) {
        List<User> users;
        Status status = statusStr != null ? Status.valueOf(statusStr) : null;
        Instant since = sinceMillis != null ? Instant.ofEpochMilli(sinceMillis) : null;

        if (status != null && since != null) {
            users = repository.findByStatusAndLastStatusChangeAfter(status, since);
        } else if (status != null) {
            users = repository.findByStatus(status);
        } else if (since != null) {
            users = repository.findByLastStatusChangeAfter(since);
        } else {
            users = repository.findAll();
        }

        List<Map<String, ? extends Serializable>> userData = users.stream().map(u -> Map.of(
                "id", u.getId(),
                "status", u.getStatus(),
                "avatarUri", u.getAvatarUri()
        )).collect(Collectors.toList());

        return Map.of(
                "requestId", Instant.now().toEpochMilli(),
                "users", userData
        );
    }


    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setAvatarUri(user.getAvatarUri());
        dto.setStatus(user.getStatus());
        return dto;
    }
}

