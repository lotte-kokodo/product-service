package shop.kokodo.productservice.service;

import org.springframework.data.domain.Pageable;
import shop.kokodo.productservice.dto.MypageReviewDto;
import shop.kokodo.productservice.dto.ReviewRequestDto;
import shop.kokodo.productservice.dto.ReviewResponseDto;
import shop.kokodo.productservice.entity.Review;

import java.util.List;

public interface ReviewService {

    public List<ReviewResponseDto> findByProductId(long productId, int page);

    public Review save(ReviewRequestDto reviewDto);

    public List<MypageReviewDto> findByMemberId(long memberId);

    public Double calcTotalRate(long productId);

    public long countReview(long productId);
}
