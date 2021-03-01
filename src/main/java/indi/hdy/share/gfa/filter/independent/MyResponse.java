package indi.hdy.share.gfa.filter.independent;

import java.io.File;
import java.util.function.Supplier;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ZeroCopyHttpOutputMessage;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.util.MultiValueMap;

import reactor.core.publisher.Mono;

public class MyResponse extends ServerHttpResponseDecorator implements ZeroCopyHttpOutputMessage {

	public MyResponse(ServerHttpResponse delegate) {
		super(delegate);
	}

	@Override
	public HttpHeaders getHeaders() {
		System.out.println("getHeaders");
		return super.getHeaders();
	}

	@Override
	public MultiValueMap<String, ResponseCookie> getCookies() {
		System.out.println("getCookies");
		return super.getCookies();
	}

	@Override
	public void addCookie(ResponseCookie cookie) {
		System.out.println("addCookie");
		super.addCookie(cookie);
	}

	@Override
	public boolean setStatusCode(HttpStatus status) {
		System.out.println("setStatusCode");
		System.out.println(status);
		return super.setStatusCode(status);
	}

	@Override
	public void beforeCommit(Supplier<? extends Mono<Void>> action) {
		System.out.println("before commit");
		super.beforeCommit(action);
	}

	@Override
	public Mono<Void> setComplete() {
		System.out.println("complete");
		return super.setComplete();
	}

	@Override
	public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
		System.out.println("writeAndFlushWith");
		body.subscribe(new Subscriber<Publisher<? extends DataBuffer>>() {

			@Override
			public void onSubscribe(Subscription s) {
				// TODO Auto-generated method stub
				System.out.println(1);
			}

			@Override
			public void onNext(Publisher<? extends DataBuffer> t) {
				// TODO Auto-generated method stub
				System.out.println(2);
			}

			@Override
			public void onError(Throwable t) {
				// TODO Auto-generated method stub
				System.out.println(3);
			}

			@Override
			public void onComplete() {
				// TODO Auto-generated method stub
				System.out.println(4);
			}

		});

		return super.writeAndFlushWith(body);
//		return Mono.empty();
	}

	@Override
	public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
		System.out.println("writeWith");
		body.subscribe(new Subscriber<DataBuffer>() {
			private Subscription s;

			@Override
			public void onSubscribe(Subscription s) {
				System.out.println("on sub---");
				this.s = s;
				s.request(1);
			}

			@Override
			public void onNext(DataBuffer t) {
				System.out.println("on next---");
				s.cancel();
				byte[] b = new byte[1024];
				System.out.println(t.readPosition());
				System.out.println(t.capacity());
				DataBuffer read = t.read(b, t.readPosition(), t.capacity() > 1024 ? 1024 : t.capacity());
				System.out.println(new String(b));
			}

			@Override
			public void onError(Throwable t) {
				System.out.println("on error---");
				s.cancel();
			}

			@Override
			public void onComplete() {
				System.out.println("on com---");
				s.cancel();
			}

		});

		return super.writeWith(body);
//		return Mono.empty();
	}

	@Override
	public Mono<Void> writeWith(File file, long position, long count) {
		System.out.println("write with file");
		ZeroCopyHttpOutputMessage delegate2 = (ZeroCopyHttpOutputMessage) getDelegate();
		return delegate2.writeWith(file, position, count);
	}

}
