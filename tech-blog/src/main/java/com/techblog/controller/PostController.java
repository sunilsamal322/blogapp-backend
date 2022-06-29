package com.techblog.controller;

import com.techblog.dto.ApiResponse;
import com.techblog.dto.PostDto;
import com.techblog.exception.ResourceNotFoundException;
import com.techblog.helper.AppConstants;
import com.techblog.repository.PostRepoitory;
import com.techblog.repository.UserRepository;
import com.techblog.services.FileServices;
import com.techblog.services.PostServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostServices postServices;
    @Autowired
    private FileServices fileServices;
    @Autowired
    private PostRepoitory postRepoitory;

    @Autowired
    private UserRepository userRepository;



    @Value("${project.image}")
    private String path;

    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto, @RequestParam Integer userId)
    {
        return new ResponseEntity<>(postServices.createPost(postDto,userId), HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Integer id)
    {
        return new ResponseEntity<>(postServices.getPostById(id),HttpStatus.OK);
    }
    @GetMapping("/")
    public ResponseEntity<List<PostDto>> getAllPosts(@RequestParam(defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                                                     @RequestParam(defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize)
    {
        return new ResponseEntity<>(postServices.getAllPosts(pageNumber,pageSize),HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto,@PathVariable Integer id)
    {
        return new ResponseEntity<>(postServices.updatePost(postDto,id),HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Integer id)
    {
        postServices.deletePost(id);
        return new ResponseEntity<>(new ApiResponse("post deleted successfully",true,String.valueOf(HttpStatus.OK), Instant.now()),HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<PostDto>> getPostsByUser(@RequestParam Integer userId,
                                                        @RequestParam(defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                                                        @RequestParam(defaultValue = AppConstants.PAGE_SIZE,required = false)Integer pageSize)
    {
        return new ResponseEntity<>(postServices.getPostsByUser(userId,pageNumber,pageSize),HttpStatus.OK);
    }
    @PostMapping("/image/upload")
    public ResponseEntity<ApiResponse> imageUpload(@RequestParam Integer postId,@RequestParam MultipartFile image) throws IOException
    {
        postRepoitory.findById(postId).orElseThrow(()->new ResourceNotFoundException("post","id",postId));
        String fileName=fileServices.uploadImage(path,image);
        postServices.updateImageName(fileName,postId);
        return new ResponseEntity<>(new ApiResponse("Image uploaded successfully",true,String.valueOf(HttpStatus.OK),Instant.now()),HttpStatus.OK);
    }
    @GetMapping(value = "/image/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
    public void getImage(@PathVariable String imageName, HttpServletResponse response) throws FileNotFoundException,IOException
    {
        InputStream resource=fileServices.getImage(path,imageName);
        StreamUtils.copy(resource,response.getOutputStream());
    }
    @DeleteMapping("/image")
    public ResponseEntity<ApiResponse> deleteImage(@RequestParam Integer postId)
    {
        fileServices.deleteImage(path,postId);
        return new ResponseEntity<>(new ApiResponse("Image deleted successfully",true,String.valueOf(HttpStatus.OK),Instant.now()),HttpStatus.OK);
    }
}
