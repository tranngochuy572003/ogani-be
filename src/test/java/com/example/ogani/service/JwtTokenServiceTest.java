package com.example.ogani.service;

import com.example.entity.User;
import com.example.enums.UserRole;
import com.example.exception.ForbiddenException;
import com.example.service.UserService;
import com.example.service.impl.JwtTokenServiceImpl;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;
import uk.org.webcompere.systemstubs.jupiter.SystemStub;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;

import java.text.ParseException;
import java.time.*;
import java.util.Date;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SystemStubsExtension.class)
class JwtTokenServiceTest {
    @SystemStub
    private EnvironmentVariables variables =
            new EnvironmentVariables("SECRET_KEY", "secret");
    @Spy
    @InjectMocks
    JwtTokenServiceImpl jwtTokenService;

    @Mock
    private UserService userService;

    private User user;
    @Mock
    private JWT jwtMock;
    @Mock
    private JWTClaimsSet claimsSetMock;
    @Captor
    ArgumentCaptor<String> userNameCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUserName("userName@gmail.com");
        user.setFullName("fullName");
        user.setAddress("address");
        user.setPhoneNumber("0917000749");
        user.setId("userId");
        user.setRole(UserRole.CUSTOMER);
    }

    @Test
    void testCreateTokenThenSuccess() {
        when(userService.findUserByEmail(user.getUsername())).thenReturn(user);
        ZoneId vietnamZoneId = ZoneId.of("Asia/Ho_Chi_Minh");
        LocalDateTime nowInVietnam = LocalDateTime.now(vietnamZoneId).plusHours(24);
        long expectedEpochSeconds = nowInVietnam.toEpochSecond(ZoneOffset.ofHours(7));

        JSONObject payload = new JSONObject();
        payload.put("userName", user.getUsername());
        payload.put("role", user.getRole().getValue());
        payload.put("exp", expectedEpochSeconds);

        jwtTokenService.createToken(user.getUsername());

        verify(userService).findUserByEmail(userNameCaptor.capture());
        Assertions.assertEquals("userName@gmail.com", userNameCaptor.getValue());
    }


    @Test
    void testRefreshTokenThenSuccess() {
        when(userService.findUserByEmail(user.getUsername())).thenReturn(user);
        jwtTokenService.refreshToken(user.getUsername());
        verify(userService).findUserByEmail(userNameCaptor.capture());
        Assertions.assertEquals(user.getUsername(), userNameCaptor.getValue());
    }



    @Test
    void testCreateRefreshTokenThenSuccess() {
        when(userService.findUserByEmail(user.getUsername())).thenReturn(user);

        ZoneId vietnamZoneId = ZoneId.of("Asia/Ho_Chi_Minh");
        LocalDateTime nowInVietnam = LocalDateTime.now(vietnamZoneId).plusHours(24);
        long expectedEpochSeconds = nowInVietnam.toEpochSecond(ZoneOffset.ofHours(7));

        JSONObject payload = new JSONObject();
        payload.put("userName", user.getUsername());
        payload.put("role", user.getRole().getValue());
        payload.put("exp", expectedEpochSeconds);

        String token = jwtTokenService.createToken(user.getUsername());
        jwtTokenService.createRefreshToken(token);

        verify(jwtTokenService).refreshToken(userNameCaptor.capture());
        Assertions.assertEquals("userName@gmail.com", userNameCaptor.getValue());
    }

    @Test
    void testCreateRefreshTokenThenThrowBadRequest() throws ParseException {
        when(userService.findUserByEmail(user.getUsername())).thenReturn(user);
        String token = jwtTokenService.createToken(user.getUsername());

        try (MockedStatic<JWTParser> jwtParserMock = mockStatic(JWTParser.class)) {
            MockedStatic<ZonedDateTime> zonedDateTime = mockStatic(ZonedDateTime.class);
            jwtParserMock.when(() -> JWTParser.parse(anyString())).thenReturn(jwtMock);
            when(jwtMock.getJWTClaimsSet()).thenReturn(claimsSetMock);
            when(claimsSetMock.getClaim("userName")).thenReturn("userName");

            Instant instantExpirationTime = mock(Instant.class);
            Date exTime = mock(Date.class);
            when(claimsSetMock.getExpirationTime()).thenReturn(exTime);
            when(exTime.toInstant()).thenReturn(instantExpirationTime);

            ZonedDateTime zonedDateTimeMock = mock(ZonedDateTime.class);
            zonedDateTime.when(() -> ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant()).thenReturn(zonedDateTimeMock);
            Instant instantNow = mock(Instant.class);

            when(zonedDateTimeMock.toInstant()).thenReturn(instantNow);

            when(instantExpirationTime.isAfter(instantNow)).thenReturn(false);
            assertThrows(ForbiddenException.class, () -> jwtTokenService.createRefreshToken(token));
        }

    }

    @Test
    void testIsValidTokenThenSuccess() {
        when(userService.findUserByEmail(user.getUsername())).thenReturn(user);
        ZoneId vietnamZoneId = ZoneId.of("Asia/Ho_Chi_Minh");
        LocalDateTime nowInVietnam = LocalDateTime.now(vietnamZoneId).plusHours(24);
        long expectedEpochSeconds = nowInVietnam.toEpochSecond(ZoneOffset.ofHours(7));

        JSONObject payload = new JSONObject();
        payload.put("userName", user.getUsername());
        payload.put("role", user.getRole().getValue());
        payload.put("exp", expectedEpochSeconds);

        String token = jwtTokenService.createToken(user.getUsername());
        boolean isValid = jwtTokenService.isValidToken(token, user);
        Assertions.assertTrue(isValid);

    }


}
