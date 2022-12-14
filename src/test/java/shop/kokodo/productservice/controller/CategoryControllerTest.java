package shop.kokodo.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import shop.kokodo.productservice.entity.Category;
import shop.kokodo.productservice.repository.CategoryRepository;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
class CategoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CategoryRepository categoryRepository;

    Category category1;
    Category category2;

    @BeforeEach
    public void setUp() {
        category1 = Category.builder().id(1).name("test1").build();
        category2 = Category.builder().id(2).name("test2").build();

        categoryRepository.save(category1);
        categoryRepository.save(category2);
    }

    @Test
    @DisplayName("???????????? ?????? ??????")
    void all() throws Exception {

        this.mockMvc.perform(get("/category/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andDo(document("category-rest-controller/category-all",
                            responseFields(
                                    fieldWithPath("[]").type(JsonFieldType.ARRAY).description("???????????? ??????"),
                                    fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("???????????? id"),
                                    fieldWithPath("[].name").type(JsonFieldType.STRING).description("???????????? ??????")
                            ))
                        );
    }

    @Test
    @DisplayName("???????????? ???????????? ??????")
    void findByName() throws Exception{

        this.mockMvc.perform(get("/category/categoryName/{name}", category1.getName())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(document("category-rest-controller/category-name",
                        pathParameters(
                                parameterWithName("name").description("???????????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("????????????"),
                                fieldWithPath("result.data[]").type(JsonFieldType.ARRAY).description("???????????? ??????"),
                                fieldWithPath("result.data[].id").type(JsonFieldType.NUMBER).description("???????????? id"),
                                fieldWithPath("result.data[].name").type(JsonFieldType.STRING).description("???????????? ??????")
                        )
                    )
                );
        }
}