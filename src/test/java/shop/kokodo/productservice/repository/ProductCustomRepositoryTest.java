package shop.kokodo.productservice.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import shop.kokodo.productservice.dto.ProductDto;
import shop.kokodo.productservice.entity.Category;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.entity.ProductDetail;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
public class ProductCustomRepositoryTest {

    private final ProductCustomRepository productCustomRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Autowired
    public ProductCustomRepositoryTest(ProductCustomRepository productCustomRepository, CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.productCustomRepository = productCustomRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    Category category;
    Product product1;
    Product product2;
    final LocalDateTime localDateTime = LocalDateTime.of(2022,10,25,0,0);
    final LocalDateTime startDateTime = LocalDateTime.of(2022,9,1,0,0);
    final LocalDateTime endDateTime = LocalDateTime.of(2022,11,1,0,0);

    @BeforeEach
    public void setUp() {
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

        product2 = Product.builder()
                .category(category)
                .name("맛닭2")
                .price(1000)
                .displayName("맛닭2")
                .stock(0)
                .deadline(localDateTime)
                .thumbnail("맛닭2")
                .sellerId(1)
                .deliveryFee(1000)
                .build();

        categoryRepository.save(category);
        productRepository.save(product1);
        productRepository.save(product2);
    }
    
    @Test
    @DisplayName("상품 조건 검색(name, status, regdate)")
    public void findByNameAndStock(){
        Pageable pageable = PageRequest.of(0,10);
        List<ProductDto> products = productCustomRepository.findProduct("맛",1,startDateTime,endDateTime,1L,pageable);

        for (ProductDto product : products) {
            Assertions.assertEquals(product.getName().contains("맛"),true);
            Assertions.assertEquals(product.getStock()>0,true);
            Assertions.assertEquals(product.getSellerId(),1);
        }

//
    }


}
