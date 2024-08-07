package com.example.common;

import java.util.regex.Pattern;

public class AppConstant {
  private AppConstant() {
  }

  public static final String JWT_HEADER = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";

  public static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[^a-zA-Z0-9\\s-]");
  public static final String PATTERN = "dd-MM-yyyy";
  public static final Pattern PUBLIC_ID_PATTERN = Pattern.compile(".*/(v\\d+/[^.]+)\\..*");
  public static final String[] WHITE_LIST_URL = {
          "/api/v1/auth/**", "/v2/api-docs", "/v3/api-docs",
          "/v3/api-docs/**", "/swagger-resources", "/swagger-resources/**", "/configuration/ui",
          "/configuration/security", "/swagger-ui/**", "/webjars/**", "/swagger-ui.html", "/api/auth/**",
          "/api/test/**", "/authenticate" ,"/api/v1/auth/login" ,"/api/v1/auth/register",
          "/api/v1/products/getProductById/**" , "/api/v1/products/getProductByName/**" , "/api/v1/products/getProductByCreatedDate/**" , "/api/v1/products/getProductByPrice/**",
          "/api/v1/categories/getCategoriesByType/**" , "/api/v1/categories/getById/**" , "/api/v1/categories/getByName/**",
          "/api/v1/categories/getCreatedDate/**" ,  "/api/v1/categories/getCategoriesActive/**"
  };
}
