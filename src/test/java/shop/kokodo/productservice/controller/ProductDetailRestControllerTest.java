package shop.kokodo.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
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
import shop.kokodo.productservice.entity.Category;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.entity.ProductDetail;
import shop.kokodo.productservice.repository.ProductDetailRepository;
import shop.kokodo.productservice.repository.ProductRepository;
import shop.kokodo.productservice.service.ProductDetailService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
//@Transactional
public class ProductDetailRestControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductDetailRepository productDetailRepository;

    Category category;
    Product product;

    long productId;

    List<ProductDetail> productDetailList;

    final LocalDateTime deadline = LocalDateTime.of(2022,10,25,0,0);



    @BeforeEach
    public void setUp(){
        category = Category.builder()
                .name("healthy")
                .build();

        productDetailList = new ArrayList<>();

        ProductDetail productDetail1 = ProductDetail.builder()
                .image("image1")
                .orders(0)
                .build();
        ProductDetail productDetail2 = ProductDetail.builder()
                .image("image2")
                .orders(2)
                .build();

        productDetailList.add(productDetail1);
        productDetailList.add(productDetail2);


        product = Product.builder()
                .category(category)
                .name("맛닭")
                .price(1000)
                .displayName("맛닭")
                .stock(10)
                .deadline(deadline)
                .thumbnail("맛닭")
                .sellerId(1)
                .deliveryFee(1000)
                .build();

        product.changeProductDetail(productDetailList);

        productId=productRepository.save(product).getId();
    }

//    @Test
//    @DisplayName("상품 디테일 이미지")
//    public void findByProductId() throws Exception {
//
//
//        this.mockMvc.perform(get("/productDetail/{productId}",productId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andDo(document("product-detail-rest-controller/find-by-product-id",
//                                pathParameters(
//                                        parameterWithName("productId").description("상품 id")
//                                ),
//                                responseFields(
//                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
//                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태코드"),
//                                        fieldWithPath("result").description("응답데이터"),
//                                        fieldWithPath("result.data[]").type(JsonFieldType.ARRAY).description("리뷰 리스트"),
//                                        fieldWithPath("result.data[].createdDate").type(JsonFieldType.ARRAY).description("리뷰 생성 날짜").optional(),
//                                        fieldWithPath("result.data[].lastModifiedDate").type(JsonFieldType.ARRAY).description("리뷰 마지막 수정 날짜").optional(),
//                                        fieldWithPath("result.data[].id").type(JsonFieldType.NUMBER).description("리뷰 id"),
//                                        fieldWithPath("result.data[].image").type(JsonFieldType.STRING).description("리뷰 내용"),
//                                        fieldWithPath("result.data[].orders").type(JsonFieldType.NUMBER).description("리뷰 평점")
//                                )
//                        )
//                );
//
//
//    }
}
