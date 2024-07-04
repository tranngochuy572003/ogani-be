package com.example.service.impl;

import com.example.entity.User;
import com.example.exception.BadRequestException;
import com.example.exception.ForbiddenException;
import com.example.repository.UserRepository;
import com.example.service.JwtTokenService;
import com.example.service.UserService;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.time.*;
import java.util.Base64;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.example.common.AppConstant.JWT_HEADER;
import static com.example.common.MessageConstant.*;

@Service
@Data
@Slf4j
public class JwtTokenServiceImpl implements JwtTokenService {

    private UserService userService;


    @Autowired
    public JwtTokenServiceImpl(UserRepository userRepository, UserService userService) {
        this.userService = userService;
    }

    public String createToken(String userName) {
        User user = userService.findUserByEmail(userName);

        LocalDateTime nowInVietnam = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).plusHours(1);
        long epochSeconds = nowInVietnam.toEpochSecond(ZoneOffset.ofHours(7));
        JSONObject payload = new JSONObject();
        payload.put("userName", user.getUsername());
        payload.put("role", user.getRole().getValue());
        payload.put("exp", epochSeconds);
        payload.put("jwtId", UUID.randomUUID().toString());
        String signature = hmacSha256(encode(JWT_HEADER.getBytes()) + "." + encode(payload.toJSONString().getBytes()), System.getenv("SECRET_KEY"));
        return encode(JWT_HEADER.getBytes()) + "." + encode(payload.toJSONString().getBytes()) + "." + signature;
    }

    public String extractUserNameFromJWT(String token) {
        try {
            if (verifyExpiration(token)) {
                JWT jwt = JWTParser.parse(token);
                JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
                return (String) claimsSet.getClaim("userName");
            }
            else {
                throw new BadRequestException(TOKEN_INVALID);
            }
        } catch (ParseException e) {
            throw new BadRequestException(TOKEN_INVALID);
        }
    }

    public String createRefreshToken(String token) {
        try {
            if (verifyExpiration(token)) {
                JWT jwt = JWTParser.parse(token);
                JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
                String userName = (String) claimsSet.getClaim("userName");
                return refreshToken(userName);
            } else {
                throw new ForbiddenException(REFRESH_TOKEN_EXPIRED);

            }
        } catch (ParseException e) {
            throw new BadRequestException(FIELD_INVALID);
        }
    }

    public boolean verifyExpiration(String authToken) {
        try {
            JWT jwt = JWTParser.parse(authToken);
            JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
            Instant expiryTime = claimsSet.getExpirationTime().toInstant();
            Instant nowVietnam = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant();
            return expiryTime.isAfter(nowVietnam);
        }catch (ParseException e){
            throw new BadRequestException(TOKEN_INVALID);
        }

    }

    public static String encode(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public String hmacSha256(String data, String secret) {
        try {
            byte[] hash = secret.getBytes(StandardCharsets.UTF_8);
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(hash, "HmacSHA256");
            sha256Hmac.init(secretKey);
            byte[] signedBytes = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return encode(signedBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            log.error(ex.getMessage());
            return null;
        }
    }


    public String refreshToken(String userName) {
        User user = userService.findUserByEmail(userName);

        ZoneId vietnamZoneId = ZoneId.of("Asia/Ho_Chi_Minh");
        LocalDateTime nowInVietnam = LocalDateTime.now(vietnamZoneId).plusMinutes(5);
        long epochSeconds = nowInVietnam.toEpochSecond(ZoneOffset.ofHours(7));

        JSONObject payload = new JSONObject();
        payload.put("userName", user.getUsername());
        payload.put("role", user.getRole().getValue());
        payload.put("exp", epochSeconds);
        String secret = "secret";

        String signature = hmacSha256(encode(JWT_HEADER.getBytes()) + "." + encode(payload.toJSONString().getBytes()), secret);
        return encode(JWT_HEADER.getBytes()) + "." + encode(payload.toJSONString().getBytes()) + "." + signature;
    }

    public boolean isValidToken(String token, UserDetails userDetails) {
        String userName = extractUserNameFromJWT(token);
        return userName.equals(userDetails.getUsername()) && verifyExpiration(token);
    }
}