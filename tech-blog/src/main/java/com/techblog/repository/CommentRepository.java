package com.techblog.repository;

import com.techblog.model.Comment;
import com.techblog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {

    List<Comment> findByPost(Post post);
}
