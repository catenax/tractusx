package net.catenax.prs.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Sample API")
@Slf4j
@RestController
public class PrsController {

    @GetMapping("api/hello")
    public String hello(){
        return "Hello World!";
    }

}
