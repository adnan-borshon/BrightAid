package com.example.Bright_Aid.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDto {

    private Integer adminId;

    @NotNull(message = "User ID is required")
    private Integer userId;

    private List<String> permissions;

    @Builder.Default
    private Boolean isActive = true;

    private LocalDateTime assignedAt;

    private String adminNotes;

    private List<Integer> verifiedNgoIds;
}