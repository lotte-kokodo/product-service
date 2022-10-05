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
import shop.kokodo.productservice.dto.ReviewRequestDto;
import shop.kokodo.productservice.entity.Category;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.entity.Review;
import shop.kokodo.productservice.repository.ProductRepository;
import shop.kokodo.productservice.repository.ReviewRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
public class ReviewRestControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ProductRepository productRepository;
    @Autowired
    ReviewRepository reviewRepository;

    Category category;
    Product product;
    Review review1;
    Review review2;
    ReviewRequestDto reviewDto;
    final LocalDateTime localDateTime = LocalDateTime.of(2022,10,25,0,0);
    final long memberId=1L;
    @BeforeEach
    public void setUp(){
        category = Category.builder()
                .name("healthy")
                .build();

        product = Product.builder()
                .category(category)
                .name("맛닭")
                .price(1000)
                .displayName("맛닭")
                .stock(10)
                .deadline(localDateTime)
                .thumbnail("맛닭")
                .sellerId(1)
                .deliveryFee(1000)
                .build();

        review1 = Review.builder()
                .content("리뷰1")
                .rating(5.0)
                .product(product)
                .memberId(memberId)
                .build();

        review2 = Review.builder()
                .content("리뷰2")
                .rating(4.5)
                .product(product)
                .memberId(memberId)
                .build();

        productRepository.save(product);

        reviewDto= ReviewRequestDto.builder()
                .productId(product.getId())
                .content("review content")
                .rating(4.5)
                .build();
    }

    @Test
    @DisplayName("상품의 리뷰 조회")
    public void findByProductId() throws Exception {

        reviewRepository.save(review1);
        reviewRepository.save(review2);

        this.mockMvc.perform(get("/review/{productId}",product.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("review-rest-controller/find-review",
                        pathParameters(
                                parameterWithName("productId").description("상품 id")
                        ),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태코드"),
                                fieldWithPath("result").description("응답데이터"),
                                fieldWithPath("result.data[]").type(JsonFieldType.ARRAY).description("리뷰 리스트"),
                                fieldWithPath("result.data[].createdDate").type(JsonFieldType.STRING).description("리뷰 생성 날짜").optional(),
                                fieldWithPath("result.data[].lastModifiedDate").type(JsonFieldType.STRING).description("리뷰 마지막 수정 날짜").optional(),
                                fieldWithPath("result.data[].id").type(JsonFieldType.NUMBER).description("리뷰 id"),
                                fieldWithPath("result.data[].content").type(JsonFieldType.STRING).description("리뷰 내용"),
                                fieldWithPath("result.data[].rating").type(JsonFieldType.NUMBER).description("리뷰 평점"),
                                fieldWithPath("result.data[].memberName").type(JsonFieldType.STRING).description("리뷰 멤버 닉네임")
                        )
                )
                );
    }



    @Test
    @DisplayName("상품의 리뷰 생성")
    public void save() throws Exception {

        reviewRepository.save(review1);
        reviewRepository.save(review2);

        this.mockMvc.perform(post("/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDto))
                        .header("memberId", memberId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("review-rest-controller/save",
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("리뷰 id").optional(),
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("멤버 ID"),
                                fieldWithPath("productId").type(JsonFieldType.NUMBER).description("상품 ID"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("리뷰 내용"),
                                fieldWithPath("rating").type(JsonFieldType.NUMBER).description("리뷰 평점")
                        ),
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태코드"),
                                        fieldWithPath("result.data.id").type(JsonFieldType.NUMBER).description("리뷰 리스트"),
                                        fieldWithPath("result.data.rating").type(JsonFieldType.NUMBER).description("리뷰 생성 날짜").optional(),
                                        fieldWithPath("result.data.content").type(JsonFieldType.STRING).description("리뷰 마지막 수정 날짜").optional(),
                                        fieldWithPath("result.data.productId").type(JsonFieldType.NUMBER).description("리뷰 id"),
                                        fieldWithPath("result.data.memberId").type(JsonFieldType.NUMBER).description("리뷰 내용")
                                )
                        )
                );
    }
}
