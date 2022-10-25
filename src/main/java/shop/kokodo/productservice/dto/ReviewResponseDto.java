package shop.kokodo.productservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewResponseDto {

    private long id;
    private String content;
    private double rating;
    private String memberName;

    @Builder
    public ReviewResponseDto(long id, String content, double rating, String memberName) {
        this.id = id;
        this.content = content;
        this.rating = rating;
        this.memberName = memberName;
    }
}
