package org.example.controller;

import com.ajaxjs.api.security.referer.HttpRefererCheck;
import com.ajaxjs.api.time_signature.TimeSignatureVerify;
import org.example.model.Foo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/foo")

public interface FooController {
    @GetMapping
    @HttpRefererCheck
    Foo getFoo();
}
