package shop.kokodo.productservice.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shop.kokodo.productservice.entity.Category;

import javax.transaction.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;
    Category category;
    Category category1;

    @BeforeEach
    public void setUp() {
        category = Category.builder()
                .name("healthy")
                .build();

        category1 = Category.builder()
                .name("power")
                .build();
    }

    @Test
    @DisplayName("카테고리 이름으로 카테고리 검색")
    void findById() {
        //given
        categoryRepository.save(category);
        categoryRepository.save(category1);

        //when
        List<Category> returnCategory = categoryRepository.findByName("eal");

        //then
        Assertions.assertEquals(returnCategory.get(0).getName(),"healthy");
    }



}