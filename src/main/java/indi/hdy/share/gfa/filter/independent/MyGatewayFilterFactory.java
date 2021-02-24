package indi.hdy.share.gfa.filter.independent;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.netty.handler.codec.http.HttpHeaderValues;
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
		System.out.println(this.hashCode() + " my gateway pre-- request=" + a.getRequest() + ",response="
				+ a.getResponse().getHeaders());
		// 调用不了doOnNext，因为该方法是指处理下一个mono，但是filter里面是递归处理完所有filter，最终返回的是一个mono.empty()，所有无法触发doOnNext
//		return b.filter(a).log().doOnNext(c -> {
//			System.out.println(c);
//		}).then(Mono.defer(() -> {
//			System.out.println(
//					"my gateway post-- request=" + a.getRequest() + ",response=" + a.getResponse().getHeaders());
//			return Mono.empty();
//		}));

		ServerHttpResponse response = a.getResponse();
//		response.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
		response.setStatusCode(HttpStatus.OK);

//		response.writeAndFlushWith((x) -> {
//			x.onNext((Publisher<DataBuffer>) (s) -> {
//				DataBufferFactory bufferFactory = response.bufferFactory();
//				DataBuffer allocateBuffer = bufferFactory.allocateBuffer();
//				allocateBuffer.write("sb".getBytes());
//				System.out.println(allocateBuffer.readPosition());
//				System.out.println(allocateBuffer.capacity());
//				s.onNext(allocateBuffer);
//			});
//		});

		response.getHeaders().add("content-type", HttpHeaderValues.TEXT_PLAIN + "; charset=UTF-8");
		response.getHeaders().set("content-length", String.valueOf("sb".getBytes().length));
		response.getHeaders().add("connection", HttpHeaderValues.KEEP_ALIVE.toString());

//		ServerWebExchange build = a.mutate().request((s) -> {
//			try {
//				s.uri(new URI("http://idanmu.im")).build();
//			} catch (URISyntaxException e) {
//				e.printStackTrace();
//			}
//		}).build();

//		try {
//			a.getResponse().getHeaders().set(HttpHeaders.LOCATION, URI.create("http://idanmu.im").toURL().toString());
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		DataBufferFactory bufferFactory2 = response.bufferFactory();
		byte[] bytes = "1001".getBytes();
		DataBuffer allocateBuffer2 = bufferFactory2.allocateBuffer(bytes.length);
		allocateBuffer2.write(bytes);
		System.out.println(allocateBuffer2.readPosition());
		System.out.println(allocateBuffer2.capacity());

		DataBuffer wrap = bufferFactory2.wrap("sb".getBytes());

//		return a.getResponse().setComplete();

//		return response.writeAndFlushWith(Flux.just(ByteBufFlux.just(response.bufferFactory().wrap("sb".getBytes()))));

//		return response.writeWith(Mono.just(response.bufferFactory().wrap("sb".getBytes())));

//		return response.writeWith((s) -> {
//			s.onSubscribe(new Subscription() {
//				boolean send = false;
//
//				@Override
//				public void request(long n) {
//					if (send) {
//						return;
//					}
//					send = true;
//					System.out.println("-----" + n);
//					s.onNext(allocateBuffer2);
//					s.onComplete();
//				}
//
//				@Override
//				public void cancel() {
//
//				}
//			});
//		});
		ChannelSendOperator
		return response.writeWith((s) -> {
			System.out.println(s.getClass());
//			s.onSubscribe(new Subscription() {
//				boolean send = false;
//
//				@Override
//				public void request(long n) {
////					if (send) {
////						return;
////					}
////					send = true;
//					System.out.println("-----" + n);
////					s.onNext(allocateBuffer2);
////					s.onComplete();
//				}
//
//				@Override
//				public void cancel() {
//
//				}
//			});
			s.onNext(allocateBuffer2);
			s.onComplete();
		});

//		return response.writeWith(Flux.just(allocateBuffer2)).then(Mono.fromRunnable(() -> {
//			System.out.println(this.hashCode() + " my gateway post-- request=" + a.getRequest() + ",response="
//					+ a.getResponse().getHeaders());
//			System.out.println(a.getResponse());
//		}));

//		return b.filter(build).then(Mono.fromRunnable(() -> {
//			System.out.println(this.hashCode() + " my gateway post-- request=" + a.getRequest() + ",response="
//					+ a.getResponse().getHeaders());
//			System.out.println(a.getResponse());
//		}));

//		return b.filter(a).log().then(Mono.fromRunnable(() -> {
//			System.out.println(
//					"my gateway post-- request=" + a.getRequest() + ",response=" + a.getResponse().getHeaders());
//		}));
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
