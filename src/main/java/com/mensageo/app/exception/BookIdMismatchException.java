package com.mensageo.app.exception;

public class BookIdMismatchException extends RuntimeException {
  public BookIdMismatchException(long id) {
    super(String.format("Book id mismatch %d", id));
  }
}
