package com.example.config;

import com.example.dto.UserDtoLogin;
import com.example.entity.User;
import com.example.service.UserService;
import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class JwtTokenProvider {
  private static final String JWT_HEADER = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
  @Autowired
  private UserService userService;

  public static String encode(byte[] bytes) {
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  }

  private String hmacSha256(String data, String secret) {
    try {
      byte[] hash = secret.getBytes(StandardCharsets.UTF_8);
      Mac sha256Hmac = Mac.getInstance("HmacSHA256");
      SecretKeySpec secretKey = new SecretKeySpec(hash, "HmacSHA256");
      sha256Hmac.init(secretKey);
      byte[] signedBytes = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
      return encode(signedBytes);
    } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
      Logger.getLogger(JwtTokenProvider.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
      return null;
    }
  }

  public String createToken(UserDtoLogin userDtoLogin)  {
    User user = userService.findUserByEmail(userDtoLogin.getUserName());
    LocalDateTime ldt = LocalDateTime.now().plusHours(1);
    JSONObject payload = new JSONObject();
    payload.put("userName", user.getUsername());
    payload.put("role", user.getRole());
    payload.put("exp", ldt.toEpochSecond(ZoneOffset.UTC));
    String secret = "secret";
    String signature = hmacSha256(encode(JWT_HEADER.getBytes()) + "." + encode(payload.toJSONString().getBytes()), secret);
    return encode(JWT_HEADER.getBytes()) + "." + encode(payload.toJSONString().getBytes()) + "." + signature;
  }
}
