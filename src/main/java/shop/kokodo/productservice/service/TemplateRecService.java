package shop.kokodo.productservice.service;

import shop.kokodo.productservice.entity.TemplateRec;

public interface TemplateRecService {


    TemplateRec findByProductId(long productId);
}
