package com.skok1025.study.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/js")
public class JsTestController {

	@GetMapping("/test")
	public String testTemplate() {
		return "test";
	}
}
