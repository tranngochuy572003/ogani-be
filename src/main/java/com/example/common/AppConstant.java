package com.example.common;

import java.util.regex.Pattern;

public class AppConstant {
  public static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[^a-zA-Z0-9]");
  public static final String PATTERN = "dd-MM-yyyy";
}
