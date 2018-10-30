package com.yidong.util;
/***
 * jwt工具类
 */

import com.yidong.model.JwtUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;

    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";

    @Value("${jwt.secret}")
    private String secret;

    //过期的时间
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 从token中提取
     * @param token
     * @return account
     */
    public String getAccountFromToken(String token) {
        String account;
        try {
            final Claims claims = getClaimsFromToken(token);
            account = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            account = null;
        }
        return account;
    }

    /**
     *
     * @param token
     * @return token创建的时间
     */
    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    /**
     *
     * @param token
     * @return token过期的时间
     */
    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    /**
     *
     * @param token
     * @return token中的claims
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
            claims = null;
        }
        return claims;
    }

    /**
     * 生成过期时间
     * @return
     */
    private Date generateExpirationDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d  = null;
        //这里会有一个异常，所以要用try catch捕获异常
        try {
            d  = sdf.parse("3017-11-06");
        }catch (Exception e){
            e.printStackTrace();
        }
        return d;
    }

    /**
     * 判断token是否过期
     * @param token
     * @return
     */
    private Boolean isTokenExpired(String token) {
       // final Date expiration = getExpirationDateFromToken(token);
      //  return expiration.before(new Date());
        return false;//默认token永不过期
    }

    //token创建的时间是否在密码修改之前
    /*
    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }*/

    /**
     * 生成token
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 生成token
     * @param claims
     * @return
     */
    String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }


    /**
     * token是否需要被更新
     * @param token
     * @return
     */
    public Boolean canTokenBeRefreshed(String token) {
        final Date created = getCreatedDateFromToken(token);
        //return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                //&& !isTokenExpired(token);
        return !isTokenExpired(token);
    }

    /**
     * 更新token
     * @param token
     * @return
     */
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }


    /**
     * 验证接收到的token是否有效
     * @param token
     * @param userDetails
     * @return
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        JwtUser user = (JwtUser) userDetails;
        final String username = getAccountFromToken(token);
        //final Date created = getCreatedDateFromToken(token);token创建的时间
        //final Date expiration = getExpirationDateFromToken(token);
        return (
                username.equals(user.getUsername())
                        && !isTokenExpired(token));
    }
}
