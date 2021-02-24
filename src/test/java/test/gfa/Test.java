package test.gfa;

import org.reactivestreams.Subscription;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 
 * @author deyu.he
 *
 */
public class Test {
	public static void main(String[] args) {
//		Jwt encode = JwtHelper.encode("dfadfadfadfadf", new MacSigner("8888"));
//		String encoded = encode.getEncoded();
//		System.out.println(encoded);

//		Mono.defer(() -> {
//			return Mono.empty();
//		}).log().doOnNext(a -> {
//			System.out.println(a);
//		}).switchIfEmpty(Mono.empty()).doOnNext(a -> {
//			System.out.println(a);
//		}).then(Mono.fromRunnable(() -> {
//			System.out.println("c");
//		})).then(Mono.fromRunnable(() -> {
//			System.out.println("c");
//		})).subscribe(a -> {
//			System.out.println("end---" + a);
//		}, a -> {
//			System.out.println("end---1" + a);
//		}, () -> {
//			System.out.println("end---2");
//		});

		Flux.from(a -> {

			a.onSubscribe(new Subscription() {

				@Override
				public void request(long n) {
					System.out.println(n);
					a.onNext("bbbb");
					a.onComplete();
				}

				@Override
				public void cancel() {
					System.out.println("cancel");
				}
			});
			System.out.println(a.getClass());

			System.out.println("----");
			// 9223372036854775807
			a.onNext("bbbb");
			a.onNext("ccc");
			a.onNext("ddd");
			a.onNext("eee");
			a.onNext("ffff");
			System.out.println("****");
		}).doOnNext(a -> {

			System.out.println("======");
			System.out.println(a);
		}).then(Mono.fromRunnable(() -> {
			System.out.println("then");
		})).subscribe();

//		Mono.just("a").log().doOnNext(a -> {
//			System.out.println(a);
//		}).doOnNext(a -> {
//			System.out.println(a);
//		}).then(Mono.fromRunnable(() -> {
//			System.out.println("c");
//		})).then(Mono.fromRunnable(() -> {
//			System.out.println("c");
//		})).subscribe(a -> {
//			System.out.println("end---" + a);
//		}, a -> {
//			System.out.println("end---1" + a);
//		}, () -> {
//			System.out.println("end---2");
//		});
	}
}
