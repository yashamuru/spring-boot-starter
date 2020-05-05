package com.mensageo.app.repository;

import com.mensageo.app.model.Book;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {
  List<Book> findByTitle(String title);
}
