package shop.kokodo.productservice.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shop.kokodo.productservice.dto.ProductInquireResponseDto;
import shop.kokodo.productservice.entity.Category;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.entity.ProductInquire;
import shop.kokodo.productservice.entity.Review;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
public class ProductInquireRepositoryTest {

    @Autowired ProductInquireRepository productInquireRepository;
    @Autowired ProductRepository productRepository;

    Category category;
    Product product;

    ProductInquire productInquire1;
    ProductInquire productInquire2;
    final LocalDateTime localDateTime = LocalDateTime.of(2022,10,25,0,0);

    @BeforeEach
    public void setUp(){
        category = Category.builder()
                .name("healthy")
                .build();

        product = Product.builder()
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

        productRepository.save(product);

        productInquire1=ProductInquire.builder()
                .question("question??")
                .product(product)
//                .answer("answer.")
                .memberId(1)
                .build();

        productInquire2=ProductInquire.builder()
                .question("question2??")
                .product(product)
                .answer("answer2.")
                .memberId(1)
                .build();

    }

    @Test
    @DisplayName("productId로 문의 조회")
    public void findByProductId(){

        productInquireRepository.save(productInquire1);
        productInquireRepository.save(productInquire2);

        List<ProductInquire> list = productInquireRepository.findByProductId(product.getId());

        Assertions.assertEquals(list.size(),2);
    }

    @Test
    @DisplayName("sellerId로 미응답 문의 조회 ")
    public void findBySellerId(){
        productInquireRepository.save(productInquire1);
        productInquireRepository.save(productInquire2);

        List<ProductInquire> list = productInquireRepository.findBySellerId(1);

        Assertions.assertEquals(list.size(),1);
    }

}
