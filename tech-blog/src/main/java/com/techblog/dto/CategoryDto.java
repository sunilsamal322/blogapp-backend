package com.techblog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    private Integer categoryId;
    @NotBlank
    @Size(min = 3,message = "category title should be minimum of 3 characters")
    private String categoryTitle;
    @Size(min=10,message = "category description should be minimum of 10 characters")
    private String categoryDescription;
}
