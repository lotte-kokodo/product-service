package shop.kokodo.productservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.kokodo.productservice.dto.CategoryDto;
import shop.kokodo.productservice.entity.Category;
import shop.kokodo.productservice.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @InjectMocks
    CategoryServiceImpl categoryServiceImpl;

    @Mock
    CategoryRepository categoryRepository;

    Category category;

    List<Category> categoryList;

    @BeforeEach
    void setUp() {
        category = new Category(1L, "one");
        categoryList = new ArrayList<>();
        categoryList.add(category);
    }

    @Test
    @DisplayName("카테고리 전체 조회")
    void findAll() {
        //given
        doReturn(categoryList).when(categoryRepository).findAll();

        //when
        final List<CategoryDto> test = categoryServiceImpl.findAll();

        //then
        assertThat(test.size()).isEqualTo(categoryList.size());

    }

    @Test
    @DisplayName("카테고리 이름으로 검색")
    void findByName() {
        //given
        doReturn(categoryList).when(categoryRepository).findByName("o");

        //when
        final List<CategoryDto> test = categoryServiceImpl.findByName("o");

        //then
        assertThat(test.size()).isEqualTo(categoryList.size());
    }
}