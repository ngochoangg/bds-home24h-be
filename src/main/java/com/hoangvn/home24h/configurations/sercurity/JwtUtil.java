package com.hoangvn.home24h.configurations.sercurity;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoangvn.home24h.configurations.UserPrincipal;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@Component
public class JwtUtil {
    private static Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    private static final String USER = "user";
    private static final String SECRET = "bIJi33DW5TUJP5EkLi5rWAXSZX5UCvKzYACAaNFNwvhW7i6ukxPAIntB5Zh4xz5bCuzaEjDbKmhEb4YFNn43eRhdSythTzkIxReU";

    // Tạo mới token
    public String generateToken(UserPrincipal user) {
        String token = null;
        try {
            JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
            builder.claim(USER, user);
            builder.expirationTime(this.generateExpireDate());
            JWTClaimsSet claimsSet = builder.build();
            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
            JWSSigner signer = new MACSigner(SECRET.getBytes());
            signedJWT.sign(signer);
            token = signedJWT.serialize();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return token;
    }

    // Trả về thời hạn token
    private Date getExpirationDateFromToken(JWTClaimsSet claims) {
        return claims != null ? claims.getExpirationTime() : new Date();
    }

    // Kiểm tra token hết hạn chưa
    private boolean isTokenExpired(JWTClaimsSet claims) {
        return getExpirationDateFromToken(claims).after(new Date());
    }

    // Thời gian hết hạn là 24h.
    public Date generateExpireDate() {
        return new Date(System.currentTimeMillis() + 86400000);
    }

    // Claims token
    public JWTClaimsSet getClaimsFromToken(String token) {
        JWTClaimsSet claimsSet = null;
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(SECRET.getBytes());
            if (signedJWT.verify(verifier)) {
                claimsSet = signedJWT.getJWTClaimsSet();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return claimsSet;
    }

    // Lấy thông tin người dùng từ token
    public UserPrincipal getUserFromToken(String token) {
        UserPrincipal user = null;
        try {
            JWTClaimsSet claimsSet = this.getClaimsFromToken(token);
            if (claimsSet != null && isTokenExpired(claimsSet)) {
                var jsonObject = claimsSet.getClaim(USER);
                ObjectMapper mapper = new ObjectMapper();
                user = mapper.convertValue(jsonObject, UserPrincipal.class);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            System.out.println(e.getMessage());
        }
        return user;
    }

}
