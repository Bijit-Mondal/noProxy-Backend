package website.ilib.noproxy.config.service;


import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	private static final String SECRET_KEY = "34753778217A25432A462D4A614E645267556B58703273357638792F423F4428";

	public String extractUserName(String jwt) {
		// TODO Auto-generated method stub
		return extractClaim(jwt, Claims::getSubject);
	}
	
	public <T> T extractClaim(String jwt,Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(jwt);
		return claimsResolver.apply(claims);
	}
	
	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}
	
	public boolean isTokenValid(String jwt, UserDetails userDetails) {
		final String userEmail = extractUserName(jwt);
		return (userEmail.equals(userDetails.getUsername())) && !isTokenExpire(jwt);
	}
	
	private boolean isTokenExpire(String jwt) {
		return extractExpiration(jwt).before(new Date());
	}

	private Date extractExpiration(String jwt) {
		// TODO Auto-generated method stub
		return extractClaim(jwt, Claims::getExpiration);
	}

	public String generateToken(
			Map<String, Object> extraClaims,
			UserDetails userDetails
	) {
		return Jwts
				.builder()
				.setClaims(extraClaims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000*60*24))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();
	}
	private Claims extractAllClaims(String jwt) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSigningKey())
				.build()
				.parseClaimsJws(jwt)
				.getBody();
	}

	private Key getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
}
