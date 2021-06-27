package com.skok1025.study.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BaseController {
	
	@ResponseBody
	@GetMapping("/test")
	public String index() {
		return "test";
	}

}
