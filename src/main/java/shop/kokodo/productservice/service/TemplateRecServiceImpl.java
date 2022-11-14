package shop.kokodo.productservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.kokodo.productservice.entity.TemplateRec;
import shop.kokodo.productservice.repository.TemplateRecRepository;

@Service
@Transactional(readOnly = true)
public class TemplateRecServiceImpl implements TemplateRecService {

    private final TemplateRecRepository templateRecRepository;


    public TemplateRecServiceImpl(TemplateRecRepository templateRecRepository) {
        this.templateRecRepository = templateRecRepository;
    }

    @Override
    public TemplateRec findByProductId(long productId) {
        return templateRecRepository.findByProductId(productId);
    }
}
