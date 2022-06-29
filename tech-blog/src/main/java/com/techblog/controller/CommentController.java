package com.techblog.controller;

import com.techblog.dto.ApiResponse;
import com.techblog.dto.CommentDto;
import com.techblog.services.CommentServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentServices commentServices;

    @PostMapping
    public ResponseEntity<CommentDto> addComment(@Valid @RequestBody CommentDto commentDto,@RequestParam Integer postId,@RequestParam Integer userId)
    {
        return new ResponseEntity<>(commentServices.addComment(commentDto,postId,userId), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<CommentDto> updateComment(@Valid @RequestBody CommentDto commentDto,@PathVariable Integer id)
    {
        return new ResponseEntity<>(commentServices.updateComment(commentDto,id),HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<CommentDto>> getCommentsByPost(@RequestParam Integer postId)
    {
        return new ResponseEntity<>(commentServices.getCommentsByPost(postId),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable Integer id)
    {
        commentServices.deleteComment(id);
        return new ResponseEntity<>(new ApiResponse("comment deleted successfully",true,String.valueOf(HttpStatus.OK), Instant.now()),HttpStatus.OK);
    }
}
