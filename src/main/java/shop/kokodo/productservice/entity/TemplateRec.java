package shop.kokodo.productservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter
@Entity
@NoArgsConstructor
public class TemplateRec extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_rec_id")
    private long id;

    @OneToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;
    @Column( nullable = false )
    private String imageOne;
    @Column( nullable = false )
    private String imageTwo;
    @Column( nullable = false )
    private String imageThree;
    @Column( nullable = false )
    private String imageFour;
    @Column( nullable = false )
    private String imageFive;

    @Column( nullable = false )
    private String writingTitle;
    @Column( nullable = false )
    private String writingTitleDetail;
    @Column( nullable = false )
    private String writingHighlightOne;
    @Column( nullable = false )
    private String writingHighlightTwo;
    @Column( nullable = false )
    private String writingName;
    @Column( nullable = false )
    private String writingDescription;

    public void changeProduct(Product product){
        this.product=product;
        product.setTemplateRec(this);
    }

    @Builder
    public TemplateRec(long id, String imageOne, String imageTwo, String imageThree, String imageFour, String imageFive,
                       String writingTitle, String writingTitleDetail, String writingHighlightOne, String writingHighlightTwo, String writingName, String writingDescription) {
        this.id = id;
        this.imageOne = imageOne;
        this.imageTwo = imageTwo;
        this.imageThree = imageThree;
        this.imageFour = imageFour;
        this.imageFive = imageFive;
        this.writingTitle = writingTitle;
        this.writingTitleDetail = writingTitleDetail;
        this.writingHighlightOne = writingHighlightOne;
        this.writingHighlightTwo = writingHighlightTwo;
        this.writingName = writingName;
        this.writingDescription = writingDescription;
    }
}
