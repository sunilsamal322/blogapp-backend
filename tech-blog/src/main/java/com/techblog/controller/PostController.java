package com.techblog.controller;

import com.techblog.dto.ApiResponse;
import com.techblog.dto.PostDto;
import com.techblog.dto.PostResponse;
import com.techblog.exception.ResourceNotFoundException;
import com.techblog.helper.AppConstants;
import com.techblog.model.Post;
import com.techblog.model.Role;
import com.techblog.model.User;
import com.techblog.repository.PostRepository;
import com.techblog.services.FileServices;
import com.techblog.services.PostServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.util.Set;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostServices postServices;
    @Autowired
    private FileServices fileServices;
    @Autowired
    private PostRepository postRepository;


    @Value("${project.image}")
    private String path;

    @PostMapping("/category/{categoryId}")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto,@PathVariable Integer categoryId)
    {
        return new ResponseEntity<>(postServices.createPost(postDto,getLoginUser().getId(),categoryId), HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Integer id)
    {
        return new ResponseEntity<>(postServices.getPostById(id),HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<PostResponse> getAllPosts(@RequestParam(defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                                                    @RequestParam(defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
                                                    @RequestParam(defaultValue = AppConstants.SORT_BY,required = false) String sortBy,
                                                    @RequestParam(defaultValue = AppConstants.SORT_DIR,required = false) String sortDir
                                                    )
    {
        return new ResponseEntity<>(postServices.getAllPosts(pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@Valid @RequestBody PostDto postDto,@PathVariable Integer id,@RequestParam Integer categoryId)
    {
        Post post= postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("post","id",id));
        if(checkIsRoleAdmin()) {
            return new ResponseEntity<>(postServices.updatePost(postDto, id,categoryId), HttpStatus.OK);
        }
        else if (getLoginUser().getId()==post.getUser().getId())
        {
            return new ResponseEntity<>(postServices.updatePost(postDto, id,categoryId), HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(new ApiResponse("Unauthorized,Access Denied",false,String.valueOf(HttpStatus.UNAUTHORIZED),Instant.now()),HttpStatus.UNAUTHORIZED);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Integer id)
    {
        Post post= postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("post","id",id));
        if(checkIsRoleAdmin())
        {
            postServices.deletePost(id);
            return new ResponseEntity<>(new ApiResponse("post deleted successfully",true,String.valueOf(HttpStatus.OK), Instant.now()),HttpStatus.OK);
        }
        else if (getLoginUser().getId()==post.getUser().getId())
        {
            postServices.deletePost(id);
            return new ResponseEntity<>(new ApiResponse("post deleted successfully",true,String.valueOf(HttpStatus.OK), Instant.now()),HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(new ApiResponse("Unauthorized,Access Denied",false,String.valueOf(HttpStatus.UNAUTHORIZED),Instant.now()),HttpStatus.UNAUTHORIZED);
        }

    }
    @GetMapping("/users/{userId}")
    public ResponseEntity<PostResponse> getPostsByUser(@PathVariable Integer userId,
                                                       @RequestParam(defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                                                       @RequestParam(defaultValue = AppConstants.PAGE_SIZE,required = false)Integer pageSize,
                                                       @RequestParam(defaultValue = AppConstants.SORT_BY,required = false) String sortBy,
                                                       @RequestParam(defaultValue = AppConstants.SORT_DIR,required = false) String sortDir)
    {
        return new ResponseEntity<>(postServices.getPostsByUser(userId,pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);
    }
    @GetMapping("/category/{categoryId}")
    public ResponseEntity <PostResponse> getPostsByCategory(@PathVariable Integer categoryId,
                                                             @RequestParam(defaultValue = AppConstants.PAGE_NUMBER,required = false)Integer pageNumber,
                                                             @RequestParam(defaultValue = AppConstants.PAGE_SIZE,required = false)Integer pageSize,
                                                             @RequestParam(defaultValue = AppConstants.SORT_BY,required = false) String sortBy,
                                                             @RequestParam(defaultValue = AppConstants.SORT_DIR,required = false) String sortDir){
        return new ResponseEntity<>(postServices.getPostsByCategory(categoryId,pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);
    }
    @GetMapping("/mypost")
    public ResponseEntity<PostResponse> getPostsOfLoginUser(@RequestParam(defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                                                            @RequestParam(defaultValue = AppConstants.PAGE_SIZE,required = false)Integer pageSize,
                                                            @RequestParam(defaultValue = AppConstants.SORT_BY,required = false) String sortBy,
                                                            @RequestParam(defaultValue = AppConstants.SORT_DIR,required = false) String sortDir)
    {
        return new ResponseEntity<>(postServices.getPostsByUser(getLoginUser().getId(),pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);
    }

    @PostMapping("/image/upload")
    public ResponseEntity<ApiResponse> imageUpload(@RequestParam Integer postId,@RequestParam MultipartFile image) throws IOException
    {
        Post post= postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("post","id",postId));
        if(post.getUser().getId()==getLoginUser().getId()) {
            String fileName = fileServices.uploadImage(path, image);
            postServices.updateImageName(fileName, postId);
            return new ResponseEntity<>(new ApiResponse("Image uploaded successfully", true, String.valueOf(HttpStatus.OK), Instant.now()), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(new ApiResponse("Unauthorized,Access Denied",false,String.valueOf(HttpStatus.UNAUTHORIZED),Instant.now()),HttpStatus.UNAUTHORIZED);
        }
    }
    @GetMapping(value = "/image/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
    public void getImage(@PathVariable String imageName, HttpServletResponse response) throws FileNotFoundException,IOException
    {
        InputStream resource=fileServices.getImage(path,imageName);
        StreamUtils.copy(resource,response.getOutputStream());
    }
    @GetMapping(value="/image")
    public void getImageByPost(HttpServletResponse response,@RequestParam Integer postId) throws IOException
    {
        Post post= postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("post","id",postId));
        String fileName=post.getPostImageName();
        InputStream resource=fileServices.getImage(path,fileName);
        StreamUtils.copy(resource,response.getOutputStream());
    }
    @DeleteMapping("/image")
    public ResponseEntity<ApiResponse> deleteImage(@RequestParam Integer postId)
    {
        Post post= postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("post","id",postId));
        if(post.getUser().getId()==getLoginUser().getId()) {
            fileServices.deleteImage(path, postId);
            return new ResponseEntity<>(new ApiResponse("Image deleted successfully", true, String.valueOf(HttpStatus.OK), Instant.now()), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new ApiResponse("Unauthorized,Access Denied",false,String.valueOf(HttpStatus.UNAUTHORIZED),Instant.now()),HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostDto>> searchPostByTitle(@RequestParam String title){
        return new ResponseEntity<>(postServices.searchPostByTitle(title),HttpStatus.OK);
    }

    public boolean checkIsRoleAdmin()
    {
        User user=getLoginUser();
        Set<Role> roles=user.getRoles();
        for(Role role:roles)
        {
            if(role.getName().equals("ROLE_ADMIN"))
            {
                return true;
            }
        }
        return false;
    }
    public User getLoginUser()
    {
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }
}
