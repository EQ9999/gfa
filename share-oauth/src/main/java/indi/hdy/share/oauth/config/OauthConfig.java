package indi.hdy.share.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

@Configuration
@EnableAuthorizationServer
public class OauthConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	public PasswordEncoder passwordEncoder;

	@Autowired
	private RedisConnectionFactory redisConnectionFactory;

	@Autowired
	public UserDetailsService myUserDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		/**
		 * redis token 方式
		 */

		endpoints.authenticationManager(authenticationManager).userDetailsService(myUserDetailsService)
				.tokenStore(new RedisTokenStore(redisConnectionFactory));
		endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
		endpoints.pathMapping("/oauth/token", "/oauth/getToken");
		endpoints.exceptionTranslator(new WebResponseExceptionTranslator<OAuth2Exception>() {

			@Override
			public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
				System.out.println("xxxx");
				e.printStackTrace();
				return null;
			}
		});

// 		设置client的第二种方式
//		DefaultTokenServices consumerTokenServices = (DefaultTokenServices) endpoints.getConsumerTokenServices();
//		InMemoryClientDetailsService client=new InMemoryClientDetailsService();
//		client.setClientDetailsStore(clientDetailsStore);
//		consumerTokenServices.setClientDetailsService(client);
//		endpoints.tokenServices(consumerTokenServices);

//		endpoints.authorizationCodeServices(new AuthorizationCodeServices() {
//			
//			@Override
//			public String createAuthorizationCode(OAuth2Authentication authentication) {
//				// TODO Auto-generated method stub
//				return null;
//			}
//			
//			@Override
//			public OAuth2Authentication consumeAuthorizationCode(String code) throws InvalidGrantException {
//				// TODO Auto-generated method stub
//				return null;
//			}
//		});

	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory().withClient("test1").secret(passwordEncoder.encode("1111"))
				.redirectUris("http://localhost:8882/hello")
				.authorizedGrantTypes("refresh_token", "authorization_code", "password")
				.accessTokenValiditySeconds(3600).scopes("all").and().withClient("test2")
				.secret(passwordEncoder.encode("2222"))
				.authorizedGrantTypes("refresh_token", "authorization_code", "password")
				.accessTokenValiditySeconds(3600).scopes("all");
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.allowFormAuthenticationForClients();
//		security.checkTokenAccess("permitAll()");
		security.checkTokenAccess("isAuthenticated()");
		security.tokenKeyAccess("isAuthenticated()");
	}
}
