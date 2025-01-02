package com.bitsu.social_media;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@SpringBootApplication
public class SocialMediaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialMediaApplication.class, args);
	}

	// @Bean
	// public GroupedOpenApi postsGroup(){
	// 	return GroupedOpenApi.builder()
	// 			.group("Posts")
	// 			.addOperationCustomizer((operation, handlerMethod) -> {
	// 				operation.addSecurityItem(new SecurityRequirement().addList("basicScheme"));
	// 				return operation;
	// 			})
	// 			.addOpenApiCustomizer(openApi -> openApi.info(new Info().title("Users API").version("v1")))
	// 			.packagesToScan("com.bitsu.social_media.controller")
	// 			.pathsToMatch("/api/v1/posts/**")
	// 			.build();
	//}

	@Bean
	public OpenAPI customApi(){
		//Create Open API OBject with JWT security scheme
		return new OpenAPI()
				.components(new Components()
						.addSecuritySchemes("bearerAuth", new SecurityScheme()
								.name("bearerAuth")
								.type(SecurityScheme.Type.HTTP)
								.scheme("bearer")
								.bearerFormat("JWT")
						)
				)
				.addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
				.info(new Info()
						.title("Social Media API")
						.version("v1")
						.license(new License().name("Apache 2.0").url("http://springdoc.org"))
				);

//		return new OpenAPI()
//		.components(
//			new Components()
//				.addSecuritySchemes
//				("bearerAuth",
//				new SecurityScheme()
//					.name("bearerAuth")
//					.type(SecurityScheme.Type.HTTP)
//					.scheme("bearer")
//					.bearerFormat("JWT")
//				)
//		)
//		.info(new Info().title("Customer accounts API").version("v1")
//		.license(new License().name("Apache 2.0").url("http://springdoc.org")));
	}

}
