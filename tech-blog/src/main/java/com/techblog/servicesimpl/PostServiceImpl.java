package com.techblog.servicesimpl;

import com.techblog.dto.PostDto;
import com.techblog.model.Post;
import com.techblog.model.User;
import com.techblog.repository.PostRepoitory;
import com.techblog.repository.UserRepository;
import com.techblog.services.PostServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostServices {

    @Autowired
    private PostRepoitory postRepoitory;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PostDto createPost(PostDto postDto,Integer userId) {
        User user=this.userRepository.findById(userId).get();
        Post post=this.modelMapper.map(postDto,Post.class);
        post.setPostAddedTime(Instant.now());
        post.setPostImageName("default.jpg");
        post.setUser(user);
        this.postRepoitory.save(post);
        return this.modelMapper.map(post,PostDto.class);
    }

    @Override
    public PostDto getPostById(Integer id) {
        Post post=this.postRepoitory.findById(id).get();
        return this.modelMapper.map(post,PostDto.class);
    }

    @Override
    public List<PostDto> getAllPosts() {
        List<Post> posts=this.postRepoitory.findAll();
        List<PostDto> postDtos=posts.stream().map(post->this.modelMapper.map(post,PostDto.class)).collect(Collectors.toList());
        return postDtos;
    }

    @Override
    public PostDto updatePost(PostDto postDto, Integer id) {
        Post post=this.postRepoitory.findById(id).get();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        this.postRepoitory.save(post);
        return this.modelMapper.map(post,PostDto.class);
    }

    @Override
    public void deletePost(Integer id) {
        Post post=this.postRepoitory.findById(id).get();
        this.postRepoitory.delete(post);
    }
}
