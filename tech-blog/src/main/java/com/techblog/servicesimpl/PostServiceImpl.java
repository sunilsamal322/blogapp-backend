package com.techblog.servicesimpl;

import com.techblog.dto.PostDto;
import com.techblog.exception.ResourceNotFoundException;
import com.techblog.model.Post;
import com.techblog.model.User;
import com.techblog.repository.PostRepoitory;
import com.techblog.repository.UserRepository;
import com.techblog.services.FileServices;
import com.techblog.services.PostServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostServices {

    @Value("${project.image}")
    private String path;
    @Autowired
    private PostRepoitory postRepoitory;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FileServices fileServices;

    @Override
    public PostDto createPost(PostDto postDto, Integer userId) {
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("user","id",userId));
        Post post=modelMapper.map(postDto,Post.class);
        post.setPostAddedTime(new Date());
        post.setPostImageName("");
        post.setUser(user);
        postRepoitory.save(post);
        return modelMapper.map(post,PostDto.class);
    }

    @Override
    public PostDto getPostById(Integer id) {
        Post post=postRepoitory.findById(id).orElseThrow(()->new ResourceNotFoundException("post","id",id));
        return modelMapper.map(post,PostDto.class);
    }

    @Override
    public List<PostDto> getAllPosts(Integer pageNumber,Integer pageSize) {
        Pageable pageable= PageRequest.of(pageNumber,pageSize);
        Page<Post> pagePosts=postRepoitory.findAll(pageable);
        List<Post> posts=pagePosts.getContent();
        List<PostDto> postDtos=posts.stream().map(post->modelMapper.map(post,PostDto.class)).collect(Collectors.toList());
        return postDtos;
    }

    @Override
    public PostDto updatePost(PostDto postDto, Integer id) {
        Post post=postRepoitory.findById(id).orElseThrow(()->new ResourceNotFoundException("post","id",id));
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        postRepoitory.save(post);
        return modelMapper.map(post,PostDto.class);
    }

    @Override
    public List<PostDto> getPostsByUser(Integer userId, Integer pageNumber, Integer pageSize) {
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("user","id",userId));
        Pageable pageable= PageRequest.of(pageNumber,pageSize);
        Page<Post> pagePosts=postRepoitory.findByUser(user,pageable);
        List<Post> posts=pagePosts.getContent();
        List<PostDto> postDtos=posts.stream().map(post->modelMapper.map(post,PostDto.class)).collect(Collectors.toList());
        return postDtos;
    }

    @Override
    public void deletePost(Integer id) {
        Post post=postRepoitory.findById(id).orElseThrow(()->new ResourceNotFoundException("post","id",id));
        postRepoitory.delete(post);
    }

    @Override
    public void updateImageName(String imageName, Integer postId) {
        Post post = postRepoitory.findById(postId).orElseThrow(() -> new ResourceNotFoundException("post", "id", postId));
        if(!post.getPostImageName().equals(""))
        {
            File file=new File(path+File.separator+post.getPostImageName());
            file.delete();
        }
        post.setPostImageName(imageName);
        postRepoitory.save(post);
    }
}
