package com.mensageo.app;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mensageo.app.model.Book;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@AutoConfigureMockMvc
public class BookIntegrationTest {
  static String API_ROOT = "/api/books";

  // Majority of the setup is from here -
  // https://dzone.com/articles/unit-and-integration-tests-in-spring-boot-2
  // classpath:application-integrationtest.properties is from here -
  // https://www.baeldung.com/spring-boot-testing
  @Autowired private WebApplicationContext wac;

  private MockMvc mvc;

  @Before
  public void initTest() {
    this.mvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @Test
  public void whenGetAllBooks_thenOK() throws Exception {
    createBook("A title", "Author");
    createBook("Title2", "Author2");

    this.mvc
        .perform(MockMvcRequestBuilders.get(API_ROOT).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(
            content()
                .json(
                    "[{\"title\": \"A title\", \"author\": \"Author\"},{\"title\": \"Title2\", \"author\": \"Author2\"}]",
                      false));
  }

  /*
  @Test
  public void whenGetBooksByTitle_thenOK() {
    Book book = createRandomBook();
    createBookAsUri(book);
    Response response = RestAssured.get(API_ROOT + "/title/" + book.getTitle());

    assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    assertTrue(response.as(List.class).size() > 0);
  }

  @Test
  public void whenGetCreatedBookById_thenOK() {
    Book book = createRandomBook();
    String location = createBookAsUri(book);
    Response response = RestAssured.get(location);

    assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    assertEquals(book.getTitle(), response.jsonPath().get("title"));
  }

  @Test
  public void whenGetNotExistBookById_thenNotFound() {
    Response response = RestAssured.get(API_ROOT + "/" + 9999);

    assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
  }

  @Test
  public void whenCreateNewBook_thenCreated() {
    Book book = createRandomBook();
    Response response =
        RestAssured.given().contentType(MediaType.APPLICATION_JSON_VALUE).body(book).post(API_ROOT);

    assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
  }

  @Test
  public void whenInvalidBook_thenError() {
    Book book = createRandomBook();
    book.setAuthor(null);
    Response response =
        RestAssured.given().contentType(MediaType.APPLICATION_JSON_VALUE).body(book).post(API_ROOT);

    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
  }

  @Test
  public void whenUpdateCreatedBook_thenUpdated() {
    Book book = createRandomBook();
    String location = createBookAsUri(book);
    book.setId(Long.parseLong(location.split("api/books/")[1]));
    book.setAuthor("newAuthor");
    Response response =
        RestAssured.given().contentType(MediaType.APPLICATION_JSON_VALUE).body(book).put(location);

    assertEquals(HttpStatus.OK.value(), response.getStatusCode());

    response = RestAssured.get(location);

    assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    assertEquals("newAuthor", response.jsonPath().get("author"));
  }

  @Test
  public void whenDeleteCreatedBook_thenOk() {
    Book book = createRandomBook();
    String location = createBookAsUri(book);
    Response response = RestAssured.delete(location);

    assertEquals(HttpStatus.OK.value(), response.getStatusCode());

    response = RestAssured.get(location);
    assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
  }*/

  private Book createBook(String title, String author) {
    Book book = new Book();
    book.setTitle(title);
    book.setAuthor(author);
    createBookAsUri(book);
    return book;
  }

  private Book createRandomBook() {
    return createBook(randomAlphabetic(10), randomAlphabetic(15));
  }

  private String randomAlphabetic(int i) {
    int leftLimit = 97; // letter 'a'
    int rightLimit = 122; // letter 'z'
    return new Random()
        .ints(leftLimit, rightLimit + 1)
        .limit(i)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();
  }

  private void createBookAsUri(Book book) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      // System.out.println(mapper.writeValueAsString(book));
      this.mvc.perform(
          post(API_ROOT)
              .contentType(MediaType.APPLICATION_JSON)
              .content(mapper.writeValueAsString(book)));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
