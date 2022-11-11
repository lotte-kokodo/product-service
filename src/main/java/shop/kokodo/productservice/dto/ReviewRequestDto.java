package shop.kokodo.productservice.dto;

import lombok.*;
import org.springframework.stereotype.Service;

@Getter
@Setter
@AllArgsConstructor
public class ReviewRequestDto {
    private Long productId;
    private String content;
    private double rating;
    private long memberId;

    public ReviewRequestDto() {
    }

    @Builder
    public ReviewRequestDto( Long productId, String content, double rating) {
        this.productId = productId;
        this.content = content;
        this.rating = rating;
    }
}
