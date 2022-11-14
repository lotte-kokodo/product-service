package shop.kokodo.productservice.service;

import org.springframework.data.domain.Pageable;
import shop.kokodo.productservice.dto.*;
import shop.kokodo.productservice.entity.Review;

import java.util.List;

public interface ReviewService {

    public PagingReviewDto findByProductId(long productId, int page);

    public Review save(ReviewRequestDto reviewDto);

    public PageMypageReviewDto findByMemberId(long memberId, int page);

    public Double calcTotalRate(long productId);

    public long countReview(long productId);
}
