package com.techblog.repository;

import com.techblog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepoitory extends JpaRepository<Post,Integer> {
}
