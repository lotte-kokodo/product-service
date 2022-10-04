package shop.kokodo.productservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import shop.kokodo.productservice.dto.ReviewRequestDto;
import shop.kokodo.productservice.dto.response.Response;
import shop.kokodo.productservice.entity.Review;
import shop.kokodo.productservice.service.ReviewService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/{productId}")
    public Response findByProductId(@PathVariable long productId){
        return Response.success(reviewService.findByProductId(productId));
    }

    @PostMapping
    public Response save(@RequestBody ReviewRequestDto reviewDto, @RequestHeader long memberId){
        reviewDto.setMemberId(memberId);
        ReviewRequestDto saveReview = convertToReviewDto(reviewService.save(reviewDto));
        return Response.success(saveReview);
    }

    private ReviewRequestDto convertToReviewDto(Review review){
        return ReviewRequestDto.builder()
                .id(review.getId())
                .rating(review.getRating())
                .content(review.getContent())
                .productId(review.getProduct().getId())
                .memberId(review.getMemberId())
                .build();
    }


}
