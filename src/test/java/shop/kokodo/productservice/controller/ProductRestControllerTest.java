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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import shop.kokodo.productservice.entity.Category;
import shop.kokodo.productservice.entity.DetailFlag;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.entity.ProductDetail;
import shop.kokodo.productservice.repository.CategoryRepository;
import shop.kokodo.productservice.repository.ProductRepository;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
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
    final LocalDateTime localDateTime = LocalDateTime.of(2022,11,26,0,0);
    final long sellerId = 1L;
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
                .sellerId(sellerId)
                .deliveryFee(1000)
                .detailFlag(DetailFlag.IMG)
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

        product1.addProductDetail(productDetail1);
        product1.addProductDetail(productDetail2);
        product1.addProductDetail(productDetail3);

        categoryRepository.save(category);
        productRepository.save(product1);
    }

    @Test
    @DisplayName("상품 삭제 성공")
    public void productDelete() throws Exception{

        this.mockMvc.perform(delete("/product/delete/{productId}", product1.getId()))
                .andExpect(status().isOk())
                .andDo(document("product-rest-controller/product-delete",
                        pathParameters(
                                parameterWithName("productId").description("상품 id")
                        ),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태코드")
                        ))
                );
    }

    @Test
    @DisplayName("단일 상품 조회 성공")
    public void findById() throws Exception{

        this.mockMvc.perform(get("/product/productId/{productId}",product1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-rest-controller/product-productId",
                                pathParameters(
                                        parameterWithName("productId").description("상품 id")
                                ),
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태코드"),
                                        fieldWithPath("result.data.id").type(JsonFieldType.NUMBER).description("상품 id"),
                                        fieldWithPath("result.data.categoryId").type(JsonFieldType.NUMBER).description("상품 카테고리 id"),
                                        fieldWithPath("result.data.name").type(JsonFieldType.STRING).description("상품 이름"),
                                        fieldWithPath("result.data.price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                        fieldWithPath("result.data.displayName").type(JsonFieldType.STRING).description("상품 노출명"),
                                        fieldWithPath("result.data.stock").type(JsonFieldType.NUMBER).description("상품 재고"),
                                        fieldWithPath("result.data.deadline").type(JsonFieldType.STRING).description("상품 유통기한"),
                                        fieldWithPath("result.data.thumbnail").type(JsonFieldType.STRING).description("상품 썸네일"),
                                        fieldWithPath("result.data.sellerId").type(JsonFieldType.NUMBER).description("상품 셀러 아이디"),
                                        fieldWithPath("result.data.deliveryFee").type(JsonFieldType.NUMBER).description("상품 배송비")
                                )
                        )
                );
    }

    @Test
    @DisplayName("전체 상품 조회 성공")
    public void findAll() throws Exception{

        this.mockMvc.perform(get("/product/productAll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-rest-controller/product-all",
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태코드"),
                                        fieldWithPath("result.data[]").type(JsonFieldType.ARRAY).description("상품 배열"),
                                        fieldWithPath("result.data[].id").type(JsonFieldType.NUMBER).description("상품 id"),
                                        fieldWithPath("result.data[].categoryId").type(JsonFieldType.NUMBER).description("상품 카테고리 id"),
                                        fieldWithPath("result.data[].name").type(JsonFieldType.STRING).description("상품 이름"),
                                        fieldWithPath("result.data[].price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                        fieldWithPath("result.data[].displayName").type(JsonFieldType.STRING).description("상품 노출명"),
                                        fieldWithPath("result.data[].stock").type(JsonFieldType.NUMBER).description("상품 재고"),
                                        fieldWithPath("result.data[].deadline").type(JsonFieldType.STRING).description("상품 유통기한"),
                                        fieldWithPath("result.data[].thumbnail").type(JsonFieldType.STRING).description("상품 썸네일"),
                                        fieldWithPath("result.data[].sellerId").type(JsonFieldType.NUMBER).description("상품 셀러 아이디"),
                                        fieldWithPath("result.data[].deliveryFee").type(JsonFieldType.NUMBER).description("상품 배송비")
                                )
                        )
                );
    }

    @Test
    @DisplayName("카테고리 별 상품 조회 및 정렬 성공")
    public void productByCategorySorting() throws Exception{

        this.mockMvc.perform(get("/product/categoryId/{categoryId}/{sortingId}/{currentpage}",category.getId(),1,1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-rest-controller/product-productByCategorySorting",
                                pathParameters(
                                        parameterWithName("categoryId").description("카테고리 Id"),
                                        parameterWithName("sortingId").description("정렬 순서"),
                                        parameterWithName("currentpage").description("현재 페이지")
                                ),
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태코드"),
                                        fieldWithPath("result.data.productDtoList[]").type(JsonFieldType.ARRAY).description("상품 배열"),
                                        fieldWithPath("result.data.productDtoList[].id").type(JsonFieldType.NUMBER).description("상품 id"),
                                        fieldWithPath("result.data.productDtoList[].categoryId").type(JsonFieldType.NUMBER).description("상품 카테고리 id"),
                                        fieldWithPath("result.data.productDtoList[].name").type(JsonFieldType.STRING).description("상품 이름"),
                                        fieldWithPath("result.data.productDtoList[].price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                        fieldWithPath("result.data.productDtoList[].displayName").type(JsonFieldType.STRING).description("상품 노출명"),
                                        fieldWithPath("result.data.productDtoList[].stock").type(JsonFieldType.NUMBER).description("상품 재고"),
                                        fieldWithPath("result.data.productDtoList[].deadline").type(JsonFieldType.STRING).description("상품 유통기한"),
                                        fieldWithPath("result.data.productDtoList[].thumbnail").type(JsonFieldType.STRING).description("상품 썸네일"),
                                        fieldWithPath("result.data.productDtoList[].sellerId").type(JsonFieldType.NUMBER).description("상품 셀러 아이디"),
                                        fieldWithPath("result.data.productDtoList[].deliveryFee").type(JsonFieldType.NUMBER).description("상품 배송비"),
                                        fieldWithPath("result.data.totalCount").type(JsonFieldType.NUMBER).description("상품 총수")
                                )
                        )
                );
    }

    /* ======= Main 상품 =======*/
    @Test
    @DisplayName("신상품 조회 성공")
    public void findByNew() throws Exception{

        this.mockMvc.perform(get("/product/main/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-rest-controller/product-main-new",
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태코드"),
                                        fieldWithPath("result.data[]").type(JsonFieldType.ARRAY).description("상품 배열"),
                                        fieldWithPath("result.data[].id").type(JsonFieldType.NUMBER).description("상품 id"),
                                        fieldWithPath("result.data[].categoryId").type(JsonFieldType.NUMBER).description("상품 카테고리 id"),
                                        fieldWithPath("result.data[].name").type(JsonFieldType.STRING).description("상품 이름"),
                                        fieldWithPath("result.data[].price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                        fieldWithPath("result.data[].displayName").type(JsonFieldType.STRING).description("상품 노출명"),
                                        fieldWithPath("result.data[].stock").type(JsonFieldType.NUMBER).description("상품 재고"),
                                        fieldWithPath("result.data[].deadline").type(JsonFieldType.STRING).description("상품 유통기한"),
                                        fieldWithPath("result.data[].thumbnail").type(JsonFieldType.STRING).description("상품 썸네일"),
                                        fieldWithPath("result.data[].sellerId").type(JsonFieldType.NUMBER).description("상품 셀러 아이디"),
                                        fieldWithPath("result.data[].deliveryFee").type(JsonFieldType.NUMBER).description("상품 배송비")
                                )
                        )
                );
    }

    @Test
    @DisplayName("타임 세일 조회 성공")
    public void findBySale() throws Exception{

        this.mockMvc.perform(get("/product/main/sale")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-rest-controller/product-main-sale",
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태코드"),
                                        fieldWithPath("result.data[]").type(JsonFieldType.ARRAY).description("상품 배열"),
                                        fieldWithPath("result.data[].id").type(JsonFieldType.NUMBER).description("상품 id"),
                                        fieldWithPath("result.data[].categoryId").type(JsonFieldType.NUMBER).description("상품 카테고리 id"),
                                        fieldWithPath("result.data[].name").type(JsonFieldType.STRING).description("상품 이름"),
                                        fieldWithPath("result.data[].price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                        fieldWithPath("result.data[].displayName").type(JsonFieldType.STRING).description("상품 노출명"),
                                        fieldWithPath("result.data[].stock").type(JsonFieldType.NUMBER).description("상품 재고"),
                                        fieldWithPath("result.data[].deadline").type(JsonFieldType.STRING).description("상품 유통기한"),
                                        fieldWithPath("result.data[].thumbnail").type(JsonFieldType.STRING).description("상품 썸네일"),
                                        fieldWithPath("result.data[].sellerId").type(JsonFieldType.NUMBER).description("상품 셀러 아이디"),
                                        fieldWithPath("result.data[].deliveryFee").type(JsonFieldType.NUMBER).description("상품 배송비")
                                )
                        )
                );
    }

    @Test
    @DisplayName("셀러 추천 조회 성공")
    public void findBySeller() throws Exception{

        this.mockMvc.perform(get("/product/main/seller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-rest-controller/product-main-seller",
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태코드"),
                                        fieldWithPath("result.data[]").type(JsonFieldType.ARRAY).description("상품 배열"),
                                        fieldWithPath("result.data[].id").type(JsonFieldType.NUMBER).description("상품 id"),
                                        fieldWithPath("result.data[].categoryId").type(JsonFieldType.NUMBER).description("상품 카테고리 id"),
                                        fieldWithPath("result.data[].name").type(JsonFieldType.STRING).description("상품 이름"),
                                        fieldWithPath("result.data[].price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                        fieldWithPath("result.data[].displayName").type(JsonFieldType.STRING).description("상품 노출명"),
                                        fieldWithPath("result.data[].stock").type(JsonFieldType.NUMBER).description("상품 재고"),
                                        fieldWithPath("result.data[].deadline").type(JsonFieldType.STRING).description("상품 유통기한"),
                                        fieldWithPath("result.data[].thumbnail").type(JsonFieldType.STRING).description("상품 썸네일"),
                                        fieldWithPath("result.data[].sellerId").type(JsonFieldType.NUMBER).description("상품 셀러 아이디"),
                                        fieldWithPath("result.data[].deliveryFee").type(JsonFieldType.NUMBER).description("상품 배송비")
                                )
                        )
                );
    }

    /* ======= 상품 나열 ======= */
    @Test
    @DisplayName("세일 상품 조회 및 정렬 성공")
    public void productBySaleSorting() throws Exception{

        this.mockMvc.perform(get("/product/main/sale/all/{sortingId}/{currentpage}",1,1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-rest-controller/product-main-sale-all",
                                pathParameters(
                                        parameterWithName("sortingId").description("정렬 순서"),
                                        parameterWithName("currentpage").description("현재 페이지")
                                ),
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태코드"),
                                        fieldWithPath("result.data.productDtoList[]").type(JsonFieldType.ARRAY).description("상품 배열"),
                                        fieldWithPath("result.data.productDtoList[].id").type(JsonFieldType.NUMBER).description("상품 id"),
                                        fieldWithPath("result.data.productDtoList[].categoryId").type(JsonFieldType.NUMBER).description("상품 카테고리 id"),
                                        fieldWithPath("result.data.productDtoList[].name").type(JsonFieldType.STRING).description("상품 이름"),
                                        fieldWithPath("result.data.productDtoList[].price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                        fieldWithPath("result.data.productDtoList[].displayName").type(JsonFieldType.STRING).description("상품 노출명"),
                                        fieldWithPath("result.data.productDtoList[].stock").type(JsonFieldType.NUMBER).description("상품 재고"),
                                        fieldWithPath("result.data.productDtoList[].deadline").type(JsonFieldType.STRING).description("상품 유통기한"),
                                        fieldWithPath("result.data.productDtoList[].thumbnail").type(JsonFieldType.STRING).description("상품 썸네일"),
                                        fieldWithPath("result.data.productDtoList[].sellerId").type(JsonFieldType.NUMBER).description("상품 셀러 아이디"),
                                        fieldWithPath("result.data.productDtoList[].deliveryFee").type(JsonFieldType.NUMBER).description("상품 배송비"),
                                        fieldWithPath("result.data.totalCount").type(JsonFieldType.NUMBER).description("상품 총수")
                                )
                        )
                );
    }
    @Test
    @DisplayName("셀러추천 상품 조회 및 정렬 성공")
    public void productBySellerSorting() throws Exception{

        this.mockMvc.perform(get("/product/main/seller/all/{sortingId}/{currentpage}",1,1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-rest-controller/product-main-seller-all",
                                pathParameters(
                                        parameterWithName("sortingId").description("정렬 순서"),
                                        parameterWithName("currentpage").description("현재 페이지")
                                ),
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태코드"),
                                        fieldWithPath("result.data.productDtoList[]").type(JsonFieldType.ARRAY).description("상품 배열"),
                                        fieldWithPath("result.data.productDtoList[].id").type(JsonFieldType.NUMBER).description("상품 id"),
                                        fieldWithPath("result.data.productDtoList[].categoryId").type(JsonFieldType.NUMBER).description("상품 카테고리 id"),
                                        fieldWithPath("result.data.productDtoList[].name").type(JsonFieldType.STRING).description("상품 이름"),
                                        fieldWithPath("result.data.productDtoList[].price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                        fieldWithPath("result.data.productDtoList[].displayName").type(JsonFieldType.STRING).description("상품 노출명"),
                                        fieldWithPath("result.data.productDtoList[].stock").type(JsonFieldType.NUMBER).description("상품 재고"),
                                        fieldWithPath("result.data.productDtoList[].deadline").type(JsonFieldType.STRING).description("상품 유통기한"),
                                        fieldWithPath("result.data.productDtoList[].thumbnail").type(JsonFieldType.STRING).description("상품 썸네일"),
                                        fieldWithPath("result.data.productDtoList[].sellerId").type(JsonFieldType.NUMBER).description("상품 셀러 아이디"),
                                        fieldWithPath("result.data.productDtoList[].deliveryFee").type(JsonFieldType.NUMBER).description("상품 배송비"),
                                        fieldWithPath("result.data.totalCount").type(JsonFieldType.NUMBER).description("상품 총수")
                                )
                        )
                );
    }
    @Test
    @DisplayName("전체 상품 검색 조회 및 정렬 성공")
    public void productByTotalSearch() throws Exception{

        this.mockMvc.perform(get("/product/totalSearch/{totalSearch}/{sortingId}/{currentpage}","닭",1,1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-rest-controller/product-totalSearch",
                                pathParameters(
                                        parameterWithName("totalSearch").description("상품 검색"),
                                        parameterWithName("sortingId").description("정렬 순서"),
                                        parameterWithName("currentpage").description("현재 페이지")
                                ),
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태코드"),
                                        fieldWithPath("result.data.productDtoList[]").type(JsonFieldType.ARRAY).description("상품 배열"),
                                        fieldWithPath("result.data.productDtoList[].id").type(JsonFieldType.NUMBER).description("상품 id"),
                                        fieldWithPath("result.data.productDtoList[].categoryId").type(JsonFieldType.NUMBER).description("상품 카테고리 id"),
                                        fieldWithPath("result.data.productDtoList[].name").type(JsonFieldType.STRING).description("상품 이름"),
                                        fieldWithPath("result.data.productDtoList[].price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                        fieldWithPath("result.data.productDtoList[].displayName").type(JsonFieldType.STRING).description("상품 노출명"),
                                        fieldWithPath("result.data.productDtoList[].stock").type(JsonFieldType.NUMBER).description("상품 재고"),
                                        fieldWithPath("result.data.productDtoList[].deadline").type(JsonFieldType.STRING).description("상품 유통기한"),
                                        fieldWithPath("result.data.productDtoList[].thumbnail").type(JsonFieldType.STRING).description("상품 썸네일"),
                                        fieldWithPath("result.data.productDtoList[].sellerId").type(JsonFieldType.NUMBER).description("상품 셀러 아이디"),
                                        fieldWithPath("result.data.productDtoList[].deliveryFee").type(JsonFieldType.NUMBER).description("상품 배송비"),
                                        fieldWithPath("result.data.totalCount").type(JsonFieldType.NUMBER).description("상품 총수")
                                )
                        )
                );
    }

    @Test
    @DisplayName("상품 상세 조회")
    public void productDetail() throws Exception {

        this.mockMvc.perform(get("/product/detail/{productId}",product1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-rest-controller/product-detail",
                        pathParameters(
                                parameterWithName("productId").description("검색 상품 이름")
                        ),
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태코드"),
                                        fieldWithPath("result.data.id").type(JsonFieldType.NUMBER).description("상품 id"),
                                        fieldWithPath("result.data.categoryId").type(JsonFieldType.NUMBER).description("카테고리 id"),
                                        fieldWithPath("result.data.productDetailList").type(JsonFieldType.ARRAY).description("상품 디테일 이미지"),
                                        fieldWithPath("result.data.productDetailList[].id").type(JsonFieldType.NUMBER).description("상품 디테일 이미지 id"),
                                        fieldWithPath("result.data.productDetailList[].image").type(JsonFieldType.STRING).description("상품 디테일 이미지 url"),
                                        fieldWithPath("result.data.productDetailList[].orders").type(JsonFieldType.NUMBER).description("상품 디테일 이미지 순서"),
                                        fieldWithPath("result.data.name").type(JsonFieldType.STRING).description("상품 이름"),
                                        fieldWithPath("result.data.price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                        fieldWithPath("result.data.displayName").type(JsonFieldType.STRING).description("상품 노출명"),
                                        fieldWithPath("result.data.detailFlag").type(JsonFieldType.STRING).description("디테일 구분"),
                                        fieldWithPath("result.data.templateRec").type(JsonFieldType.OBJECT).description("디테일 템플릿").optional(),
                                        fieldWithPath("result.data.stock").type(JsonFieldType.NUMBER).description("상품 재고"),
                                        fieldWithPath("result.data.deadline").type(JsonFieldType.STRING).description("상품 유통기한"),
                                        fieldWithPath("result.data.thumbnail").type(JsonFieldType.STRING).description("상품 대표 이미지"),
                                        fieldWithPath("result.data.sellerId").type(JsonFieldType.NUMBER).description("셀러 id"),
                                        fieldWithPath("result.data.deliveryFee").type(JsonFieldType.NUMBER).description("상품 배송비")
                                )
                        )
                );
    }

    @Test
    @DisplayName("product 조건 검색")
    public void findByProductNameAndStatusAndDate() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("productName","닭");
        params.add("status","0");
        params.add("startDate","2020-01-01 11:11");
        params.add("endDate","2025-02-02 22:22");
        params.add("sellerId","1");
        params.add("page","1");

        this.mockMvc.perform(get("/product")
                        .params(params)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-rest-controller/find-by-name-status-date",
                                requestParameters(
                                        parameterWithName("productName").description("상품 이름"),
                                        parameterWithName("status").description("상품 판매 상태"),
                                        parameterWithName("startDate").description("상품 등록 시작 일자"),
                                        parameterWithName("endDate").description("상품 등록 마감 일자"),
                                        parameterWithName("sellerId").description("셀러 id"),
                                        parameterWithName("page").description("페이지 번호")
                                ),
                                responseFields(
                                        fieldWithPath("productDtoList[].id").type(JsonFieldType.NUMBER).description("상품 id"),
                                        fieldWithPath("productDtoList[].categoryId").type(JsonFieldType.NUMBER).description("상품 카테고리 id"),
                                        fieldWithPath("productDtoList[].categoryName").type(JsonFieldType.STRING).description("상품 카테고리 이름"),
                                        fieldWithPath("productDtoList[].price").type(JsonFieldType.NUMBER).description("상품 가격"),
                                        fieldWithPath("productDtoList[].displayName").type(JsonFieldType.STRING).description("상품 노출명"),
                                        fieldWithPath("productDtoList[].stock").type(JsonFieldType.NUMBER).description("상품 재고"),
                                        fieldWithPath("productDtoList[].thumbnail").type(JsonFieldType.STRING).description("상품 대표 이미지"),
                                        fieldWithPath("productDtoList[].sellerId").type(JsonFieldType.NUMBER).description("셀러 id"),
                                        fieldWithPath("productDtoList[].deliveryFee").type(JsonFieldType.NUMBER).description("상품 배송비"),
                                        fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("total count")
                                )
                        )
                );
    }

