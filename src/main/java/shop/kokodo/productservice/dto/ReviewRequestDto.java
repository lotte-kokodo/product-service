package shop.kokodo.productservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@NoArgsConstructor
public class ReviewRequestDto {

    private long id;
    private long productId;
    private String content;
    private double rating;
    private long memberId;

    @Builder
    public ReviewRequestDto(long id, long productId, String content, double rating, long memberId) {
        this.id = id;
        this.productId = productId;
        this.content = content;
        this.rating = rating;
        this.memberId = memberId;
    }
}
