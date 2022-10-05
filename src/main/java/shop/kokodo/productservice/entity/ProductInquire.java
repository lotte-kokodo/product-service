package shop.kokodo.productservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Getter
@Entity
@NoArgsConstructor
public class ProductInquire extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_inquire_id")
    private Long id;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    private String question;

    private String answer;
    private long memberId;

    @Builder
    public ProductInquire(Long id, Product product,String question, String answer, long memberId) {
        this.id = id;
        this.product = product;
        this.question=question;
        this.answer = answer;
        this.memberId = memberId;
    }

    public void answerQuestion(String answer){
        this.answer=answer;
    }
}
