package org.example.controller;

import org.example.model.Foo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/foo")
public interface FooController {
    @GetMapping
    Foo getFoo();
}
