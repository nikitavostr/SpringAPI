package ru.nvostrikov.springapi.controller;

import ru.nvostrikov.springapi.dto.UserDTO;
import ru.nvostrikov.springapi.entity.Status;
import ru.nvostrikov.springapi.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/upload")
    public Map<String, String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        String uri = userService.saveAvatar(file);
        return Map.of("uri", uri);
    }

    @PostMapping("/users")
    public Map<String, Long> createUser(@RequestBody UserDTO dto) {
        Long id = userService.createUser(dto);
        return Map.of("id", id);
    }

    @GetMapping("/users")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public UserDTO getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PutMapping("/users/{id}/status")
    public Map<String, Object> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> req) throws InterruptedException {
        String newStatus = req.get("status");
        return userService.updateStatus(id, Status.valueOf(newStatus));
    }

    @GetMapping("/statistics")
    public Map<String, Object> getStatistics(@RequestParam(required = false) String status,
                                             @RequestParam(required = false) Long since) {
        return userService.getStatistics(status, since);
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleNotFound(EntityNotFoundException e) {
        return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
    }
}

