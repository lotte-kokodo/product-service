package shop.kokodo.productservice.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import shop.kokodo.productservice.circuitbreaker.AllCircuitBreaker;
import shop.kokodo.productservice.dto.ProductDto;
import shop.kokodo.productservice.entity.Category;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.feign.SellerServiceClient;
import shop.kokodo.productservice.repository.CategoryRepository;
import shop.kokodo.productservice.repository.ProductCustomRepository;
import shop.kokodo.productservice.repository.ProductCustomRepositoryImpl;
import shop.kokodo.productservice.repository.ProductRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.BDDMockito.given;
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
    ProductCustomRepositoryImpl productCustomRepository;

    @Mock
    SellerServiceClient sellerServiceClient;

    @Mock
    CircuitBreaker circuitBreaker;

    private static MockedStatic<AllCircuitBreaker> allCircuitBreakerMockedStatic;

    @BeforeAll
    public static void beforeClass() {
        allCircuitBreakerMockedStatic = mockStatic(AllCircuitBreaker.class);
    }

    @AfterAll
    public static void afterClass() {
        allCircuitBreakerMockedStatic.close();
    }

    Category category;
    Category category1;
    Product product;
    Product product1;
    ProductDto productDto;
    ProductDto productDto1;
    List<Product> productList;
    final LocalDateTime localDateTime = LocalDateTime.of(2022,10,25,0,0);

    @BeforeEach
    public void setUp() {

        when(AllCircuitBreaker.createSellerCircuitBreaker()).thenReturn(circuitBreaker);

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
                .id(1L)
                .category(category1)
                .name("소닭")
                .price(1000)
                .displayName("소닭")
                .stock(10)
                .deadline(localDateTime)
                .thumbnail("소닭")
                .sellerId(1)
                .deliveryFee(1000)
                .build();

        productDto = ProductDto.builder()
                .id(1L)
                .categoryId(category.getId())
                .name("맛닭")
                .price(1000)
                .displayName("맛닭")
                .stock(10)
                .deadline(localDateTime)
                .thumbnail("맛닭")
                .sellerId(1)
                .deliveryFee(1000)
                .build();

        productDto1 = ProductDto.builder()
                .id(1L)
                .categoryId(category.getId())
                .name("소닭")
                .price(1000)
                .displayName("소닭")
                .stock(10)
                .deadline(localDateTime)
                .thumbnail("소닭")
                .sellerId(1)
                .deliveryFee(1000)
                .build();

        productList = new ArrayList<>();
        productList.add(product);
    }

    @Test
    @DisplayName("상품 ID로 삭제 성공")
    void deleteProduct() {
        //given
        categoryRepository.save(category);
        productRepository.save(product);

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        //when
        productServiceImpl.deleteProduct(product.getId());

        //then
        Mockito.verify(productRepository).deleteById(product.getId());
    }

    @Test
    @DisplayName("전체 상품 조회 성공")
    void findAll() {
        // given
        doReturn(productList).when(productRepository).findAll();

        // when
        final List<ProductDto> productDtos = productServiceImpl.findAll();

        // then
        assertThat(productDtos.size()).isEqualTo(productList.size());
    }

    @Test
    @DisplayName("카테고리ID로 상품 조회 성공")
    void findProductByCategory() {
        // given
        doReturn(productList).when(productRepository).findProductByCategory(category.getId());

        // when
        final List<ProductDto> productDtos = productServiceImpl.findProductByCategory(category.getId());

        // then
        assertThat(productDtos.size()).isEqualTo(productList.size());
    }

    @Test
    @DisplayName("전체 상품에서 상품 이름으로 검색 성공")
    void findProductByTotalSearch() {
        // given
        doReturn(productList).when(productRepository).findProductByTotalSearch("닭");

        // when
        final List<ProductDto> productDtos = productServiceImpl.findProductByTotalSearch("닭");

        // then
        assertThat(productDtos.size()).isEqualTo(productList.size());
    }
}