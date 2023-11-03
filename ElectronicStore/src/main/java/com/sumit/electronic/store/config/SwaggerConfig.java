package com.sumit.electronic.store.config;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

	@Bean
	public Docket docket() {
		Docket docket = new Docket(DocumentationType.SWAGGER_2);
		docket.apiInfo(getApiInfo());
		
		docket.securityContexts(Arrays.asList(getSecurityContext()));
		docket.securitySchemes(Arrays.asList(getSchemes()));
		
		ApiSelectorBuilder select = docket.select();
		select.apis(RequestHandlerSelectors.any());
		select.paths(PathSelectors.any());
		Docket build = select.build();
		return build;
		
	}


	private SecurityContext getSecurityContext() {
		// TODO Auto-generated method stub
		SecurityContext context = SecurityContext
				.builder()
				.securityReferences(getSecurityRefrences())
				.build();
		return context;
	}
	
	private List<SecurityReference> getSecurityRefrences() {
		// TODO Auto-generated method stub
		AuthorizationScope [] scopes = {
				new AuthorizationScope("Global", "Access Every Thing")
		};
		return Arrays.asList(new SecurityReference("JWT", scopes));
	}


	private ApiKey getSchemes() {
		// TODO Auto-generated method stub
		
		return new ApiKey("JWT", "Authorization", "header");
	}

	private ApiInfo getApiInfo() {
		ApiInfo apiInfo = new ApiInfo(
				"Electronic Store Backened : APIs", 
				"This is backened project created by Sumit Sharma",
				"1.0v",
				"https://www.sumitsharma.com",
				new Contact("Sumit","https://www.sumitsharma.com/home", "sumit.javadeveloper09@gmail.com"),
				"LICENSE OF APIs ",
				"https://wwww.sumit/about",
				new  ArrayDeque<>()
				);
		
		
		
		
		return apiInfo;
	}
}
