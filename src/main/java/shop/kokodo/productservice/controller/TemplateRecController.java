package shop.kokodo.productservice.controller;


import org.springframework.web.bind.annotation.*;
import shop.kokodo.productservice.dto.response.Response;
import shop.kokodo.productservice.entity.TemplateRec;
import shop.kokodo.productservice.service.TemplateRecService;

@RestController
@RequestMapping("/templateRec")
public class TemplateRecController {

    private final TemplateRecService templateRecService;

    public TemplateRecController(TemplateRecService templateRecService) {
        this.templateRecService = templateRecService;
    }

    @GetMapping("/{productId}")
    public Response findByProductId(@PathVariable("productId")long productId){
        System.out.println("TemplateRecController.findByProductId");
        return Response.success(templateRecService.findByProductId(productId));
    }
}
