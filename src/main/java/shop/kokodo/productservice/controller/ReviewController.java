package shop.kokodo.productservice.controller;

import feign.Param;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.kokodo.productservice.dto.MypageReviewDto;
import shop.kokodo.productservice.dto.ReviewRequestDto;
import shop.kokodo.productservice.dto.ReviewTotalDto;
import shop.kokodo.productservice.dto.response.Response;
import shop.kokodo.productservice.entity.Review;
import shop.kokodo.productservice.service.ReviewService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/{productId}")
    public Response findByProductId(@PathVariable long productId, @RequestParam("page") int page){
        return Response.success(reviewService.findByProductId(productId,page));
    }

    @PostMapping
    public Response save(@RequestBody ReviewRequestDto reviewDto, @RequestHeader long memberId){
        reviewDto.setMemberId(memberId);
        ReviewRequestDto saveReview = convertToReviewDto(reviewService.save(reviewDto));
        return Response.success(saveReview);
    }

    private ReviewRequestDto convertToReviewDto(Review review){
        return ReviewRequestDto.builder()
                .rating(review.getRating())
                .content(review.getContent())
                .productId(review.getProduct().getId())
                .build();
    }

    /* 마이페이지 사용자 별 리뷰 조회 */
    @GetMapping("/member/{memberId}")
    public ResponseEntity findByMemberId(@PathVariable("memberId") long memberId){
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.findByMemberId(memberId));
    }

    @GetMapping("/total/{productId}")
    public Response getTotalRate(@PathVariable long productId){
        Double totalRate = reviewService.calcTotalRate(productId);
        long reviewCnt = reviewService.countReview(productId);


        ReviewTotalDto reviewTotalDto = ReviewTotalDto.builder()
                .totalRate(Math.round(totalRate * 10) / 10.0)
                .reviewCnt(reviewCnt)
                .build();


        return Response.success(reviewTotalDto);
    }


}
