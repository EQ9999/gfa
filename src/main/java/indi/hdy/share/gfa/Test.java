package indi.hdy.share.gfa;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class Test {

	@Value("${thello}")
	private String test;

	@RequestMapping("/hello")
	public String hello() {
		return test;
	}

	public static void main(String[] args) {
		SpringApplication.run(Test.class);
	}
}
