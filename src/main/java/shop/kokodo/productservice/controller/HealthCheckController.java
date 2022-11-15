package shop.kokodo.productservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/health")
    public HttpStatus healthCheck() {
        return HttpStatus.OK;
    }
}
