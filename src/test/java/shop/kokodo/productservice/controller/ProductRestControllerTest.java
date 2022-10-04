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
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.entity.ProductDetail;
import shop.kokodo.productservice.repository.CategoryRepository;
import shop.kokodo.productservice.repository.ProductRepository;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
public class ProductRestControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;

    Category category;
    Product product1;
    ProductDetail productDetail1;
    ProductDetail productDetail2;
    ProductDetail productDetail3;
    final LocalDateTime localDateTime = LocalDateTime.of(2022,10,25,0,0);

    @BeforeEach
    public void setUp(){
        category = Category.builder()
                .name("healthy")
                .build();

        product1 = Product.builder()
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
        productDetail1 = ProductDetail.builder()
                .product(product1)
                .image("image1")
                .orders(1)
                .build();
        productDetail2 = ProductDetail.builder()
                .product(product1)
                .image("image2")
                .orders(1)
                .build();
        productDetail3 = ProductDetail.builder()
                .product(product1)
                .image("image3")
                .orders(1)
                .build();
    }

    @Test
    @DisplayName("product detail 조회")
    public void productDetail() throws Exception {

        product1.addProductDetail(productDetail1);
        product1.addProductDetail(productDetail2);
        product1.addProductDetail(productDetail3);

        categoryRepository.save(category);

        productRepository.save(product1);

        this.mockMvc.perform(get("/product/detail/{productId}",product1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-rest-controller/product-detail",
                                pathParameters(
                                        parameterWithName("productId").description("상품 id")
                                ),
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태코드"),
                                        fieldWithPath("result.data.createdDate").type(JsonFieldType.STRING).description("상품 생성 날짜"),
                                        fieldWithPath("result.data.lastModifiedDate").type(JsonFieldType.STRING).description("상품 마지막 수정 날짜"),
                                        fieldWithPath("result.data.id").type(JsonFieldType.NUMBER).description("상품 id"),
                                        fieldWithPath("result.data.name").type(JsonFieldType.STRING).description("상품 이름"),
                                        fieldWithPath("result.data.price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                        fieldWithPath("result.data.displayName").type(JsonFieldType.STRING).description("상품 노출명"),
                                        fieldWithPath("result.data.stock").type(JsonFieldType.NUMBER).description("상품 재고"),
                                        fieldWithPath("result.data.deadline").type(JsonFieldType.STRING).description("상품 유통기한"),
                                        fieldWithPath("result.data.thumbnail").type(JsonFieldType.STRING).description("상품 썸네일"),
                                        fieldWithPath("result.data.sellerId").type(JsonFieldType.NUMBER).description("상품 셀러 아이디"),
                                        fieldWithPath("result.data.deliveryFee").type(JsonFieldType.NUMBER).description("상품 배송비"),
                                        fieldWithPath("result.data.productDetailList[]").type(JsonFieldType.ARRAY).description("상품 디테일 이미지 배열"),
                                        fieldWithPath("result.data.productDetailList[].createdDate").type(JsonFieldType.NULL).description("상품 디테일 생성 날짜"),
                                        fieldWithPath("result.data.productDetailList[].lastModifiedDate").type(JsonFieldType.NULL).description("상품 디테일 마지막 수정일"),
                                        fieldWithPath("result.data.productDetailList[].id").type(JsonFieldType.NUMBER).description("상품 디테일 아이디"),
                                        fieldWithPath("result.data.productDetailList[].image").type(JsonFieldType.STRING).description("상품 디테일 이미지 url"),
                                        fieldWithPath("result.data.productDetailList[].orders").type(JsonFieldType.NUMBER).description("상품 디테일 이미지 순서")
                                )
                        )
                );

    }
}