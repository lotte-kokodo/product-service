package shop.kokodo.productservice.service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.kokodo.productservice.dto.ReviewRequestDto;
import shop.kokodo.productservice.dto.ReviewResponseDto;
import shop.kokodo.productservice.entity.Product;
import shop.kokodo.productservice.entity.Review;
import shop.kokodo.productservice.feign.UserServiceClient;
import shop.kokodo.productservice.repository.ProductRepository;
import shop.kokodo.productservice.repository.ReviewRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
//    private final UserServiceClient userServiceClient;

    @Override
    public List<ReviewResponseDto> findByProductId(long productId) {

        productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품"));

        List<ReviewResponseDto> reviewResponseDtoList = new ArrayList<>();
        List<Review> reviewList=reviewRepository.findByProductId(productId);
        for (Review review : reviewList) {
            reviewResponseDtoList.add(convertToReviewResponse(review));
        }

        return reviewResponseDtoList;
    }

    @Override
    public Review save(ReviewRequestDto reviewDto) {

        Review review = convertToReviewRequest(reviewDto);
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> findByMemberId(long memberId) {

        return reviewRepository.findByMemberId(memberId);
    }

    @Override
    public String calcTotalRate(long productId) {
        List<Review> reviewList = reviewRepository.findByProductId(productId);
        double sum=0;

        for (Review review : reviewList) {
            sum+=review.getRating();
        }

        double rate=sum/reviewList.size();
        return String.format("%.1f",rate);
    }

    @Override
    public long countReview(long productId) {
        return reviewRepository.countByProductId(productId);
    }

    private ReviewResponseDto convertToReviewResponse(Review review){
//        String memberName = userServiceClient.findMemberName(review.getMemberId());
        String memberName = " memberName";
        return ReviewResponseDto.builder()
                .content(review.getContent())
                .memberName(memberName)
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
