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
import org.springframework.test.context.ActiveProfiles;
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
@ActiveProfiles("test")
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
                .name("??????")
                .price(1000)
                .displayName("??????")
                .stock(10)
                .deadline(localDateTime)
                .thumbnail("??????")
                .sellerId(1)
                .deliveryFee(1000)
                .build();

        review1 = Review.builder()
                .content("??????1")
                .rating(5.0)
                .product(product)
                .memberId(memberId)
                .build();

        review2 = Review.builder()
                .content("??????2")
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
    @DisplayName("????????? ?????? ??????")
    public void findByProductId() throws Exception {

        reviewRepository.save(review1);
        reviewRepository.save(review2);

        this.mockMvc.perform(get("/review/{productId}",product.getId())
                        .param("page","1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("review-rest-controller/find-review",
                        pathParameters(
                                parameterWithName("productId").description("?????? id")
                        ),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("????????????"),
                                fieldWithPath("result").description("???????????????"),
                                fieldWithPath("result.data.totalCount").type(JsonFieldType.NUMBER).description("?????? total count"),
                                fieldWithPath("result.data.reviewResponseDtoList[]").type(JsonFieldType.ARRAY).description("?????? ?????????"),
                                fieldWithPath("result.data.reviewResponseDtoList[].createdDate").type(JsonFieldType.STRING).description("?????? ?????? ??????").optional(),
                                fieldWithPath("result.data.reviewResponseDtoList[].lastModifiedDate").type(JsonFieldType.STRING).description("?????? ????????? ?????? ??????").optional(),
                                fieldWithPath("result.data.reviewResponseDtoList[].id").type(JsonFieldType.NUMBER).description("?????? id"),
                                fieldWithPath("result.data.reviewResponseDtoList[].content").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("result.data.reviewResponseDtoList[].rating").type(JsonFieldType.NUMBER).description("?????? ??????"),
                                fieldWithPath("result.data.reviewResponseDtoList[].memberName").type(JsonFieldType.STRING).description("?????? ?????? ?????????").optional()
                        )
                )
                );
    }



    @Test
    @DisplayName("????????? ?????? ??????")
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
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????? id").optional(),
                                fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("?????? ID"),
                                fieldWithPath("productId").type(JsonFieldType.NUMBER).description("?????? ID"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("rating").type(JsonFieldType.NUMBER).description("?????? ??????")
                        ),
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("????????????"),
                                        fieldWithPath("result.data.rating").type(JsonFieldType.NUMBER).description("?????? ?????? ??????").optional(),
                                        fieldWithPath("result.data.content").type(JsonFieldType.STRING).description("?????? ????????? ?????? ??????").optional(),
                                        fieldWithPath("result.data.productId").type(JsonFieldType.NUMBER).description("?????? id"),
                                        fieldWithPath("result.data.memberId").type(JsonFieldType.NUMBER).description("?????? ??????")
                                )
                        )
                );
    }

    @Test
    @DisplayName("??????????????? ????????? ??? ?????? ??????")
    public void findByMemberId() throws Exception {

        reviewRepository.save(review1);
        reviewRepository.save(review2);

        this.mockMvc.perform(get("/review/member/{memberId}/{currentpage}",memberId,1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("review-rest-controller/get-total-rate",
                                pathParameters(
                                        parameterWithName("memberId").description("?????? id"),
                                        parameterWithName("currentpage").description("?????????")
                                ),
                                responseFields(
                                        fieldWithPath("mypageReviewDtoList[].id").type(JsonFieldType.NUMBER).description("?????? id"),
                                        fieldWithPath("mypageReviewDtoList[].createdDate").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                                        fieldWithPath("mypageReviewDtoList[].productId").type(JsonFieldType.NUMBER).description("?????? ?????? Id"),
                                        fieldWithPath("mypageReviewDtoList[].content").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("mypageReviewDtoList[].rating").type(JsonFieldType.NUMBER).description("?????? ??????"),
                                        fieldWithPath("mypageReviewDtoList[].memberId").type(JsonFieldType.NUMBER).description("?????? ?????? id"),
                                        fieldWithPath("mypageReviewDtoList[].thumbnail").type(JsonFieldType.STRING).description("?????? ??? ?????? ??????"),
                                        fieldWithPath("mypageReviewDtoList[].displayName").type(JsonFieldType.STRING).description("?????? ??? ?????????"),
                                        fieldWithPath("mypageReviewDtoList[].thumbnail").type(JsonFieldType.STRING).description("?????? ??? ??????"),
                                        fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("??? ?????? ???")
                                )
                        )
                );
    }

    @Test
    @DisplayName("????????? ?????? ??? ??????")
    public void getTotalRate() throws Exception {

        reviewRepository.save(review1);
        reviewRepository.save(review2);

        this.mockMvc.perform(get("/review/total/{productId}",product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("review-rest-controller/get_total_rate",
                        pathParameters(
                                parameterWithName("productId").description("?????? id")
                        ),
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("????????????"),
                                        fieldWithPath("result.data.totalRate").type(JsonFieldType.NUMBER).description("?????? ??? ??? ??????"),
                                        fieldWithPath("result.data.reviewCnt").type(JsonFieldType.NUMBER).description("?????? ??? ??????")
                                )
                        )
                );
    }
}
