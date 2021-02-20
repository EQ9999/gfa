package indi.hdy.share.gfa.filter.independent;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class MyGatewayFilterFactory implements GatewayFilterFactory<MyConfig> {

	@Override
	public GatewayFilter apply(MyConfig config) {
		return new MyGateWay();
	}

	@Override
	public MyConfig newConfig() {
		return new MyConfig();
	}

	@Override
	public String name() {
		return "My";
	}

	@Override
	public Class<MyConfig> getConfigClass() {
		return MyConfig.class;
	}

	public static void main(String[] args) {
		MyGatewayFilterFactory mgf = new MyGatewayFilterFactory();
		System.out.println(mgf.name());
	}
}

class MyGateWay implements GatewayFilter, Ordered {

	@Override
	public int getOrder() {
		return -1;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange a, GatewayFilterChain b) {
		System.out.println("my gateway pre-- request=" + a.getRequest() + ",response=" + a.getResponse().getHeaders());
		// 调用不了doOnNext，因为该方法是指处理下一个mono，但是filter里面是递归处理完所有filter，最终返回的是一个mono.empty()，所有无法触发doOnNext
//		return b.filter(a).log().doOnNext(c -> {
//			System.out.println(c);
//		}).then(Mono.defer(() -> {
//			System.out.println(
//					"my gateway post-- request=" + a.getRequest() + ",response=" + a.getResponse().getHeaders());
//			return Mono.empty();
//		}));

		return b.filter(a).log().then(Mono.fromRunnable(() -> {
			System.out.println(
					"my gateway post-- request=" + a.getRequest() + ",response=" + a.getResponse().getHeaders());
		}));
	}

}

class MyConfig {
	private String name;
	private String tData;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String gettData() {
		return tData;
	}

	public void settData(String tData) {
		this.tData = tData;
	}

}
