package com.ucoruh.picocli.io;

public interface UserInput {
  String readLine(String prompt);
  char[] readPassword(String prompt);
}
