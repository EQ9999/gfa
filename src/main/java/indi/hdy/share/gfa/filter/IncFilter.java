package indi.hdy.share.gfa.filter;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class IncFilter implements GlobalFilter, Ordered {

	@Override
	public int getOrder() {
		return 0;
	}

	public IncFilter() {
		this.inc = new AtomicInteger(0);
	}

	private AtomicInteger inc;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		System.out.println("global filter--" + exchange.getRequest().getURI() + "--" + exchange.getResponse().getClass()
				+ "---" + inc.incrementAndGet());
		return chain.filter(exchange);
	}

}
