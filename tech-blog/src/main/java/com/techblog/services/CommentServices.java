package com.techblog.services;

import com.techblog.dto.CommentDto;

import java.util.List;

public interface CommentServices {

    CommentDto addComment(CommentDto commentDto,Integer postId,Integer userId);

    CommentDto updateComment(CommentDto commentDto,Integer id);

    List<CommentDto> getCommentsByPost(Integer postId);
    void deleteComment(Integer id);
}
