package com.techblog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostDto {

    private Integer id;
    @NotBlank
    @Size(max = 500,message = "title length can be maximum of 500 characters")
    private String title;
    @NotBlank
    @Size(max = 20000,message = "content length can be maximum of 20000 characters")
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss")
    private Date postAddedTime;
    private String postImageName;
    private List<CommentDto> comments=new ArrayList<>();
    private UserDto user;
}
