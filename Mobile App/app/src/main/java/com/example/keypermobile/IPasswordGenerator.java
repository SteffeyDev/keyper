package com.example.keypermobile;

import java.util.ArrayList;

interface IPasswordGenerator
{
      int DEFAULT_PASSWORD_LENGTH = 12;
      int SEEK_BAR_MIN = 6;
      int SEEK_BAR_MAX = 64;
      int SEEK_BAR_STEP = 1;

      String generatePassword(int length);
}
