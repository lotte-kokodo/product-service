package shop.kokodo.productservice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PagingReviewDto {

    List<ReviewResponseDto> reviewResponseDtoList;

    long totalCount;

    @Builder
    public PagingReviewDto(List<ReviewResponseDto> reviewResponseDtoList, long totalCount) {
        this.reviewResponseDtoList = reviewResponseDtoList;
        this.totalCount = totalCount;
    }
}
