package indi.hdy.share.gfa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class Gateway {

	@Autowired
	private TestBean testBean;

	public TestBean getTestBean() {
		return testBean;
	}

	public void setTestBean(TestBean testBean) {
		this.testBean = testBean;
	}

	@Value("${thello}")
	private String test;

	@RequestMapping("/hello")
	public String hello() {
		return test + "----" + testBean.getMsg();
	}

//	@Bean
//	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//		return builder.routes()
//				.route("path_route", r -> r.path("/s")
//						.uri("https://www.baidu.com"))
//				.build();
//	}

	public static void main(String[] args) {
		SpringApplication.run(Gateway.class);

	}
}
