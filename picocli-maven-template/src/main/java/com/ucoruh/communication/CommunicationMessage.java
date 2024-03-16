package com.ucoruh.communication;

public class CommunicationMessage {
  @FieldEncoding(value = EncodingType.ASCII, minLength = 1, maxLength = 50)
  public String name;

  @FieldEncoding(value = EncodingType.ASCII, minLength = 1, maxLength = 50)
  public String surname;

  @FieldEncoding(value = EncodingType.INTEGER, minLength = 0, maxLength = 150)
  public int age;

  @FieldEncoding(value = EncodingType.BINARY, minLength = 1, maxLength = 16)
  public byte[] securityKey;

  @FieldEncoding(value = EncodingType.ASCII, minLength = 1, maxLength = 50)
  public String residence;

}
