package cn.itcast.gateway.utils;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author conghuhu
 * @create 2022-03-10 12:08
 */
@Component
public class JwtTokenUtil {
    /**
     * 24小时
     */
    private static long tokenExpiration = 24 * 60 * 60 * 1000;
    /**
     * 秘钥
     */
    private static String tokenSignKey = "cong0917lovebiyanqi";

    private static String passwordKey = "conghuhu";

    public String createToken(String userName, String password) {
        String token = Jwts.builder().setSubject(userName)
                .claim(passwordKey, password).setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(SignatureAlgorithm.HS512, tokenSignKey).compressWith(CompressionCodecs.GZIP).compact();
        return token;
    }

    public String getUserNameFromToken(String token) throws ExpiredJwtException, MalformedJwtException {
        String userName = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token).getBody().getSubject();
        return userName;
    }

    public String getUserPasswordFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token).getBody();
        return claims.get(passwordKey).toString();
    }

}
