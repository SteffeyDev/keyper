package com.example.keypermobile;

import java.util.ArrayList;

interface IPasswordGenerator
{
      int DEFAULT_PASSWORD_LENGTH = 15;
      int SEEK_BAR_MIN = 8;
      int SEEK_BAR_MAX = 40;

      String ALL_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";
      String NUMBERS = "0123456789";
      String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
      String SPECIAL_CHARACTERS = "~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";
      String NUMBERS_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
      String NUMBERS_SPECIAL_CHARACTERS = "0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";
      String LETTERS_SPECIAL_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";



      String generatePassword(int length, String characterSet);
}
