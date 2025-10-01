package com.example.Bright_Aid.Dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectUpdateDto {

    private Integer updateId;
    private Integer projectId;
    private String updateTitle;
    private String updateDescription;
    private BigDecimal progressPercentage;
    private BigDecimal amountUtilized;
    private List<String> imagesUrls;
}
