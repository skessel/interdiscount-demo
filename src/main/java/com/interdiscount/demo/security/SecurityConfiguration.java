package com.interdiscount.demo.security;

import java.util.List;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.interdiscount.demo.api.posts.PostsRestController;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Value("${kc.realm}")
	private String realm;
	
    @Value("${spring.security.cors.path:/**}")
    private String corsPath;

    @Value("${spring.security.cors.allowedorigins:*}")
    private List<String> allowedOrigins;

    @Value("${spring.security.cors.allowedmethods:OPTIONS,HEAD,GET,POST,PUT,DELETE,PATCH}")
    private List<String> allowedMethods;

    @Value("${spring.security.cors.allowedheaders:Authorization,Cache-Control,Content-Type}")
    private List<String> allowedHeaders;

	@Autowired
	private KeycloakOauth2UserService keycloakOidcUserService;

	@Autowired
	private KeycloakLogoutHandler keycloakLogoutHandler;

	@Autowired
	private ClientRegistrationRepository clientRegisratrionRepo;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		ClientRegistration clientReg = this.clientRegisratrionRepo.findByRegistrationId("procuration");
		Filter f = new OidcBearerTokenAuthenticationFilter(clientReg, this.keycloakOidcUserService);

		//@formatter:off
        http
        	.addFilterBefore(f, UsernamePasswordAuthenticationFilter.class)
        	.csrf()
        		.disable()
        	.cors()
        		.and()
        	.authorizeRequests()
                .antMatchers(HttpMethod.GET, PostsRestController.REST_POST_PATH + "/**").permitAll()
                .antMatchers(PostsRestController.REST_POST_PATH + "/**").hasAuthority("ROLE_MANAGE_POSTS")
//        		.anyRequest().permitAll()
                .and()
            .logout()
                .addLogoutHandler(this.keycloakLogoutHandler)
                .and()
            .oauth2Login()
                .userInfoEndpoint()
                	.oidcUserService(this.keycloakOidcUserService);
        //@formatter:on
	}
	
	@Bean
    public CorsConfigurationSource corsConfigurationSource()
    {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(this.allowedOrigins);
        configuration.setAllowedMethods(this.allowedMethods);
        configuration.setAllowedHeaders(this.allowedHeaders);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(this.corsPath, configuration);
        return source;
    }

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public KeycloakOauth2UserService keycloakOidcUserService(OAuth2ClientProperties oauth2ClientProperties) {

		String jwtSetUri = oauth2ClientProperties.getProvider().get("keycloak").getJwkSetUri();
		JwtDecoder jwtDecoder = JwtUtils.getJwtDecoder(jwtSetUri);

		SimpleAuthorityMapper authoritiesMapper = new SimpleAuthorityMapper();
		authoritiesMapper.setConvertToUpperCase(true);

		return new KeycloakOauth2UserService(jwtDecoder, authoritiesMapper);
	}

	@Bean
	public KeycloakLogoutHandler keycloakLogoutHandler() {
		return new KeycloakLogoutHandler(new RestTemplate());
	}
}