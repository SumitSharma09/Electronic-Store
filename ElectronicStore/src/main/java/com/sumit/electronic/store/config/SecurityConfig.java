package com.sumit.electronic.store.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.sumit.electronic.store.security.JwtAuthenticationEntryPoint;
import com.sumit.electronic.store.security.JwtAuthenticationFilter;
import org.springframework.http.HttpMethod;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtAuthenticationEntryPoint authenticationEntryPoint;
	
	@Autowired
	private JwtAuthenticationFilter authenticationFilter;
	
	
	private String[] PUBLIC_URLS= {
			"/swagger-ui/**",
			"/webjars/**",
			"/swagger-resources/**",
			"/v3/api-docs/**",
			"/v2/api-docs/**"
			
			
	};
	
	// HARD CODED TO CREDENTIALS TO WRITE OUR PROGRAM
//	@Bean
//	public UserDetailsService userDetailsService() {
		// users create 
//	UserDetails normal = User.builder()
//		.username("Sumit")
//		.password(passwordEncoder().encode("Sumit"))
//		.roles("Normal")
//		.build();
//		
//		UserDetails admin = User.builder()
//		.username("admin")
//		.password(passwordEncoder().encode("admin"))
//		.roles("superadmin")
//		.build();
//		return new InMemoryUserDetailsManager(normal, admin);
//		
//	}
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
		dao.setUserDetailsService(this.userDetailsService);
		dao.setPasswordEncoder(passwordEncoder());
		return dao;
		
	}
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//	
//		http.authorizeRequests()
//		.anyRequest()
//		.authenticated()
//		.and().
//		formLogin()
//		.loginPage("login.html")
//		.loginProcessingUrl("/process-url")
//		.defaultSuccessUrl("/dashboard")
//		.failureUrl("error").and().logout().logoutUrl("/do-logout");
		
		http.
		csrf()
		.disable()
		.cors()
		.disable()
		.authorizeRequests()
		.antMatchers("/auth/login")
		.permitAll()
		.antMatchers("/auth/google")
		.permitAll()
		.antMatchers(HttpMethod.POST, "/users")
		.permitAll()
		.antMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
		.antMatchers(PUBLIC_URLS)
		.permitAll()
		.anyRequest()
		.authenticated()
		.and()
		.exceptionHandling()
		.authenticationEntryPoint(authenticationEntryPoint)
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		
		http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
		return builder.getAuthenticationManager();
		
	}
	
	

}
