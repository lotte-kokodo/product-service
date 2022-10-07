package shop.kokodo.productservice.service;

import shop.kokodo.productservice.dto.ReviewRequestDto;
import shop.kokodo.productservice.dto.ReviewResponseDto;
import shop.kokodo.productservice.entity.Review;

import java.util.List;

public interface ReviewService {

    public List<ReviewResponseDto> findByProductId(long productId);

    public Review save(ReviewRequestDto reviewDto);

    public List<Review> findByMemberId(long memberId);
}