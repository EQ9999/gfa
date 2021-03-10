package indi.hdy.share.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class Oauth {

	@RequestMapping("/hello")
	public String hello() throws Exception {
		return "hello";
	}

	public static void main(String[] args) {
		System.out.println("xxxx");
//		Log log = LogFactory.getLog(Oauth.class);

//		log.debug("xxx", new Exception());
//		SpringApplication.run(Oauth.class, new String[] { "--debug" });

//		FilterSecurityInterceptor

		SpringApplication.run(Oauth.class);
	}

	// getToken
	// http://localhost:8882/oauth/getToken?grant_type=password&username=admin&password=1111&scope=all&client_id=test1&client_secret=1111

}
