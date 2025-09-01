package com.example.ex7.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

@Configuration
@EnableWebSecurity // security 에 대한 설정을 가능하게 함.
public class SecurityConfig {
    // Security에 대한 설정시 메서드의 리턴타입이 중요 ⭐

    // permitAll 할 내용을 복수개 설정하기 위한 문자 배열 선언
    private static final String[] AUTH_WHITELIST = {
            "/", "/auth/accessDenied", "/auth/authenticationFailure"
    };

    @Bean // SecurityFilterChain 설정시 모든 시큐리티 설정은 직접 지정해줘야 한다.
    protected SecurityFilterChain config(HttpSecurity httpSecurity) throws Exception {

        // authorizeHttpRequests():: http 요청 별로 인증 처리
        httpSecurity.authorizeHttpRequests(new Customizer<AuthorizeHttpRequestsConfigurer<org.springframework.security.config.annotation.web.builders.HttpSecurity>.AuthorizationManagerRequestMatcherRegistry>() {
            @Override
            public void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
                // Security는 인증(Authentication)과 권한(Authority)으로 나뉨

                // 인증(Authentication) 시작 ====================
                // anyRequest(): 어떠한 요청에도 (이후에 requestMatchers()로 개별로 지정불가)
                //auth.anyRequest().permitAll();    // permitAll(): 모두 수용
                //auth.anyRequest().authenticated();// authenticated():모두 인증

                // requestMatchers("url"): 해당 url에 대해서 인증 설정
                //auth.requestMatchers("/").permitAll(); // 해당 주소 모두 허용
                //auth.requestMatchers("/sample/all").authenticated();// 해당 주소 인증 필요
                //auth.requestMatchers("/sample/all").permitAll(); // 해당 주소 모두 허용
                auth.requestMatchers(AUTH_WHITELIST).permitAll(); // 건별로 하기 번거로울 때
                auth.requestMatchers("/sample/**").permitAll(); // 주소를 필터링해서 허용
                // 인증(Authentication) 끝 ======================

                // hasRole(): 권한 여부에 대해서
                // auth.requestMatchers("/sample/manager").hasRole("MANAGER");// 권한 단수일때
                auth.requestMatchers("/sample/manager").access( // 권한 복수일때
                        new WebExpressionAuthorizationManager("hasRole('MANAGER') or hasRole('ADMIN')"));
                auth.requestMatchers("/sample/admin").hasRole("ADMIN");

            }
        });

        // 인증요구시 로그인페이지를 security 기본 페이지로 설정
        httpSecurity.formLogin(Customizer.withDefaults());

        // 인증요구시 로그인페이지를 사용자 커스터마이징 페이지로 설정
    /*httpSecurity.formLogin(new Customizer<FormLoginConfigurer<HttpSecurity>>() {
      @Override
      public void customize(FormLoginConfigurer<HttpSecurity> httpSecurityFormLoginConfigurer) {
        httpSecurityFormLoginConfigurer
            .loginPage("/sample/login") // 커스텀 로그인 페이지를 직접 설정
            .loginProcessingUrl("/sample/loginSuccess") // 로구인 후 이동할 페이지 직접 설정
            .successHandler((req, res, auth) -> res.sendRedirect("/"));
      }
    });*/

        //logout
    /*httpSecurity.logout(new Customizer<LogoutConfigurer<HttpSecurity>>() {
      @Override
      public void customize(LogoutConfigurer<HttpSecurity> httpSecurityLogoutConfigurer) {
        httpSecurityLogoutConfigurer
            .logoutUrl("/logout") // 로그아웃 처리 URL
            .deleteCookies("JSESSIONID") // 로그아웃 후 쿠키 삭제
            .logoutSuccessHandler((req,res,auth) -> res.sendRedirect("/"))
            .logoutSuccessUrl("/");  // 로그아웃 성공 후 이동페이지 지정
            // logoutSuccessUrl과 logoutSuccessHandler 동시 지정시 logoutSuccessHandler 우선
      }
    });*/

        return httpSecurity.build();
    }
}