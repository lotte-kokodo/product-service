package shop.kokodo.productservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PageMypageReviewDto {
    List<MypageReviewDto> mypageReviewDtoList;
    long totalCount;

    public PageMypageReviewDto(List<MypageReviewDto> mypageReviewDtoList, long totalCount) {
        this.mypageReviewDtoList = mypageReviewDtoList;
        this.totalCount = totalCount;
    }
}
