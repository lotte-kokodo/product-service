package shop.kokodo.productservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.data.domain.*;
import shop.kokodo.productservice.dto.ProductDto;
import shop.kokodo.productservice.entity.Category;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.repository.CategoryRepository;
import shop.kokodo.productservice.repository.ProductRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.WARN)
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @InjectMocks
    ProductServiceImpl productServiceImpl;

    @Mock
    ProductRepository productRepository;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    CircuitBreaker circuitBreaker;

    Category category;
    Category category1;
    Product product;
    Product product1;

    List<Product> productList;
    final LocalDateTime localDateTime = LocalDateTime.of(2022,11,26,0,0);
    Pageable pageable;
    Page<Product> pages;

    @BeforeEach
    public void setUp() {
        category = Category.builder()
                .name("healthy")
                .build();

        category1 = Category.builder()
                .name("power")
                .build();

        product = Product.builder()
                .id(1L)
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

        product1 = Product.builder()
                .id(2L)
                .category(category1)
                .name("소닭")
                .price(1000)
                .displayName("소닭")
                .stock(10)
                .deadline(localDateTime)
                .thumbnail("소닭")
                .sellerId(2)
                .deliveryFee(1000)
                .build();

        categoryRepository.save(category);
        categoryRepository.save(category1);
        productRepository.save(product);
        productRepository.save(product1);

        productList = new ArrayList<>();
        productList.add(product);
        productList.add(product1);

        pageable = PageRequest.of(0,20);
        pages = new PageImpl<>(productList);
    }

    @Test
    @DisplayName("상품 ID로 삭제 성공")
    void deleteProduct() {
        //given
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        //when
        productServiceImpl.deleteProduct(product.getId());

        //then
        Mockito.verify(productRepository).deleteById(product.getId());
    }

    @Test
    @DisplayName("단일 상품 조회 성공")
    void findById() {
        // given
        doReturn(Optional.of(product)).when(productRepository).findById(1L);

        // when
        Optional<Product> pr = productServiceImpl.findById(1L);

        // then
        assertThat(pr.get().getName()).isEqualTo("맛닭");
    }

    @Test
    @DisplayName("전체 상품 조회 성공")
    void findAll() {
        // given
        doReturn(productList).when(productRepository).findAll();

        // when
        List<ProductDto> productDtos = productServiceImpl.findAll();

        // then
        assertThat(productDtos.size()).isEqualTo(productList.size());
    }

    @Test
    @DisplayName("카테고리 별 상품 조회 성공")
    void findProductByCategory() {
        // given
        when(productRepository.findProductByCategory(category.getId(),pageable)).thenReturn(pages);

        // when
        Page<Product> products = productServiceImpl.findProductByCategory(category.getId(),0);

        // then
        assertThat(products.getSize()).isEqualTo(pages.getSize());
    }

    @Test
    @DisplayName("신상품 조회 성공")
    void findProductByNew() {
        // given
        LocalDate now = LocalDate.now();
        String month = monthStr(now.getMonthValue());
        String lastMonth = monthStr(now.minusMonths(1).getMonthValue());

        when(productRepository.findProductByNew(month,lastMonth,pageable)).thenReturn(pages);

        // when
        Page<Product> products = productServiceImpl.findProductByNew(0);

        // then
        assertThat(products.getSize()).isEqualTo(pages.getSize());
    }

    @Test
    @DisplayName("타임세일 조회 성공")
    void findProductBySale() {
        // given
        LocalDate now = LocalDate.now();
        int dayOfMonth = now.getDayOfMonth();
        String nextMonth = monthStr(now.plusMonths(1).getMonthValue());
        String month = monthStr(now.getMonthValue());
        String day = dayOfMonth < 10 ? "0" + (dayOfMonth) : Integer.toString(dayOfMonth);

        when(productRepository.findProductBySale(nextMonth,month,day,pageable)).thenReturn(pages);

        // when
        Page<Product> products = productServiceImpl.findProductBySale(0);

        // then
        assertThat(products.getSize()).isEqualTo(pages.getSize());
    }

    @Test
    @DisplayName("MD추천 조회 성공")
    void findProductBySeller() {
        // given
        when( productRepository.findProductBySeller(pageable)).thenReturn(pages);

        // when
        Page<Product> products = productServiceImpl.findProductBySeller(0);

        // then
        assertThat(products.getSize()).isEqualTo(pages.getSize());
    }

    @Test
    @DisplayName("전체 상품에서 상품 이름으로 검색 성공")
    void findProductByTotalSearch() {
        // given
        when( productRepository.findProductByTotalSearch("닭",pageable)).thenReturn(pages);

        // when
        Page<Product> products = productServiceImpl.findProductByTotalSearch("닭",0);

        // then
        assertThat(products.getSize()).isEqualTo(pages.getSize());
    }

    @Test
    @DisplayName("전체 상품에서 상품 이름으로 검색 성공")
    void findBySellerId() {
        // given
        when( productRepository.findProductByTotalSearch("닭",pageable)).thenReturn(pages);

        // when
        Page<Product> products = productServiceImpl.findProductByTotalSearch("닭",0);

        // then
        assertThat(products.getSize()).isEqualTo(pages.getSize());
    }



    public String monthStr (int monthValue) {
        return monthValue < 10 ? "0"+monthValue : Integer.toString(monthValue);
    }
}