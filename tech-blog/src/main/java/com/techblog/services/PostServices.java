package com.techblog.services;

import com.techblog.dto.PostDto;
import com.techblog.dto.PostResponse;

import java.util.List;

public interface PostServices {

    PostDto createPost(PostDto postDto, Integer userId,Integer categoryId);

    PostDto getPostById(Integer id);

    PostResponse getAllPosts(Integer pageNumber, Integer pageSize,String sortBy,String sortDir);

    PostDto updatePost(PostDto postDto,Integer id,Integer categoryId);

    PostResponse getPostsByUser(Integer userId,Integer pageNumber,Integer pageSize,String sortBy,String sortDir);

    PostResponse getPostsByCategory(Integer categoryId,Integer pageNumber,Integer pageSize,String sortBy,String sortDir);

    void deletePost(Integer id);

    void updateImageName(String imageName,Integer postId);

    List<PostDto> searchPostByTitle(String title);
}
