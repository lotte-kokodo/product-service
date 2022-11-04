package shop.kokodo.productservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.kokodo.productservice.circuitbreaker.AllCircuitBreaker;
import shop.kokodo.productservice.dto.MypageReviewDto;
import shop.kokodo.productservice.dto.ReviewRequestDto;
import shop.kokodo.productservice.dto.ReviewResponseDto;
import shop.kokodo.productservice.dto.UserDto;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.entity.Review;
import shop.kokodo.productservice.feign.UserServiceClient;
import shop.kokodo.productservice.repository.ProductRepository;
import shop.kokodo.productservice.repository.ReviewCustomRepository;
import shop.kokodo.productservice.repository.ReviewRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserServiceClient userServiceClient;
    private final CircuitBreaker circuitBreaker = AllCircuitBreaker.createSellerCircuitBreaker();

    @Override
    public List<ReviewResponseDto> findByProductId(long productId, int page) {

        productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품"));

        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>();

        Pageable pageable = PageRequest.of(page,5);
        Page<Review> reviewList=reviewRepository.findByProductIdPaging(productId,pageable);
        for (Review review : reviewList) {
            reviewResponseDtoList.add(convertToReviewResponse(review));
        }

        return reviewResponseDtoList;
    }

    @Transactional
    @Override
    public Review save(ReviewRequestDto reviewDto) {

        Review review = convertToReviewRequest(reviewDto);
        return reviewRepository.save(review);
    }

    @Override
    public List<MypageReviewDto> findByMemberId(long memberId) {
        return reviewRepository.findByMemberId(memberId);
    }

    @Override
    public Double calcTotalRate(long productId) {
        List<Review> reviewList = reviewRepository.findByProductId(productId);
        double sum=0;

        for (Review review : reviewList) {
            sum+=review.getRating();
        }

        double rate=sum/reviewList.size();
        return rate;
    }

    @Override
    public long countReview(long productId) {
        return reviewRepository.findByProductId(productId).size();
    }

    private ReviewResponseDto convertToReviewResponse(Review review){

        UserDto userDto = circuitBreaker.run(()-> userServiceClient.findMemberName(review.getMemberId())
                ,throwable -> new UserDto("사용자",""));


        return ReviewResponseDto.builder()
                .content(review.getContent())
                .memberName(userDto.getLoginId())
                .rating(review.getRating())
                .build();
    }

    private Review convertToReviewRequest(ReviewRequestDto reviewDto){
        Product product= productRepository.findById(reviewDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품"));

        return Review.builder()
                .content(reviewDto.getContent())
                .rating(reviewDto.getRating())
                .product(product)
                .memberId(reviewDto.getMemberId())
                .build();
    }


}
