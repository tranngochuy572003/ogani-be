package com.example.util;

import com.example.exception.BadRequestException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.regex.Matcher;

import static com.example.common.AppConstant.*;
import static com.example.common.MessageConstant.FIELD_INVALID;

public class AppUtil {
  public static boolean containsSpecialCharacters(String s) {
    Matcher matcher = SPECIAL_CHAR_PATTERN.matcher(s);
    if (s.isEmpty()) {
      return true;
    }
    return matcher.find();
  }

  public static LocalDate checkDateValid(String date) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);
    try {
      return LocalDate.parse(date, formatter);
    } catch (DateTimeParseException e) {
      throw new BadRequestException(FIELD_INVALID);
    }
  }

}
