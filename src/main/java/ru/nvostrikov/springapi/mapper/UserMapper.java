package ru.nvostrikov.springapi.mapper;

import org.springframework.stereotype.Component;
import ru.nvostrikov.springapi.dto.UserDTO;
import ru.nvostrikov.springapi.entity.User;

@Component
public class UserMapper {
    public UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setAvatarUri(user.getAvatarUri());
        dto.setStatus(user.getStatus());
        return dto;
    }
}