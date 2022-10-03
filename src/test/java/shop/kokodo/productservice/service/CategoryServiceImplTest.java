package shop.kokodo.productservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.kokodo.productservice.entity.Category;
import shop.kokodo.productservice.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
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
    void findAll() {
        doReturn(categoryList).when(categoryRepository).findAll();
        categoryServiceImpl.findAll();
    }

    @Test
    void findByName() {
        doReturn(categoryList).when(categoryRepository).findByName("o");
        categoryServiceImpl.findByName("o");
    }
}