package ru.nvostrikov.springapi.dto;

import ru.nvostrikov.springapi.entity.Status;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String avatarUri;
    private Status status;
    private String lastStatusChange;
}
