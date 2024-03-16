package com.ucoruh.communication; // Assuming this is the correct package for all classes

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MessageEncoderDecoder {

  public static byte[] encodeMessage(CommunicationMessage message) throws IllegalAccessException, IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    Field[] fields = CommunicationMessage.class.getDeclaredFields();

    for (Field field : fields) {
      field.setAccessible(true);

      if (field.isAnnotationPresent(FieldEncoding.class)) {
        EncodingType encodingType = field.getAnnotation(FieldEncoding.class).value();
        int minLength = field.getAnnotation(FieldEncoding.class).minLength();
        int maxLength = field.getAnnotation(FieldEncoding.class).maxLength();
        Object fieldValue = field.get(message);
        byte[] encodedValue = encodeValue(encodingType, fieldValue, minLength, maxLength);
        // Write length of the data
        outputStream.write((byte) encodedValue.length);
        // Write encoded data
        outputStream.write(encodedValue);
      }
    }

    return outputStream.toByteArray();
  }

  public static CommunicationMessage decodeMessage(byte[] encodedMessage) throws IllegalAccessException, IOException {
    CommunicationMessage message = new CommunicationMessage();

    ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedMessage);

    Field[] fields = CommunicationMessage.class.getDeclaredFields();

    for (Field field : fields) {
      field.setAccessible(true);

      if (field.isAnnotationPresent(FieldEncoding.class)) {
        EncodingType encodingType = field.getAnnotation(FieldEncoding.class).value();
        int minLength = field.getAnnotation(FieldEncoding.class).minLength();
        int maxLength = field.getAnnotation(FieldEncoding.class).maxLength();
        // Read length of the data
        int length = inputStream.read();

        if (length == -1) {
          throw new IllegalArgumentException("Unexpected end of input stream");
        }

        // Read encoded data
        byte[] encodedValue = new byte[length];
        int bytesRead = inputStream.read(encodedValue);

        if (bytesRead != length) {
          throw new IllegalArgumentException("Failed to read expected number of bytes");
        }

        Object decodedValue = decodeValue(encodingType, encodedValue, minLength, maxLength);
        field.set(message, decodedValue);
      }
    }

    return message;
  }

  private static byte[] encodeValue(EncodingType encodingType, Object value, int minLength, int maxLength) {
    switch (encodingType) {
      case ASCII:
        String stringValue = (String) value;

        if (stringValue.length() < minLength || stringValue.length() > maxLength) {
          throw new IllegalArgumentException("Invalid length for ASCII field: " + stringValue.length());
        }

        return encodeAsAscii(stringValue);

      case INTEGER:
        int intValue = (int) value;

        if (intValue < minLength || intValue > maxLength) {
          throw new IllegalArgumentException("Integer field out of range: " + intValue);
        }

        return encodeAsInteger(intValue);

      case BINARY:
        byte[] byteArrayValue = (byte[]) value;

        if (byteArrayValue.length < minLength || byteArrayValue.length > maxLength) {
          throw new IllegalArgumentException("Invalid length for binary field: " + byteArrayValue.length);
        }

        return byteArrayValue;

      default:
        return new byte[0];
    }
  }

  private static Object decodeValue(EncodingType encodingType, byte[] encodedValue, int minLength, int maxLength) {
    switch (encodingType) {
      case ASCII:
        String decodedAscii = decodeAsAscii(encodedValue);

        if (decodedAscii.length() < minLength || decodedAscii.length() > maxLength) {
          throw new IllegalArgumentException("Decoded ASCII value out of range: " + decodedAscii);
        }

        return decodedAscii;

      case INTEGER:
        int decodedInteger = decodeAsInteger(encodedValue);

        if (decodedInteger < minLength || decodedInteger > maxLength) {
          throw new IllegalArgumentException("Decoded integer value out of range: " + decodedInteger);
        }

        return decodedInteger;

      case BINARY:
        if (encodedValue.length < minLength || encodedValue.length > maxLength) {
          throw new IllegalArgumentException("Decoded binary value out of range: " + encodedValue.length);
        }

        return encodedValue;

      default:
        return null;
    }
  }

  private static String decodeAsAscii(byte[] encodedData) {
    return new String(encodedData, StandardCharsets.US_ASCII);
  }

  private static int decodeAsInteger(byte[] encodedData) {
    int result = 0;

    for (byte b : encodedData) {
      result = result << 8 | (b & 0xFF);
    }

    return result;
  }

  private static byte[] encodeAsAscii(String data) {
    return data.getBytes(StandardCharsets.US_ASCII);
  }

  private static byte[] encodeAsInteger(int value) {
    byte[] bytes = new byte[Integer.BYTES];

    for (int i = 0; i < Integer.BYTES; ++i) {
      bytes[Integer.BYTES - 1 - i] = (byte) (value >> (i * 8));
    }

    return bytes;
  }
}
