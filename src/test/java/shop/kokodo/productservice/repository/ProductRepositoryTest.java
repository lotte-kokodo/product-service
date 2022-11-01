package shop.kokodo.productservice.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shop.kokodo.productservice.entity.Category;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.entity.ProductDetail;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import shop.kokodo.productservice.feign.repository.ProductFeignRepository;

@SpringBootTest
@Transactional
public class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductFeignRepository productFeignRepository;

    @Autowired
    CategoryRepository categoryRepository;

    Category category;
    Category category1;
    Product product1;
    Product product2;
    Product product3;

    ProductDetail productDetail1;
    ProductDetail productDetail2;
    ProductDetail productDetail3;
    final LocalDateTime localDateTime = LocalDateTime.of(2022,10,25,0,0);

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
                .category(category)
                .name("맛닭볶음밥")
                .price(1000)
                .displayName("맛닭볶음밥")
                .stock(10)
                .deadline(localDateTime)
                .thumbnail("맛닭볶음밥")
                .sellerId(1)
                .deliveryFee(1000)
                .build();

        product3 = Product.builder()
                .category(category1)
                .name("맛프로틴")
                .price(1000)
                .displayName("맛프로틴")
                .stock(10)
                .deadline(localDateTime)
                .thumbnail("맛프로틴")
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
    @DisplayName("Category ID로 상품 조회 성공")
    void findProductByCategory() {
        //given
        categoryRepository.save(category);
        categoryRepository.save(category1);

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);

        //when
        List<Product> productList = productRepository.findProductByCategory(category.getId());
        productList.forEach(System.out::println);

        //then
        Assertions.assertEquals(productList.size(), 2);
    }

    @Test
    @DisplayName("상품 이름 검색으로 상품 조회 성공")
    void findProductByTotalSearch() {
        //given
        categoryRepository.save(category);
        categoryRepository.save(category1);

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);

        //when
        List<Product> productList = productRepository.findProductByTotalSearch("닭");
        productList.forEach(System.out::println);

        //then
        Assertions.assertEquals(productList.size(), productList.size());
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

}