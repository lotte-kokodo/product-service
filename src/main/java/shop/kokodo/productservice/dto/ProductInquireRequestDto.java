package shop.kokodo.productservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductInquireRequestDto {

    private long id;
    private long productId;
    private String question;
    private String answer;
    private long memberId;


    public ProductInquireRequestDto(long id, long productId, String question, String answer, long memberId) {
        this.id = id;
        this.productId = productId;
        this.question = question;
        this.answer = answer;
        this.memberId = memberId;
    }
}
