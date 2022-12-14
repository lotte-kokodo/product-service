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
import shop.kokodo.productservice.circuitbreaker.AllCircuitBreaker;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.*;

import shop.kokodo.productservice.dto.ProductDto;
import shop.kokodo.productservice.entity.Category;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.feign.SellerServiceClient;
import shop.kokodo.productservice.repository.CategoryRepository;
import shop.kokodo.productservice.repository.ProductCustomRepositoryImpl;
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

    List<Product> productList;
    final LocalDateTime localDateTime = LocalDateTime.of(2022,11,26,0,0);
    Pageable pageable;
    Page<Product> pages;

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
                .name("??????")
                .price(1000)
                .displayName("??????")
                .stock(10)
                .deadline(localDateTime)
                .thumbnail("??????")
                .sellerId(1)
                .deliveryFee(1000)
                .build();

        product1 = Product.builder()
                .id(2L)
                .category(category1)
                .name("??????")
                .price(1000)
                .displayName("??????")
                .stock(10)
                .deadline(localDateTime)
                .thumbnail("??????")
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
    @DisplayName("?????? ID??? ?????? ??????")
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
    @DisplayName("?????? ?????? ?????? ??????")
    void findById() {
        // given
        doReturn(Optional.of(product)).when(productRepository).findById(1L);

        // when
        Optional<Product> pr = productServiceImpl.findById(1L);

        // then
        assertThat(pr.get().getName()).isEqualTo("??????");
    }

    @Test
    @DisplayName("?????? ?????? ?????? ??????")
    void findAll() {
        // given
        doReturn(productList).when(productRepository).findAll();

        // when
        List<ProductDto> productDtos = productServiceImpl.findAll();

        // then
        assertThat(productDtos.size()).isEqualTo(productList.size());
    }

    @Test
    @DisplayName("???????????? ??? ?????? ?????? ??????")
    void findProductByCategory() {
        // given
        when(productRepository.findProductByCategory(category.getId(),pageable)).thenReturn(pages);

        // when
        Page<Product> products = productServiceImpl.findProductByCategory(category.getId(),0);

        // then
        assertThat(products.getSize()).isEqualTo(pages.getSize());
    }

    @Test
    @DisplayName("????????? ?????? ??????")
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
    @DisplayName("???????????? ?????? ??????")
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
    @DisplayName("MD?????? ?????? ??????")
    void findProductBySeller() {
        // given
        when( productRepository.findProductBySeller(pageable)).thenReturn(pages);

        // when
        Page<Product> products = productServiceImpl.findProductBySeller(0);

        // then
        assertThat(products.getSize()).isEqualTo(pages.getSize());
    }

    @Test
    @DisplayName("?????? ???????????? ?????? ???????????? ?????? ??????")
    void findProductByTotalSearch() {
        // given
        when( productRepository.findProductByTotalSearch("???",pageable)).thenReturn(pages);

        // when
        Page<Product> products = productServiceImpl.findProductByTotalSearch("???",0);

        // then
        assertThat(products.getSize()).isEqualTo(pages.getSize());
    }

    @Test
    @DisplayName("?????? ???????????? ?????? ???????????? ?????? ??????")
    void findBySellerId() {
        // given
        when( productRepository.findProductByTotalSearch("???",pageable)).thenReturn(pages);

        // when
        Page<Product> products = productServiceImpl.findProductByTotalSearch("???",0);

        // then
        assertThat(products.getSize()).isEqualTo(pages.getSize());
    }



    public String monthStr (int monthValue) {
        return monthValue < 10 ? "0"+monthValue : Integer.toString(monthValue);
    }
}