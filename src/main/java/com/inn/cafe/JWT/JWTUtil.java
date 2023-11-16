package com.inn.cafe.JWT;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTUtil {
	
	private String secret = "RrRwa6Gm2bdzrdVJrQx8wWfxcHjGaADDhhtYiioMkkyeggtVbbhyytNiilBBWdRtGuJoLpNtFrDV";
	public String extractUserName(String token) {
		return extractClaims(token,Claims::getSubject);
	}
	
	public Date extractExpiration(String token) {
		return extractClaims(token,Claims::getExpiration);
	}
	public <T>  T extractClaims(String token, Function<Claims,T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
		
		
	}

	@SuppressWarnings("deprecation")
	public Claims extractAllClaims(String token) {
		return Jwts.parser()
			    .setSigningKey(secret)
			    .parseClaimsJws(token)
			    .getBody();
	}
	
	private  Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date())?true:false;
	}
	
	public String generateToken(String userName, String role) {
		Map<String,Object> claims= new HashMap<>();
		claims.put("role", role);
		return createToken(claims, userName);
		
	}
	
	@SuppressWarnings("deprecation")
	private String createToken(Map<String,Object> claims, String subject) {
		String word= Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis()+ 1000*60*60*10)).signWith(SignatureAlgorithm.HS256,secret).compact();
		return word;
//		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis()+ 1000*60*60*10)).signWith(SignatureAlgorithm.HS256,secret).compact();
		
	}
	public Boolean validateToken(String token,UserDetails userDetails) {
		final String username = extractUserName(token);
		return (username.equals(userDetails.getUsername())&& !isTokenExpired(token));
		
	}
	
}
