package shop.kokodo.productservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import shop.kokodo.productservice.dto.ReviewRequestDto;
import shop.kokodo.productservice.dto.ReviewTotalDto;
import shop.kokodo.productservice.dto.response.Response;
import shop.kokodo.productservice.entity.Review;
import shop.kokodo.productservice.service.ReviewService;

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
                .id(review.getId())
                .rating(review.getRating())
                .content(review.getContent())
                .productId(review.getProduct().getId())
                .memberId(review.getMemberId())
                .build();
    }

    @GetMapping("/member")
    public Response findByMemberId(@RequestHeader long memberId){
        return Response.success(reviewService.findByMemberId(memberId));
    }

    @GetMapping("/total/{productId}")
    public Response getTotalRate(@PathVariable long productId){
        Double totalRate = reviewService.calcTotalRate(productId);
        long reviewCnt = reviewService.countReview(productId);

        System.out.println(totalRate+" "+reviewCnt);

        ReviewTotalDto reviewTotalDto = ReviewTotalDto.builder()
                .totalRate(Math.round(totalRate * 10) / 10.0)
                .reviewCnt(reviewCnt)
                .build();

        System.out.println(reviewTotalDto.getTotalRate());

        return Response.success(reviewTotalDto);
    }


}
