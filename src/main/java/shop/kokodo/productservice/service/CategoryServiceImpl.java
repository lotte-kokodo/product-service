package shop.kokodo.productservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.kokodo.productservice.dto.CategoryDto;
import shop.kokodo.productservice.entity.Category;
import shop.kokodo.productservice.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDto> findAll() {
        List<Category> categoryList = categoryRepository.findAll();
        List<CategoryDto> categoryDtoList = new ArrayList<>();

        for(Category c : categoryList){
            CategoryDto categoryDto = CategoryDto.builder().id(c.getId()).name(c.getName()).build();
            categoryDtoList.add(categoryDto);
        }

        return categoryDtoList;
    }

    @Override
    public List<CategoryDto> findByName(String name) {
        List<Category> categoryList = categoryRepository.findByName(name);
        List<CategoryDto> categoryDtoList = new ArrayList<>();

        for(Category c : categoryList){
            CategoryDto categoryDto = CategoryDto.builder().id(c.getId()).name(c.getName()).build();
            categoryDtoList.add(categoryDto);
        }

        return categoryDtoList;
    }
}
