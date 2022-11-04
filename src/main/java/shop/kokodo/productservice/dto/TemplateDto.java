package shop.kokodo.productservice.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TemplateDto {

    private String imageOne;
    private String imageTwo;
    private String imageThree;
    private String imageFour;
    private String imageFive;

    private String writingTitle;
    private String writingTitleDetail;
    private String writingHighlightOne;
    private String writingHighlightTwo;
    private String writingName;
    private String writingDescription;

    @Builder
    public TemplateDto(String imageOne, String imageTwo, String imageThree, String imageFour, String imageFive,
                       String writingTitle, String writingTitleDetail, String writingHighlightOne, String writingHighlightTwo, String writingName, String writingDescription) {
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
