package com.skok1025.study.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.http.ResponseEntity;
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
		String command = "python C:\\workspace\\java\\StudyProject\\src\\main\\java\\com\\skok1025\\study\\craw\\rank_crawer.py movie";
		StringBuilder result = new StringBuilder();
		
		try {
			Process p = Runtime.getRuntime().exec(command);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(), "euc-kr"));

			String line = null;

			while ((line = br.readLine()) != null) {
				result.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	@GetMapping("/hello")
	public ResponseEntity<String> hello() {
		return ResponseEntity.ok("hello");
	}

}
