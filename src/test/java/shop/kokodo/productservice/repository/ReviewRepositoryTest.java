package shop.kokodo.productservice.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import shop.kokodo.productservice.dto.MypageReviewDto;
import shop.kokodo.productservice.entity.Category;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.entity.Review;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
public class ReviewRepositoryTest {

    @Autowired ProductRepository productRepository;
    @Autowired ReviewRepository reviewRepository;

    Category category;
    Product product;
    Review review1;
    Review review2;
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

        review1 = Review.builder()
                .content("리뷰1")
                .rating(5.0)
                .product(product)
                .memberId(1)
                .build();

        review2 = Review.builder()
                .content("리뷰2")
                .rating(4.5)
                .product(product)
                .memberId(1)
                .build();

        productRepository.save(product);
    }

    @Test
    @DisplayName("상품의 리뷰 조회")
    public void findByProductId(){
        reviewRepository.save(review1);
        reviewRepository.save(review2);

        List<Review> reviews = reviewRepository.findByProductId(product.getId());

        Assertions.assertEquals(reviews.size(),2);
    }

    @Test
    @DisplayName("member id로 찾기")
    public void findByMemberId(){
        reviewRepository.save(review1);
        reviewRepository.save(review2);

        List<MypageReviewDto> reviews = reviewRepository.findByMemberId(1);

        Assertions.assertEquals(reviews.size(),2);

        for (MypageReviewDto review : reviews) {
            System.out.println(review.toString());
        }

    }
}
