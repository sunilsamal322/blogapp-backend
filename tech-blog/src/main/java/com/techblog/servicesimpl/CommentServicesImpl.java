package com.techblog.servicesimpl;

import com.techblog.dto.CommentDto;
import com.techblog.exception.ResourceNotFoundException;
import com.techblog.model.Comment;
import com.techblog.model.Post;
import com.techblog.model.User;
import com.techblog.repository.CommentRepository;
import com.techblog.repository.PostRepoitory;
import com.techblog.repository.UserRepository;
import com.techblog.services.CommentServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServicesImpl implements CommentServices {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepoitory postRepoitory;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CommentDto addComment(CommentDto commentDto, Integer postId, Integer userId) {

        Post post=postRepoitory.findById(postId).orElseThrow(()->new ResourceNotFoundException("post","id",postId));
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("user","id",userId));
        Comment comment=modelMapper.map(commentDto,Comment.class);
        comment.setPost(post);
        comment.setUser(user);
        comment.setCommentTime(new Date());
        Comment addedComment=commentRepository.save(comment);
        return modelMapper.map(addedComment,CommentDto.class);
    }

    @Override
    public CommentDto updateComment(CommentDto commentDto,Integer id) {
        Comment comment=commentRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("comment","id",id));
        comment.setContent(commentDto.getContent());
        Comment updatedComment=commentRepository.save(comment);
        return modelMapper.map(updatedComment,CommentDto.class);
    }

    @Override
    public List<CommentDto> getCommentsByPost(Integer postId) {
        Post post=postRepoitory.findById(postId).orElseThrow(()->new ResourceNotFoundException("post","id",postId));
        List<Comment> comments=commentRepository.findByPost(post);
        List<CommentDto> commentDtos=comments.stream().map((comment)->modelMapper.map(comment,CommentDto.class)).collect(Collectors.toList());
        return commentDtos;
    }

    @Override
    public void deleteComment(Integer id) {
        Comment comment=commentRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("comment","id",id));
        commentRepository.delete(comment);
    }
}
