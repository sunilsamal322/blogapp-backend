package com.techblog.servicesimpl;

import com.techblog.dto.PostDto;
import com.techblog.dto.PostResponse;
import com.techblog.exception.ResourceNotFoundException;
import com.techblog.helper.AppConstants;
import com.techblog.model.Category;
import com.techblog.model.Post;
import com.techblog.model.User;
import com.techblog.repository.CategoryRepository;
import com.techblog.repository.PostRepository;
import com.techblog.repository.UserRepository;
import com.techblog.services.PostServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PostDto createPost(PostDto postDto, Integer userId,Integer categoryId) {
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("user","id",userId));
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("category","id",categoryId));
        Post post=modelMapper.map(postDto,Post.class);
        post.setPostAddedTime(new Date());
        post.setPostImageName("default.jpg");
        post.setUser(user);
        post.setCategory(category);
        postRepository.save(post);
        return modelMapper.map(post,PostDto.class);
    }

    @Override
    public PostDto getPostById(Integer id) {
        Post post= postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("post","id",id));
        return modelMapper.map(post,PostDto.class);
    }

    @Override
    public PostResponse getAllPosts(Integer pageNumber,Integer pageSize,String sortBy,String sortDir) {
        Sort sort=(sortDir.equalsIgnoreCase("asc"))?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Post> pagePosts= postRepository.findAll(pageable);
        List<Post> posts=pagePosts.getContent();
        List<PostDto> postDtos=posts.stream().map(post->modelMapper.map(post,PostDto.class)).collect(Collectors.toList());

        PostResponse postResponse=new PostResponse();
        postResponse.setContent(postDtos);
        postResponse.setPageSize(pagePosts.getSize());
        postResponse.setPageNumber(pagePosts.getNumber());
        postResponse.setTotalPages(pagePosts.getTotalPages());
        postResponse.setTotalElements(pagePosts.getTotalElements());
        postResponse.setLastPage(pagePosts.isLast());

        return postResponse;
    }

    @Override
    public PostDto updatePost(PostDto postDto, Integer id,Integer categoryId) {
        Post post= postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("post","id",id));
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("category","id",id));
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setCategory(category);
        postRepository.save(post);
        return modelMapper.map(post,PostDto.class);
    }

    @Override
    public PostResponse getPostsByUser(Integer userId, Integer pageNumber, Integer pageSize,String sortBy,String sortDir) {
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("user","id",userId));
        Sort sort=(sortDir.equalsIgnoreCase("asc"))?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        Page<Post> pagePosts= postRepository.findByUser(user,pageable);
        List<Post> posts=pagePosts.getContent();
        List<PostDto> postDtos=posts.stream().map(post->modelMapper.map(post,PostDto.class)).collect(Collectors.toList());

        PostResponse postResponse=new PostResponse();
        postResponse.setContent(postDtos);
        postResponse.setPageSize(pagePosts.getSize());
        postResponse.setPageNumber(pagePosts.getNumber());
        postResponse.setTotalPages(pagePosts.getTotalPages());
        postResponse.setTotalElements(pagePosts.getTotalElements());
        postResponse.setLastPage(pagePosts.isLast());

        return postResponse;
    }
    public PostResponse getPostsByCategory(Integer categoryId,Integer pageNumber,Integer pageSize,String sortBy,String sortDir){
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("category","id",categoryId));
        Sort sort=(sortDir.equalsIgnoreCase("asc"))?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable=PageRequest.of(pageNumber,pageSize,sort);
        Page<Post> pagePosts=postRepository.findByCategory(category,pageable);
        List<Post> posts=pagePosts.getContent();
        List<PostDto> postDtos=posts.stream().map(post->modelMapper.map(post,PostDto.class)).collect(Collectors.toList());

        PostResponse postResponse=new PostResponse();
        postResponse.setContent(postDtos);
        postResponse.setPageSize(pagePosts.getSize());
        postResponse.setPageNumber(pagePosts.getNumber());
        postResponse.setTotalPages(pagePosts.getTotalPages());
        postResponse.setTotalElements(pagePosts.getTotalElements());
        postResponse.setLastPage(pagePosts.isLast());

        return postResponse;
    }

    @Override
    public void deletePost(Integer id) {
        Post post= postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("post","id",id));
        if(!post.getPostImageName().equals(""))
        {
            File file=new File(path+File.separator+post.getPostImageName());
            file.delete();
        }
        postRepository.delete(post);
    }

    @Override
    public List<PostDto> searchPostByTitle(String title) {
        return postRepository.findByTitleLike("%"+title+"%").stream().map((post)->modelMapper.map(post,PostDto.class)).collect(Collectors.toList());
    }

    @Override
    public void updateImageName(String imageName, Integer postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("post", "id", postId));
        if (!post.getPostImageName().equals("")) {
            File file = new File(path + File.separator + post.getPostImageName());
            file.delete();
        }
        post.setPostImageName(imageName);
        postRepository.save(post);
    }
}
