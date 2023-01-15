package com.techblog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import java.util.Date;

@Getter
@Setter
public class CommentDto {
    private Integer id;
    @NotBlank
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss")
    private Date commentTime;
    private UserDto user;
}
