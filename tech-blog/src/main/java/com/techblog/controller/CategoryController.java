package com.techblog.controller;

import com.techblog.dto.ApiResponse;
import com.techblog.dto.CategoryDto;
import com.techblog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
        return new ResponseEntity<>(categoryService.createCategory(categoryDto), HttpStatus.CREATED);
    }
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Integer categoryId) {
        return new ResponseEntity<>(categoryService.getCategory(categoryId),HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories(){
        return new ResponseEntity<>(categoryService.getCategories(),HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Integer categoryId,@Valid @RequestBody CategoryDto categoryDto){
        return new ResponseEntity<>(categoryService.updateCategory(categoryDto,categoryId),HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer categoryId){
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(new ApiResponse("Category deleted successfully",true,String.valueOf(HttpStatus.OK), Instant.now()),HttpStatus.OK);
    }


}
