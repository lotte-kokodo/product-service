package shop.kokodo.productservice.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import shop.kokodo.productservice.dto.ProductDto;
import shop.kokodo.productservice.entity.Category;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.repository.CategoryRepository;
import shop.kokodo.productservice.repository.ProductRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

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
        category = Category.builder()
                .id(1L)
                .name("healthy")
                .build();

        category1 = Category.builder()
                .id(2L)
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
    @DisplayName("상품 생성 성공")
    void saveProduct() {
        doReturn(Optional.of(new Category())).when(categoryRepository).findById(1L);
        doReturn(product).when(productRepository).save(any());

        Product saveProduct = productServiceImpl.saveProduct(productDto);

        Assertions.assertEquals(saveProduct.getName(),product.getName());
        Assertions.assertEquals(saveProduct.getPrice(),product.getPrice());
    }

    @Test
    @DisplayName("상품 업데이트 성공")
    void updateProduct() {
        doReturn(product1).when(productRepository).save(any());

        Product saveProduct1 = productServiceImpl.saveProduct(productDto);
        Product saveProduct2 = productServiceImpl.updateProduct(productDto1);

        Assertions.assertEquals(saveProduct1.getName(),saveProduct2.getName());
        Assertions.assertEquals(saveProduct1.getPrice(),saveProduct2.getPrice());
    }

    @Test
    @DisplayName("상품 ID로 삭제 성공")
    void deleteProduct() {
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(any(Product.class));
        productServiceImpl.deleteProduct(product.getId());

        verify(productRepository, times(1)).delete(any(Product.class));
    }

    @Test
    @DisplayName("전체 상품 조회 성공")
    void findAll() {
        doReturn(productList).when(productRepository).findAll();
        productServiceImpl.findAll();
    }

    @Test
    @DisplayName("카테고리ID로 상품 조회 성공")
    void findProductByCategory() {
        doReturn(productList).when(productRepository).findProductByCategory(category.getId());
        productServiceImpl.findProductByCategory(category.getId());
    }

    @Test
    @DisplayName("전체 상품에서 상품 이름으로 검색 성공")
    void findProductByTotalSearch() {
        doReturn(productList).when(productRepository).findProductByTotalSearch("닭");
        productServiceImpl.findProductByTotalSearch("닭");
    }

    @Test
    @DisplayName("카테고리 내 상품에서 상품 이름으로 검색 성공")
    void findProductByCategorySearch() {
        doReturn(productList).when(productRepository).findProductByCategorySearch(category.getId(), "닭");
        productServiceImpl.findProductByCategorySearch(category.getId(), "닭");
    }
}