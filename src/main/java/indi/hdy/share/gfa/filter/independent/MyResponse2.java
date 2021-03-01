package indi.hdy.share.gfa.filter.independent;

import java.io.File;

import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ZeroCopyHttpOutputMessage;
import org.springframework.http.server.reactive.AbstractServerHttpResponse;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.http.server.HttpServerResponse;

public class MyResponse2 extends AbstractServerHttpResponse implements ZeroCopyHttpOutputMessage {

	private HttpServerResponse response;
	private AbstractServerHttpResponse target;

	public MyResponse2(AbstractServerHttpResponse target) {
		super(target.bufferFactory());
		this.target = target;
		this.response = target.getNativeResponse();
	}

	@Override
	public Mono<Void> writeWith(File file, long position, long count) {
		System.out.println("write with file");
		return doCommit(() -> this.response.sendFile(file.toPath(), position, count).then());
	}

	@Override
	public HttpHeaders getHeaders() {
		System.out.println("getHeaders");
		return this.target.getHeaders();
	}

	@Override
	public <T> T getNativeResponse() {
		System.out.println("getNativeResponse");
		return (T) this.response;
	}

	@Override
	protected void applyStatusCode() {
		System.out.println("applyStatusCode");
		Integer statusCode = getStatusCodeValue();
		if (statusCode != null) {
			this.response.status(HttpResponseStatus.valueOf(statusCode));
		}
	}

	@Override
	protected Mono<Void> writeWithInternal(Publisher<? extends DataBuffer> publisher) {
		System.out.println("writeWithInternal");
		Publisher<ByteBuf> body = toByteBufs(publisher);
		return this.response.send(body).then();
	}

	@Override
	protected Mono<Void> writeAndFlushWithInternal(Publisher<? extends Publisher<? extends DataBuffer>> publisher) {
		System.out.println("writeAndFlushWithInternal");
		Publisher<Publisher<ByteBuf>> body = Flux.from(publisher).map(MyResponse2::toByteBufs);
		return this.response.sendGroups(body).then();
	}

	public static Publisher<ByteBuf> toByteBufs(Publisher<? extends DataBuffer> dataBuffers) {
		return Flux.from(dataBuffers).map(NettyDataBufferFactory::toByteBuf);
	}

	@Override
	protected void applyHeaders() {
		System.out.println("applyHeaders");
		getHeaders().forEach((headerName, headerValues) -> {
			for (String value : headerValues) {
				this.response.responseHeaders().add(headerName, value);
			}
		});
	}

	@Override
	protected void applyCookies() {
		System.out.println("applyCookies");
		for (String name : getCookies().keySet()) {
			for (ResponseCookie httpCookie : getCookies().get(name)) {
				Cookie cookie = new DefaultCookie(name, httpCookie.getValue());
				if (!httpCookie.getMaxAge().isNegative()) {
					cookie.setMaxAge(httpCookie.getMaxAge().getSeconds());
				}
				if (httpCookie.getDomain() != null) {
					cookie.setDomain(httpCookie.getDomain());
				}
				if (httpCookie.getPath() != null) {
					cookie.setPath(httpCookie.getPath());
				}
				cookie.setSecure(httpCookie.isSecure());
				cookie.setHttpOnly(httpCookie.isHttpOnly());
				this.response.addCookie(cookie);
			}
		}
	}

}
