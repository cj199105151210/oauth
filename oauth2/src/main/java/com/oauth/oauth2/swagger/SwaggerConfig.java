package com.oauth.oauth2.swagger;


import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Component
@EnableSwagger2
public class SwaggerConfig {

    private String securitySchemaOAuth2 = "oauth2schema";
    private AuthorizationScope authorizationScopeRead = new AuthorizationScope("read","read");
    private AuthorizationScope authorizationScopeWrite = new AuthorizationScope("write","write");
    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("swagger标题")
                .version("1.0")
                .description("write by wbb")
                .build();
    }

    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.wbb.swagger2oauth2.controller"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.POST, globalResponse())
                .globalResponseMessage(RequestMethod.DELETE, globalResponse())
                .globalResponseMessage(RequestMethod.PUT, globalResponse())
                .globalResponseMessage(RequestMethod.GET, globalResponse())
                .securitySchemes(newArrayList(securitySchema()))
                .securityContexts(newArrayList(securityContext()));
    }
    private SecurityContext securityContext(){
        return SecurityContext.builder()
                .securityReferences(securityReference())
                .forPaths(PathSelectors.any())
                .build();

    }
    private List<SecurityReference> securityReference(){
        AuthorizationScope[] scopes = new AuthorizationScope[2];
        scopes[0] = authorizationScopeRead;
        scopes[1] = authorizationScopeWrite;
        return newArrayList(new SecurityReference(securitySchemaOAuth2,scopes));
    }

    private List<ResponseMessage> globalResponse() {
        List<ResponseMessage> result = newArrayList();
        result.add(new ResponseMessageBuilder().code(400).message("Bad Request").build());
        result.add(new ResponseMessageBuilder().code(401).message("Unauthorized").build());
        result.add(new ResponseMessageBuilder().code(403).message("Forbidden").build());
        result.add(new ResponseMessageBuilder().code(404).message("Not Found").build());
        result.add(new ResponseMessageBuilder().code(500).message("Internal Server Error").build());
        return result;
    }
    private OAuth securitySchema() {
        LoginEndpoint loginEndpoint = new LoginEndpoint("http://localhost:8888/oauth/authorize");
        GrantType grantType = new ImplicitGrant(loginEndpoint, "access_token");
        return new OAuth(securitySchemaOAuth2, newArrayList(authorizationScopeRead, authorizationScopeWrite), newArrayList(grantType));
    }


    @Bean
    SecurityConfiguration security() {
        return new SecurityConfiguration(
                "demoClient",
                "ignoredButCannotBeNullClientSecret",
                "oauth2-resource",//client resourceIds
                "swagger",
                "apiKey",
                ApiKeyVehicle.HEADER,
                "api_key",
                " " /*scope separator*/);
    }

}
