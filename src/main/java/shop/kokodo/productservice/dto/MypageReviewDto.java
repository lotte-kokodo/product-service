package shop.kokodo.productservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MypageReviewDto {

    private long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;
    private long productId;
    private String content;
    private double rating;
    private long memberId;
    private String displayName;
    private String thumbnail;

    @Builder
    public MypageReviewDto(long id, LocalDateTime createdDate, long productId, String content, double rating, long memberId, String displayName, String thumbnail) {
        this.id = id;
        this.createdDate = createdDate;
        this.productId = productId;
        this.content = content;
        this.rating = rating;
        this.memberId = memberId;
        this.displayName = displayName;
        this.thumbnail = thumbnail;
    }
}
