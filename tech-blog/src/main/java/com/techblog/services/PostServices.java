package com.techblog.services;

import com.techblog.dto.PostDto;
import com.techblog.model.User;

import java.util.List;

public interface PostServices {

    PostDto createPost(PostDto postDto, Integer userId);

    PostDto getPostById(Integer id);

    List<PostDto> getAllPosts();

    PostDto updatePost(PostDto postDto,Integer id);

    List<PostDto> getPostsByUser(Integer userId);

    void deletePost(Integer id);

    void updateImageName(String imageName,Integer postId);
}
