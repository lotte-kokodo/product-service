package shop.kokodo.productservice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProductInquireResponseDto {

    private long id;
    private long productId;
    private String question;
    private String answer;
    private String memberName;

    @Builder
    public ProductInquireResponseDto(long id, long productId, String question, String answer, String memberName) {
        this.id = id;
        this.productId = productId;
        this.question = question;
        this.answer = answer;
        this.memberName = memberName;
    }
}
