package shop.kokodo.productservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class TemplateRec extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_rec_id")
    private long id;

    @OneToOne(fetch = LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "product_id")
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

}
