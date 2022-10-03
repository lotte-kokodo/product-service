package shop.kokodo.productservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.kokodo.productservice.dto.CategoryDto;
import shop.kokodo.productservice.entity.Category;
import shop.kokodo.productservice.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;

    List<CategoryDto> categoryDtoList = new ArrayList<>();

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // 임시 저장(나중에 삭제 예정)
    @Transactional
    @Override
    public void save(CategoryDto categoryDto) {
        Category category = Category.builder().id(categoryDto.getId()).name(categoryDto.getName()).build();
        categoryRepository.save(category);
    }


    @Override
    public List<CategoryDto> findAll() {
        List<Category> categoryList = categoryRepository.findAll();

        for(Category c : categoryList){
            CategoryDto categoryDto = CategoryDto.builder().id(c.getId()).name(c.getName()).build();
            categoryDtoList.add(categoryDto);
        }

        return categoryDtoList;
    }

    @Override
    public List<CategoryDto> findByName(String name) {
        List<Category> categoryList = categoryRepository.findByName(name);

        for(Category c : categoryList){
            CategoryDto categoryDto = CategoryDto.builder().id(c.getId()).name(c.getName()).build();
            categoryDtoList.add(categoryDto);
        }

        return categoryDtoList;
    }
}
