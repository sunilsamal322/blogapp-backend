package com.techblog.services;

import com.techblog.dto.PostDto;

import java.util.List;

public interface PostServices {

    PostDto createPost(PostDto postDto,Integer userId);

    PostDto getPostById(Integer id);

    List<PostDto> getAllPosts();

    PostDto updatePost(PostDto postDto,Integer id);

    void deletePost(Integer id);
}
