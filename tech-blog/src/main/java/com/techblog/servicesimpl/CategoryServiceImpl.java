package com.techblog.servicesimpl;

import com.techblog.dto.CategoryDto;
import com.techblog.exception.ResourceNotFoundException;
import com.techblog.model.Category;
import com.techblog.repository.CategoryRepository;
import com.techblog.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category=modelMapper.map(categoryDto,Category.class);
        return modelMapper.map(categoryRepository.save(category),CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("category","id",categoryId));
        category.setCategoryTitle((categoryDto.getCategoryTitle()));
        category.setCategoryDescription(categoryDto.getCategoryDescription());
        return modelMapper.map(categoryRepository.save(category),CategoryDto.class);

    }

    @Override
    public void deleteCategory(Integer categoryId) {
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("category","id",categoryId));
        categoryRepository.delete(category);
    }

    @Override
    public CategoryDto getCategory(Integer categoryId) {
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("category","id",categoryId));
        return modelMapper.map(category,CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getCategories() {
        List<CategoryDto> categoryDtoList=new ArrayList<>();
        categoryRepository.findAll().forEach((category -> {
            categoryDtoList.add(modelMapper.map(category,CategoryDto.class));
        }));
        return categoryDtoList;
    }
}
