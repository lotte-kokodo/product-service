package shop.kokodo.productservice.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReviewTotalDto {
    String totalRate;
    long reviewCnt;

    @Builder
    public ReviewTotalDto(String totalRate, long reviewCnt) {
        this.totalRate = totalRate;
        this.reviewCnt = reviewCnt;
    }
}
