package com.example.common;

import com.example.entity.Image;

import java.util.logging.Logger;
import java.util.regex.Pattern;

public class AppConstant {
  public static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[^a-zA-Z0-9\\s-]");
  public static final String PATTERN = "dd-MM-yyyy";
  public static final Pattern PUBLIC_ID_PATTERN = Pattern.compile(".*/(v[0-9]+/[^.]+)\\..*");
  public static final Logger LOGGER = Logger.getLogger(Image.class.getName());

}
