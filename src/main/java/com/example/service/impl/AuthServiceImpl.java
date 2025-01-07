package com.example.service.impl;

import com.example.dto.AuthenticationDto;
import com.example.dto.AuthorizationDto;
import com.example.dto.RegisterDto;
import com.example.dto.UserDto;
import com.example.entity.User;
import com.example.exception.BadRequestException;
import com.example.mapper.UserMapper;
import com.example.service.AuthService;
import com.example.service.JwtTokenService;
import com.example.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.common.MessageConstant.*;

@Service
public class AuthServiceImpl implements AuthService {
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private JwtTokenService jwtTokenService;


    public AuthServiceImpl(UserService userService, PasswordEncoder passwordEncoder, JwtTokenService jwtTokenService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
    }


    public void register(RegisterDto registerDto) {
        if (userService.findUserByEmail(registerDto.getUserName()) != null) {
            if (!registerDto.getConfirmPassword().equals(registerDto.getPassword())) {
                throw new BadRequestException(CONFIRM_PASSWORD_INCORRECT);
            }
            UserDto userDto = UserMapper.toUserDto(registerDto);
            userDto.setActive(true);
            userService.addUser(userDto);
        }

    }

    @Override
    public void logout(String authorizationHeader) {
        if (jwtTokenService.verifyExpiration(authorizationHeader)) {
            User user = userService.getUserByRefreshToken(authorizationHeader);
            if (user == null) {
                throw new BadRequestException(TOKEN_INVALID);
            }
            user.setRefreshToken(null);
            userService.save(user);

        }
    }


    @Override
    public AuthorizationDto login(AuthenticationDto authenticationDto) {

        UserDetails userDetails = userService.loadUserByUsername(authenticationDto.getUserName());
        String jwtToken = createTokenByValidAccount(authenticationDto.getUserName(), authenticationDto.getPassword());

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .toList();

        String refreshToken = jwtTokenService.createRefreshToken(jwtToken);
        User user = userService.findUserByEmail(authenticationDto.getUserName());
        if (!user.isActive()) {
            throw new BadRequestException(ITEM_UNACTIVED);
        }
        user.setRefreshToken(refreshToken);
        userService.save(user);
        return new AuthorizationDto(jwtToken, refreshToken, user.getId(), user.getUsername(), user.isActive(), roles);
    }

    @Override
    public String createTokenByValidAccount(String email, String rawPassword) {
        User user = userService.findUserByEmail(email);
        if (user != null) {
            boolean checkPassword = passwordEncoder.matches(rawPassword, user.getPassword());
            if (!checkPassword) {
                throw new BadRequestException(EMAIL_PASSWORD_INVALID);
            }
            return jwtTokenService.createToken(email);
        } else {
            throw new BadRequestException(EMAIL_PASSWORD_INVALID);
        }
    }
}
