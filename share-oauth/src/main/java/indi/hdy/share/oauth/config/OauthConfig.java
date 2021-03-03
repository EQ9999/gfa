package indi.hdy.share.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
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
		endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET,HttpMethod.POST);
		endpoints.pathMapping("/oauth/token","/oauth/getToken");
	}
	

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory().withClient("test1").secret(passwordEncoder.encode("1111"))
				.authorizedGrantTypes("refresh_token", "authorization_code", "password")
				.accessTokenValiditySeconds(3600).scopes("all").and().withClient("test2")
				.secret(passwordEncoder.encode("2222"))
				.authorizedGrantTypes("refresh_token", "authorization_code", "password")
				.accessTokenValiditySeconds(3600).scopes("all");
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.allowFormAuthenticationForClients();
		security.checkTokenAccess("isAuthenticated()");
		security.tokenKeyAccess("isAuthenticated()");
	}
}
