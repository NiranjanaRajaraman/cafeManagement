package com.inn.cafe.JWT;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;

@Component
public class JWTFilter extends OncePerRequestFilter{
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private CustomerUserDetailsService service;
	
	Claims claims= null;
	private String userName=null;

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest , HttpServletResponse httpServletResponse, FilterChain filterChain)
			throws ServletException, IOException {
		
		if(httpServletRequest.getServletPath().matches("/user/login|/user/signUp|/user/forgotPassword")) {
			filterChain.doFilter(httpServletRequest, httpServletResponse);
		}
		else {
			String authorizationHeader= httpServletRequest.getHeader("Authorization");
			String token =null;
			
			if(Objects.nonNull(authorizationHeader) &&authorizationHeader.startsWith("Bearer ") ) {
				token = authorizationHeader.split(" ")[1].trim();
				
				
				userName=jwtUtil.extractUserName(token);
				claims = jwtUtil.extractAllClaims(token);
			}
			
			if(Objects.nonNull(userName) && SecurityContextHolder.getContext().getAuthentication()== null) {
				UserDetails userDetails =service.loadUserByUsername(userName);
				if(jwtUtil.validateToken(token, userDetails)) {
					UsernamePasswordAuthenticationToken usernamePassowrdAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				
					usernamePassowrdAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
					
					SecurityContextHolder.getContext().setAuthentication(usernamePassowrdAuthenticationToken);
				
				}
			}
			
			filterChain.doFilter(httpServletRequest, httpServletResponse);
		}
		
	}
	
	public Boolean  isAdmin() {
		return "admin".equalsIgnoreCase((String) claims.get("role"));
	}
	
	public Boolean  isUser() {
		return "user".equalsIgnoreCase((String) claims.get("role"));
	}
	
	public String getCurrentUser() {
		return userName;
	}
	
}
///user/signUp|