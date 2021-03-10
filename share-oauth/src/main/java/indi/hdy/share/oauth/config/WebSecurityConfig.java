package indi.hdy.share.oauth.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistration.Builder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	Logger log = LoggerFactory.getLogger(getClass());

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new PasswordEncoder() {

			@Override
			public String encode(CharSequence rawPassword) {
				return rawPassword.toString();
			}

			@Override
			public boolean matches(CharSequence rawPassword, String encodedPassword) {
				if (encodedPassword.equals(rawPassword.toString()))
					return true;
				return false;
			}
		};
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean(value = "myUserDetailsService")
	public UserDetailsService getUserDetailsService() {
		return new UserDetailsService() {

			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				log.info("usernameis:" + username);
				// 查询数据库操作
				if (!username.equals("admin")) {
					throw new UsernameNotFoundException("the user is not found");
				} else {
					// 用户角色也应在数据库中获取
					String role = "ROLE_ADMIN";
					List<SimpleGrantedAuthority> authorities = new ArrayList<>();
					authorities.add(new SimpleGrantedAuthority(role));
					// 线上环境应该通过用户名查询数据库获取加密后的密码
					String password = passwordEncoder().encode("1111");
					return new User(username, password, authorities);
				}
			}
		};
	}

	// 创建client对象，也可以通过配置文件创建
	@Bean
	public ClientRegistrationRepository getClientRegistrationRepository() {
		return new ClientRegistrationRepository() {

			@Override
			public ClientRegistration findByRegistrationId(String registrationId) {
				Builder build = ClientRegistration.withRegistrationId(registrationId);
				build.clientName("sb").clientSecret("111").clientId("test1")
						.clientAuthenticationMethod(ClientAuthenticationMethod.POST)
						.authorizationGrantType(new AuthorizationGrantType("password"))
						.authorizationUri("http://localhost:8882/oauth/authorize")
						.tokenUri("http://localhost:8882/oauth/getToken")
						.userInfoUri("http://localhost:8882/oauth/check_token");

				return build.build();
			}
		};
	}

	/**
	 * 允许匿名访问所有接口 主要是 oauth 接口
	 * 
	 * @param http
	 * @throws Exception
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().antMatchers("/oauth/**").permitAll().and().authorizeRequests()
				.antMatchers("/login").permitAll().and().authorizeRequests().anyRequest().authenticated();

		// 通过form表单登录
		http.formLogin().and().httpBasic();
		// 通过openid登录
//		http.openidLogin();
		// 通过oauth2登录
//		http.oauth2Login();

		// 异常处理接口重写
		http.exceptionHandling().accessDeniedHandler(new AccessDeniedHandler() {

			@Override
			public void handle(HttpServletRequest request, HttpServletResponse response,
					AccessDeniedException accessDeniedException) throws IOException, ServletException {
				System.out.println("yyyyyyy");
				accessDeniedException.printStackTrace();
			}

		}).authenticationEntryPoint(new AuthenticationEntryPoint() {

			@Override
			public void commence(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException authException) throws IOException, ServletException {
				System.out.println("bbbbbb");
				authException.printStackTrace();
			}

		});
	}

}
