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

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
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
                .name("??????")
                .price(1000)
                .displayName("??????")
                .stock(10)
                .deadline(localDateTime)
                .thumbnail("??????")
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
    @DisplayName("?????? ?????? ??????")
    public void productDelete() throws Exception{

        this.mockMvc.perform(delete("/product/delete/{productId}", product1.getId()))
                .andExpect(status().isOk())
                .andDo(document("product-rest-controller/product-delete",
                        pathParameters(
                                parameterWithName("productId").description("?????? id")
                        ),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("????????????")
                        ))
                );
    }

    @Test
    @DisplayName("?????? ?????? ?????? ??????")
    public void findById() throws Exception{

        this.mockMvc.perform(get("/product/productId/{productId}",product1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-rest-controller/product-productId",
                                pathParameters(
                                        parameterWithName("productId").description("?????? id")
                                ),
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("????????????"),
                                        fieldWithPath("result.data.id").type(JsonFieldType.NUMBER).description("?????? id"),
                                        fieldWithPath("result.data.categoryId").type(JsonFieldType.NUMBER).description("?????? ???????????? id"),
                                        fieldWithPath("result.data.name").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("result.data.price").type(JsonFieldType.NUMBER).description("?????? ??????"),
                                        fieldWithPath("result.data.displayName").type(JsonFieldType.STRING).description("?????? ?????????"),
                                        fieldWithPath("result.data.stock").type(JsonFieldType.NUMBER).description("?????? ??????"),
                                        fieldWithPath("result.data.deadline").type(JsonFieldType.STRING).description("?????? ????????????"),
                                        fieldWithPath("result.data.thumbnail").type(JsonFieldType.STRING).description("?????? ?????????"),
                                        fieldWithPath("result.data.sellerId").type(JsonFieldType.NUMBER).description("?????? ?????? ?????????"),
                                        fieldWithPath("result.data.deliveryFee").type(JsonFieldType.NUMBER).description("?????? ?????????")
                                )
                        )
                );
    }

    @Test
    @DisplayName("?????? ?????? ?????? ??????")
    public void findAll() throws Exception{

        this.mockMvc.perform(get("/product/productAll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-rest-controller/product-all",
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("????????????"),
                                        fieldWithPath("result.data[]").type(JsonFieldType.ARRAY).description("?????? ??????"),
                                        fieldWithPath("result.data[].id").type(JsonFieldType.NUMBER).description("?????? id"),
                                        fieldWithPath("result.data[].categoryId").type(JsonFieldType.NUMBER).description("?????? ???????????? id"),
                                        fieldWithPath("result.data[].name").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("result.data[].price").type(JsonFieldType.NUMBER).description("?????? ??????"),
                                        fieldWithPath("result.data[].displayName").type(JsonFieldType.STRING).description("?????? ?????????"),
                                        fieldWithPath("result.data[].stock").type(JsonFieldType.NUMBER).description("?????? ??????"),
                                        fieldWithPath("result.data[].deadline").type(JsonFieldType.STRING).description("?????? ????????????"),
                                        fieldWithPath("result.data[].thumbnail").type(JsonFieldType.STRING).description("?????? ?????????"),
                                        fieldWithPath("result.data[].sellerId").type(JsonFieldType.NUMBER).description("?????? ?????? ?????????"),
                                        fieldWithPath("result.data[].deliveryFee").type(JsonFieldType.NUMBER).description("?????? ?????????")
                                )
                        )
                );
    }

    @Test
    @DisplayName("???????????? ??? ?????? ?????? ??? ?????? ??????")
    public void productByCategorySorting() throws Exception{

        this.mockMvc.perform(get("/product/categoryId/{categoryId}/{sortingId}/{currentpage}",category.getId(),1,1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-rest-controller/product-productByCategorySorting",
                                pathParameters(
                                        parameterWithName("categoryId").description("???????????? Id"),
                                        parameterWithName("sortingId").description("?????? ??????"),
                                        parameterWithName("currentpage").description("?????? ?????????")
                                ),
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("????????????"),
                                        fieldWithPath("result.data.productDtoList[]").type(JsonFieldType.ARRAY).description("?????? ??????"),
                                        fieldWithPath("result.data.productDtoList[].id").type(JsonFieldType.NUMBER).description("?????? id"),
                                        fieldWithPath("result.data.productDtoList[].categoryId").type(JsonFieldType.NUMBER).description("?????? ???????????? id"),
                                        fieldWithPath("result.data.productDtoList[].name").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("result.data.productDtoList[].price").type(JsonFieldType.NUMBER).description("?????? ??????"),
                                        fieldWithPath("result.data.productDtoList[].displayName").type(JsonFieldType.STRING).description("?????? ?????????"),
                                        fieldWithPath("result.data.productDtoList[].stock").type(JsonFieldType.NUMBER).description("?????? ??????"),
                                        fieldWithPath("result.data.productDtoList[].deadline").type(JsonFieldType.STRING).description("?????? ????????????"),
                                        fieldWithPath("result.data.productDtoList[].thumbnail").type(JsonFieldType.STRING).description("?????? ?????????"),
                                        fieldWithPath("result.data.productDtoList[].sellerId").type(JsonFieldType.NUMBER).description("?????? ?????? ?????????"),
                                        fieldWithPath("result.data.productDtoList[].deliveryFee").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                        fieldWithPath("result.data.totalCount").type(JsonFieldType.NUMBER).description("?????? ??????")
                                )
                        )
                );
    }

    /* ======= Main ?????? =======*/
    @Test
    @DisplayName("????????? ?????? ??????")
    public void findByNew() throws Exception{

        this.mockMvc.perform(get("/product/main/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-rest-controller/product-main-new",
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("????????????"),
                                        fieldWithPath("result.data[]").type(JsonFieldType.ARRAY).description("?????? ??????"),
                                        fieldWithPath("result.data[].id").type(JsonFieldType.NUMBER).description("?????? id"),
                                        fieldWithPath("result.data[].categoryId").type(JsonFieldType.NUMBER).description("?????? ???????????? id"),
                                        fieldWithPath("result.data[].name").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("result.data[].price").type(JsonFieldType.NUMBER).description("?????? ??????"),
                                        fieldWithPath("result.data[].displayName").type(JsonFieldType.STRING).description("?????? ?????????"),
                                        fieldWithPath("result.data[].stock").type(JsonFieldType.NUMBER).description("?????? ??????"),
                                        fieldWithPath("result.data[].deadline").type(JsonFieldType.STRING).description("?????? ????????????"),
                                        fieldWithPath("result.data[].thumbnail").type(JsonFieldType.STRING).description("?????? ?????????"),
                                        fieldWithPath("result.data[].sellerId").type(JsonFieldType.NUMBER).description("?????? ?????? ?????????"),
                                        fieldWithPath("result.data[].deliveryFee").type(JsonFieldType.NUMBER).description("?????? ?????????")
                                )
                        )
                );
    }

    @Test
    @DisplayName("?????? ?????? ?????? ??????")
    public void findBySale() throws Exception{

        this.mockMvc.perform(get("/product/main/sale")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-rest-controller/product-main-sale",
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("????????????"),
                                        fieldWithPath("result.data[]").type(JsonFieldType.ARRAY).description("?????? ??????"),
                                        fieldWithPath("result.data[].id").type(JsonFieldType.NUMBER).description("?????? id"),
                                        fieldWithPath("result.data[].categoryId").type(JsonFieldType.NUMBER).description("?????? ???????????? id"),
                                        fieldWithPath("result.data[].name").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("result.data[].price").type(JsonFieldType.NUMBER).description("?????? ??????"),
                                        fieldWithPath("result.data[].displayName").type(JsonFieldType.STRING).description("?????? ?????????"),
                                        fieldWithPath("result.data[].stock").type(JsonFieldType.NUMBER).description("?????? ??????"),
                                        fieldWithPath("result.data[].deadline").type(JsonFieldType.STRING).description("?????? ????????????"),
                                        fieldWithPath("result.data[].thumbnail").type(JsonFieldType.STRING).description("?????? ?????????"),
                                        fieldWithPath("result.data[].sellerId").type(JsonFieldType.NUMBER).description("?????? ?????? ?????????"),
                                        fieldWithPath("result.data[].deliveryFee").type(JsonFieldType.NUMBER).description("?????? ?????????")
                                )
                        )
                );
    }

    @Test
    @DisplayName("?????? ?????? ?????? ??????")
    public void findBySeller() throws Exception{

        this.mockMvc.perform(get("/product/main/seller")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-rest-controller/product-main-seller",
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("????????????"),
                                        fieldWithPath("result.data[]").type(JsonFieldType.ARRAY).description("?????? ??????"),
                                        fieldWithPath("result.data[].id").type(JsonFieldType.NUMBER).description("?????? id"),
                                        fieldWithPath("result.data[].categoryId").type(JsonFieldType.NUMBER).description("?????? ???????????? id"),
                                        fieldWithPath("result.data[].name").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("result.data[].price").type(JsonFieldType.NUMBER).description("?????? ??????"),
                                        fieldWithPath("result.data[].displayName").type(JsonFieldType.STRING).description("?????? ?????????"),
                                        fieldWithPath("result.data[].stock").type(JsonFieldType.NUMBER).description("?????? ??????"),
                                        fieldWithPath("result.data[].deadline").type(JsonFieldType.STRING).description("?????? ????????????"),
                                        fieldWithPath("result.data[].thumbnail").type(JsonFieldType.STRING).description("?????? ?????????"),
                                        fieldWithPath("result.data[].sellerId").type(JsonFieldType.NUMBER).description("?????? ?????? ?????????"),
                                        fieldWithPath("result.data[].deliveryFee").type(JsonFieldType.NUMBER).description("?????? ?????????")
                                )
                        )
                );
    }

    /* ======= ?????? ?????? ======= */
    @Test
    @DisplayName("?????? ?????? ?????? ??? ?????? ??????")
    public void productBySaleSorting() throws Exception{

        this.mockMvc.perform(get("/product/main/sale/all/{sortingId}/{currentpage}",1,1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-rest-controller/product-main-sale-all",
                                pathParameters(
                                        parameterWithName("sortingId").description("?????? ??????"),
                                        parameterWithName("currentpage").description("?????? ?????????")
                                ),
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("????????????"),
                                        fieldWithPath("result.data.productDtoList[]").type(JsonFieldType.ARRAY).description("?????? ??????"),
                                        fieldWithPath("result.data.productDtoList[].id").type(JsonFieldType.NUMBER).description("?????? id"),
                                        fieldWithPath("result.data.productDtoList[].categoryId").type(JsonFieldType.NUMBER).description("?????? ???????????? id"),
                                        fieldWithPath("result.data.productDtoList[].name").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("result.data.productDtoList[].price").type(JsonFieldType.NUMBER).description("?????? ??????"),
                                        fieldWithPath("result.data.productDtoList[].displayName").type(JsonFieldType.STRING).description("?????? ?????????"),
                                        fieldWithPath("result.data.productDtoList[].stock").type(JsonFieldType.NUMBER).description("?????? ??????"),
                                        fieldWithPath("result.data.productDtoList[].deadline").type(JsonFieldType.STRING).description("?????? ????????????"),
                                        fieldWithPath("result.data.productDtoList[].thumbnail").type(JsonFieldType.STRING).description("?????? ?????????"),
                                        fieldWithPath("result.data.productDtoList[].sellerId").type(JsonFieldType.NUMBER).description("?????? ?????? ?????????"),
                                        fieldWithPath("result.data.productDtoList[].deliveryFee").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                        fieldWithPath("result.data.totalCount").type(JsonFieldType.NUMBER).description("?????? ??????")
                                )
                        )
                );
    }
    @Test
    @DisplayName("???????????? ?????? ?????? ??? ?????? ??????")
    public void productBySellerSorting() throws Exception{

        this.mockMvc.perform(get("/product/main/seller/all/{sortingId}/{currentpage}",1,1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-rest-controller/product-main-seller-all",
                                pathParameters(
                                        parameterWithName("sortingId").description("?????? ??????"),
                                        parameterWithName("currentpage").description("?????? ?????????")
                                ),
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("????????????"),
                                        fieldWithPath("result.data.productDtoList[]").type(JsonFieldType.ARRAY).description("?????? ??????"),
                                        fieldWithPath("result.data.productDtoList[].id").type(JsonFieldType.NUMBER).description("?????? id"),
                                        fieldWithPath("result.data.productDtoList[].categoryId").type(JsonFieldType.NUMBER).description("?????? ???????????? id"),
                                        fieldWithPath("result.data.productDtoList[].name").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("result.data.productDtoList[].price").type(JsonFieldType.NUMBER).description("?????? ??????"),
                                        fieldWithPath("result.data.productDtoList[].displayName").type(JsonFieldType.STRING).description("?????? ?????????"),
                                        fieldWithPath("result.data.productDtoList[].stock").type(JsonFieldType.NUMBER).description("?????? ??????"),
                                        fieldWithPath("result.data.productDtoList[].deadline").type(JsonFieldType.STRING).description("?????? ????????????"),
                                        fieldWithPath("result.data.productDtoList[].thumbnail").type(JsonFieldType.STRING).description("?????? ?????????"),
                                        fieldWithPath("result.data.productDtoList[].sellerId").type(JsonFieldType.NUMBER).description("?????? ?????? ?????????"),
                                        fieldWithPath("result.data.productDtoList[].deliveryFee").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                        fieldWithPath("result.data.totalCount").type(JsonFieldType.NUMBER).description("?????? ??????")
                                )
                        )
                );
    }
    @Test
    @DisplayName("?????? ?????? ?????? ?????? ??? ?????? ??????")
    public void productByTotalSearch() throws Exception{

        this.mockMvc.perform(get("/product/totalSearch/{totalSearch}/{sortingId}/{currentpage}","???",1,1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-rest-controller/product-totalSearch",
                                pathParameters(
                                        parameterWithName("totalSearch").description("?????? ??????"),
                                        parameterWithName("sortingId").description("?????? ??????"),
                                        parameterWithName("currentpage").description("?????? ?????????")
                                ),
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("????????????"),
                                        fieldWithPath("result.data.productDtoList[]").type(JsonFieldType.ARRAY).description("?????? ??????"),
                                        fieldWithPath("result.data.productDtoList[].id").type(JsonFieldType.NUMBER).description("?????? id"),
                                        fieldWithPath("result.data.productDtoList[].categoryId").type(JsonFieldType.NUMBER).description("?????? ???????????? id"),
                                        fieldWithPath("result.data.productDtoList[].name").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("result.data.productDtoList[].price").type(JsonFieldType.NUMBER).description("?????? ??????"),
                                        fieldWithPath("result.data.productDtoList[].displayName").type(JsonFieldType.STRING).description("?????? ?????????"),
                                        fieldWithPath("result.data.productDtoList[].stock").type(JsonFieldType.NUMBER).description("?????? ??????"),
                                        fieldWithPath("result.data.productDtoList[].deadline").type(JsonFieldType.STRING).description("?????? ????????????"),
                                        fieldWithPath("result.data.productDtoList[].thumbnail").type(JsonFieldType.STRING).description("?????? ?????????"),
                                        fieldWithPath("result.data.productDtoList[].sellerId").type(JsonFieldType.NUMBER).description("?????? ?????? ?????????"),
                                        fieldWithPath("result.data.productDtoList[].deliveryFee").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                        fieldWithPath("result.data.totalCount").type(JsonFieldType.NUMBER).description("?????? ??????")
                                )
                        )
                );
    }

    @Test
    @DisplayName("?????? ?????? ??????")
    public void productDetail() throws Exception {

        this.mockMvc.perform(get("/product/detail/{productId}",product1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-rest-controller/product-detail",
                        pathParameters(
                                parameterWithName("productId").description("?????? ?????? ??????")
                        ),
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("????????????"),
                                        fieldWithPath("result.data.id").type(JsonFieldType.NUMBER).description("?????? id"),
                                        fieldWithPath("result.data.categoryId").type(JsonFieldType.NUMBER).description("???????????? id"),
                                        fieldWithPath("result.data.productDetailList").type(JsonFieldType.ARRAY).description("?????? ????????? ?????????"),
                                        fieldWithPath("result.data.productDetailList[].id").type(JsonFieldType.NUMBER).description("?????? ????????? ????????? id"),
                                        fieldWithPath("result.data.productDetailList[].image").type(JsonFieldType.STRING).description("?????? ????????? ????????? url"),
                                        fieldWithPath("result.data.productDetailList[].orders").type(JsonFieldType.NUMBER).description("?????? ????????? ????????? ??????"),
                                        fieldWithPath("result.data.name").type(JsonFieldType.STRING).description("?????? ??????"),
                                        fieldWithPath("result.data.price").type(JsonFieldType.NUMBER).description("?????? ??????"),
                                        fieldWithPath("result.data.displayName").type(JsonFieldType.STRING).description("?????? ?????????"),
                                        fieldWithPath("result.data.detailFlag").type(JsonFieldType.STRING).description("????????? ??????"),
                                        fieldWithPath("result.data.templateRec").type(JsonFieldType.OBJECT).description("????????? ?????????").optional(),
                                        fieldWithPath("result.data.stock").type(JsonFieldType.NUMBER).description("?????? ??????"),
                                        fieldWithPath("result.data.deadline").type(JsonFieldType.STRING).description("?????? ????????????"),
                                        fieldWithPath("result.data.thumbnail").type(JsonFieldType.STRING).description("?????? ?????? ?????????"),
                                        fieldWithPath("result.data.sellerId").type(JsonFieldType.NUMBER).description("?????? id"),
                                        fieldWithPath("result.data.deliveryFee").type(JsonFieldType.NUMBER).description("?????? ?????????")
                                )
                        )
                );
    }

    @Test
    @DisplayName("product ?????? ??????")
    public void findByProductNameAndStatusAndDate() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("productName","???");
        params.add("status","0");
        params.add("startDate","2020-01-01 11:11");
        params.add("endDate","2025-02-02 22:22");
        params.add("page","1");
        params.add("sellerId","1");

        this.mockMvc.perform(get("/product")
                        .params(params)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-rest-controller/find-by-name-status-date",
                                requestParameters(
                                        parameterWithName("productName").description("?????? ??????"),
                                        parameterWithName("status").description("?????? ?????? ??????"),
                                        parameterWithName("startDate").description("?????? ?????? ?????? ??????"),
                                        parameterWithName("endDate").description("?????? ?????? ?????? ??????"),
                                        parameterWithName("page").description("????????? ??????"),
                                        parameterWithName("sellerId").description("?????? ?????????")
                                ),
                                responseFields(
                                        fieldWithPath("productDtoList[].id").type(JsonFieldType.NUMBER).description("?????? id"),
                                        fieldWithPath("productDtoList[].categoryId").type(JsonFieldType.NUMBER).description("?????? ???????????? id"),
                                        fieldWithPath("productDtoList[].categoryName").type(JsonFieldType.STRING).description("?????? ???????????? ??????"),
                                        fieldWithPath("productDtoList[].price").type(JsonFieldType.NUMBER).description("?????? ??????"),
                                        fieldWithPath("productDtoList[].displayName").type(JsonFieldType.STRING).description("?????? ?????????"),
                                        fieldWithPath("productDtoList[].stock").type(JsonFieldType.NUMBER).description("?????? ??????"),
                                        fieldWithPath("productDtoList[].thumbnail").type(JsonFieldType.STRING).description("?????? ?????? ?????????"),
                                        fieldWithPath("productDtoList[].sellerId").type(JsonFieldType.NUMBER).description("?????? id"),
                                        fieldWithPath("productDtoList[].deliveryFee").type(JsonFieldType.NUMBER).description("?????? ?????????"),
                                        fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("total count")
                                )
                        )
                );
    }


    @Test
    @DisplayName("seller ???????????? ?????? ??????")
    public void findBySellerId() throws Exception {

        this.mockMvc.perform(get("/product/seller")
                        .header("sellerId",sellerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("product-rest-controller/find-by-seller-id",

                                requestHeaders(
                                    headerWithName("sellerId").description("seller id")
                                ),
                                responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("????????????"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("????????????"),
                                        fieldWithPath("result.data[].id").type(JsonFieldType.NUMBER).description("?????? id"),
                                        fieldWithPath("result.data[].name").type(JsonFieldType.STRING).description("?????? id"),
                                        fieldWithPath("result.data[].categoryId").type(JsonFieldType.NUMBER).description("?????? ???????????? id"),
                                        fieldWithPath("result.data[].price").type(JsonFieldType.NUMBER).description("?????? ??????"),
                                        fieldWithPath("result.data[].displayName").type(JsonFieldType.STRING).description("?????? ?????????"),
                                        fieldWithPath("result.data[].deadline").type(JsonFieldType.STRING).description("?????? ?????????"),
                                        fieldWithPath("result.data[].stock").type(JsonFieldType.NUMBER).description("?????? ??????"),
                                        fieldWithPath("result.data[].thumbnail").type(JsonFieldType.STRING).description("?????? ?????? ?????????"),
                                        fieldWithPath("result.data[].sellerId").type(JsonFieldType.NUMBER).description("?????? id"),
                                        fieldWithPath("result.data[].deliveryFee").type(JsonFieldType.NUMBER).description("?????? ?????????")
                                )
                        )
                );
    }
}