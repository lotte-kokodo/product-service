package shop.kokodo.productservice.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReviewTotalDto {
    Double totalRate;
    long reviewCnt;

    @Builder
    public ReviewTotalDto(Double totalRate, long reviewCnt) {
        this.totalRate = totalRate;
        this.reviewCnt = reviewCnt;
    }
}
