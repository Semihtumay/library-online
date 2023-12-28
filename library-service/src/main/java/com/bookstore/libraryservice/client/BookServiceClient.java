package com.bookstore.libraryservice.client;

import com.bookstore.libraryservice.dto.BookDto;
import com.bookstore.libraryservice.dto.BookIdDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "book-service", path = "/v1/book")
public interface BookServiceClient {

    Logger logger = LoggerFactory.getLogger(BookServiceClient.class);

    @GetMapping("/isbn/{isbn}")
    @CircuitBreaker(name = "getBookByIsbnCircuitBreaker", fallbackMethod = "getBookFallback")
    ResponseEntity<BookIdDto> getBookByIsbn(@PathVariable(value = "isbn") String isbn);

    @GetMapping("/{id}")
    @CircuitBreaker(name = "getBookByIdCircuitBreaker", fallbackMethod = "getBookIdFallback")
    ResponseEntity<BookDto> getBookById(@PathVariable(value = "id") String id);

    default ResponseEntity<BookIdDto> getBookFallback(String isbn, Exception exception){
        logger.info("Book not found by isbn: " + isbn + ", returning default BookIdDto object." );
        return ResponseEntity.ok(new BookIdDto("default-bookId", "default-isbn"));
    }

    default ResponseEntity<BookDto> getBookIdFallback(String isbn, Exception exception){
        logger.info("Book not found by isbn: " + isbn + ", returning default BookIdDto object." );
        return ResponseEntity.ok(new BookDto(new BookIdDto("default-bookId", "default-isbn"),
                "default-title",
                0,
                "default-author",
                "default-press-name"));
    }
}
