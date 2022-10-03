package shop.kokodo.productservice.controller;

import com.google.gson.Gson;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import shop.kokodo.productservice.dto.CategoryDto;
import shop.kokodo.productservice.entity.Category;
import shop.kokodo.productservice.repository.CategoryRepository;
import shop.kokodo.productservice.service.CategoryService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    private MockMvc mockMvc;

    Category category1;
    Category category2;

    @BeforeEach
    public void init() {
        category1 = Category.builder().id(1).name("건강식").build();
        category2 = Category.builder().id(2).name("닭가슴살").build();
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @DisplayName("카테고리 목록 조회 성공")
    @Test
    void all() throws Exception {
//        //given
//        categoryRepository.save(category1);
//        categoryRepository.save(category2);
//
//        String url = "http://localhost:9270/category/all";
//
//        //when
//        ResponseEntity<Category[]> responseEntity = restTemplate.getForEntity(url, Category[].class);
//        List<Category> list = Arrays.asList(responseEntity.getBody());
//
//        //then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        mockMvc.perform(get("/category/all")).andExpect(status().isOk());
    }

    @DisplayName("이름으로 카테고리 검색 성공")
    @Test
    void findByName() throws Exception {
        mockMvc.perform(get("/category/categoryName/gon")).andExpect(status().isOk());
    }
}