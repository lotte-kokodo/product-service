package shop.kokodo.productservice.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import shop.kokodo.productservice.entity.Category;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.entity.ProductDetail;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import shop.kokodo.productservice.entity.TemplateRec;
import shop.kokodo.productservice.feign.repository.ProductFeignRepository;

@SpringBootTest
//@Transactional
public class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductFeignRepository productFeignRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
            TemplateRecRepository templateRecRepository;

    Category category;
    Category category1;
    Product product1;
    Product product2;
    Product product3;

    ProductDetail productDetail1;
    ProductDetail productDetail2;
    ProductDetail productDetail3;

    TemplateRec templateRec;
    final LocalDateTime localDateTime = LocalDateTime.of(2022,11,30,0,0);

    Pageable pageable;

    @BeforeEach
    public void setUp() {
        category = Category.builder()
                .name("healthy")
                .build();

        category1 = Category.builder()
                .name("power")
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

        product2 = Product.builder()
                .category(category1)
                .name("맛닭볶음밥")
                .price(1000)
                .displayName("맛닭볶음밥")
                .stock(10)
                .deadline(localDateTime)
                .thumbnail("맛닭볶음밥")
                .sellerId(2)
                .deliveryFee(1000)
                .build();

        product3 = Product.builder()
                .category(category)
                .name("맛프로틴")
                .price(1000)
                .displayName("맛프로틴")
                .stock(10)
                .deadline(localDateTime)
                .thumbnail("맛프로틴")
                .sellerId(3)
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
        pageable = PageRequest.of(0,20);

        templateRec = TemplateRec.builder()
                .imageOne("image1")
                .imageTwo("image2")
                .imageThree("image3")
                .imageFour("image4")
                .imageFive("image5")
                .writingTitle("writingTitle")
                .writingDescription("writingDescription")
                .writingHighlightOne("writimgHighligntOne")
                .writingHighlightTwo("writingHighlightTwo")
                .writingName("writionName")
                .writingTitleDetail("writionTitleDetail")
                .build();

    }


    @Test
    @DisplayName("카테고리별 상품 조회")
    void findProductByCategory() {
        //given
        categoryRepository.save(category);
        categoryRepository.save(category1);

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);

        //when
        Page<Product> productList = productRepository.findProductByCategory(category.getId(),pageable);
        productList.forEach(System.out::println);

        //then
        Assertions.assertEquals(productList.getTotalElements(), 2);
    }

    /* ======= Main 화면 ======= */
    @Test
    @DisplayName("신상품 조회")
    void findProductByNew() {
        //given
        categoryRepository.save(category);
        categoryRepository.save(category1);

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);

        LocalDate now = LocalDate.now();
        String month = monthStr(now.getMonthValue());
        String lastMonth = monthStr(now.minusMonths(1).getMonthValue());

        //when
        Page<Product> productList = productRepository.findProductByNew(month,lastMonth,pageable);
        productList.forEach(System.out::println);

        //then
        Assertions.assertEquals(productList.getTotalElements(), 3);
    }

    @Test
    @DisplayName("Sale상품 조회")
    void findProductBySale() {
        //given
        categoryRepository.save(category);
        categoryRepository.save(category1);

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);

        LocalDate now = LocalDate.now();
        int dayOfMonth = now.getDayOfMonth();
        String nextMonth = monthStr(now.plusMonths(1).getMonthValue());
        String month = monthStr(now.getMonthValue());
        String day = dayOfMonth < 10 ? "0" + (dayOfMonth) : Integer.toString(dayOfMonth);

        //when
        Page<Product> productList = productRepository.findProductBySale(nextMonth,month,day,pageable);
        productList.forEach(System.out::println);

        //then
        Assertions.assertEquals(productList.getTotalElements(), 3);
    }

    @Test
    @DisplayName("MD 추천 상품 조회")
    void findProductBySeller() {
        //given
        categoryRepository.save(category);
        categoryRepository.save(category1);

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);

        //when
        Page<Product> productList = productRepository.findProductBySeller(pageable);
        productList.forEach(System.out::println);

        //then
        Assertions.assertEquals(productList.getTotalElements(), 3);
    }

    /* ======= 전체 상품 검색 ======= */
    @Test
    @DisplayName("전체 상품 검색")
    void findProductByTotalSearch() {
        //given
        categoryRepository.save(category);
        categoryRepository.save(category1);

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);

        //when
        Page<Product> productList = productRepository.findProductByTotalSearch("닭",pageable);
        productList.forEach(System.out::println);

        //then
        Assertions.assertEquals(productList.getTotalElements(), 2);
    }

    @Test
    @DisplayName("product detail 조회")
    public void findByProductId(){
        product1.addProductDetail(productDetail1);
        product1.addProductDetail(productDetail2);
        product1.addProductDetail(productDetail3);

        categoryRepository.save(category);

        productRepository.save(product1);

        Optional<Product> findProduct = productRepository.findById(product1.getId());

        Assertions.assertEquals(findProduct.isPresent(),true);
        Assertions.assertEquals(findProduct.get().getId(), product1.getId());
    }

    @Test
    @DisplayName("productId 리스트로 Product 객체 조회 성공")
    public void findProductListById(){
        List<Long> productIdList = new ArrayList<>();

         productIdList.add(productRepository.save(product1).getId());
         productIdList.add(productRepository.save(product2).getId());
         productRepository.save(product3).getId();

        List<Product> productList = productFeignRepository.findProductListById(productIdList);
        Assertions.assertEquals(productList.size(),2);
    }

    public String monthStr (int monthValue) {
        return monthValue < 10 ? "0"+monthValue : Integer.toString(monthValue);
    }


    @Test
    @DisplayName("상품, 상품 디테일 이미지 저장")
    public void saveProductAndDetail(){
        product1.addProductDetail(productDetail1);

        productRepository.save(product1);
        productDetail1.changeProduct(product1);

        System.out.println(productRepository.findById(product1.getId()));

    }

    @Test
    @DisplayName("상품, 상품 템플릿 저장")
    public void saveProductAndTemplate(){
        product1.setTemplateRec(templateRec);

        productRepository.save(product1);
        templateRec.changeProduct(product1);

        System.out.println(templateRecRepository.findAll());

    }
}