//    @Test
//    @DisplayName("seller 아이디로 상품 조회")
//    public void findBySellerId() throws Exception {
//        categoryRepository.save(category);
//        productRepository.save(product1);
//
//        this.mockMvc.perform(get("/product/seller/{sellerId}",sellerId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andDo(document("product-rest-controller/find-by-seller-id",
//                                pathParameters(
//                                        parameterWithName("sellerId").description("셀러 id")
//                                ),
//                                responseFields(
//                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공여부"),
//                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("상태코드"),
//                                        fieldWithPath("result.data[].id").type(JsonFieldType.NUMBER).description("상품 id"),
//                                        fieldWithPath("result.data[].name").type(JsonFieldType.STRING).description("상품 id"),
//                                        fieldWithPath("result.data[].categoryId").type(JsonFieldType.NUMBER).description("상품 카테고리 id"),
//                                        fieldWithPath("result.data[].price").type(JsonFieldType.NUMBER).description("상품 가격"),
//                                        fieldWithPath("result.data[].displayName").type(JsonFieldType.STRING).description("상품 노출명"),
//                                        fieldWithPath("result.data[].deadline").type(JsonFieldType.STRING).description("상품 노출명"),
//                                        fieldWithPath("result.data[].stock").type(JsonFieldType.NUMBER).description("상품 재고"),
//                                        fieldWithPath("result.data[].thumbnail").type(JsonFieldType.STRING).description("상품 대표 이미지"),
//                                        fieldWithPath("result.data[].sellerId").type(JsonFieldType.NUMBER).description("셀러 id"),
//                                        fieldWithPath("result.data[].deliveryFee").type(JsonFieldType.NUMBER).description("상품 배송비")
//                                )
//                        )
//                );
//    }

//    // TODO : 응답 문서 다시 수정
//    @Test
//    @DisplayName("상품 id로 상품 조회")
//    public void findProductById() throws Exception {
//
//        this.mockMvc.perform(get("/product/feign/id")
//                        .param("productId",""+product1.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andDo(document("product-rest-controller/find-by-product-id",
//                                requestParameters(
//                                        parameterWithName("productId").description("상품 id")
//                                ),
//                                responseFields(
//                                        fieldWithPath("flag").type(JsonFieldType.BOOLEAN).description("상품 존재 유무")
//                                )
//                        )
//                );
//    }
}