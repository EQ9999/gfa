package test.gfa;

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

		Mono.from((a) -> {
			a.onNext("100");
			System.out.println(a);
		}).subscribe();

		Mono.just("a").log().doOnNext(a -> {
			System.out.println(a);
		}).doOnNext(a -> {
			System.out.println(a);
		}).then(Mono.fromRunnable(() -> {
			System.out.println("c");
		})).then(Mono.fromRunnable(() -> {
			System.out.println("c");
		})).subscribe(a -> {
			System.out.println("end---" + a);
		}, a -> {
			System.out.println("end---1" + a);
		}, () -> {
			System.out.println("end---2");
		});
	}
}
