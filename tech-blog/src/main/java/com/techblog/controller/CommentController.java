package com.techblog.controller;

import com.techblog.dto.ApiResponse;
import com.techblog.dto.CommentDto;
import com.techblog.exception.ResourceNotFoundException;
import com.techblog.exception.UserNotFoundException;
import com.techblog.model.Comment;
import com.techblog.model.User;
import com.techblog.repository.CommentRepository;
import com.techblog.repository.UserRepository;
import com.techblog.services.CommentServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentServices commentServices;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;

    @PostMapping
    public ResponseEntity<CommentDto> addComment(@Valid @RequestBody CommentDto commentDto,@RequestParam Integer postId)
    {
        return new ResponseEntity<>(commentServices.addComment(commentDto,postId, getLoginUser().getId()), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(@Valid @RequestBody CommentDto commentDto,@PathVariable Integer id)
    {
        Comment comment=commentRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("comment","id",id));
        if(comment.getPost().getUser().getId()== getLoginUser().getId()) {
            return new ResponseEntity<>(commentServices.updateComment(commentDto, id), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new ApiResponse("Unauthorized,Access Denied",false,String.valueOf(HttpStatus.UNAUTHORIZED),Instant.now()),HttpStatus.UNAUTHORIZED);
        }
    }
    @GetMapping
    public ResponseEntity<List<CommentDto>> getCommentsByPost(@RequestParam Integer postId)
    {
        return new ResponseEntity<>(commentServices.getCommentsByPost(postId),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable Integer id)
    {
        Comment comment=commentRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("comment","id",id));

        if(comment.getUser().getId()==getLoginUser().getId()) {
            commentServices.deleteComment(id);
            return new ResponseEntity<>(new ApiResponse("comment deleted successfully", true, String.valueOf(HttpStatus.OK), Instant.now()), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new ApiResponse("Unauthorized,Access Denied",false,String.valueOf(HttpStatus.UNAUTHORIZED),Instant.now()),HttpStatus.UNAUTHORIZED);
        }
    }
    public User getLoginUser()
    {
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }
}
