package com.github.enquete.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
    DataSource dataSource;
	 
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.jdbcAuthentication().dataSource(dataSource).
		   usersByUsernameQuery("SELECT username, password, enabled FROM usuario WHERE username=?")
		  .authoritiesByUsernameQuery("SELECT username, role FROM tipo_usuario INNER JOIN usuario ON tipo_usuario.username_id=usuario.id where username = ?");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
		.antMatchers("/layout/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				//.antMatchers("/votes/new").hasRole("USER_PERMITION")
				//.antMatchers("/votes/**").hasRole("USER_PERMITION")
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login")
				.permitAll()
				.and()
			.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
	}
	
}
