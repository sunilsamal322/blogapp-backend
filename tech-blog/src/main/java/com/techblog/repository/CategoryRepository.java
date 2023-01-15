package com.techblog.repository;

import com.techblog.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CategoryRepository extends JpaRepository<Category,Integer> {

}
