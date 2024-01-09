package com.bookstore.bookservice.service;

import com.bookstore.bookservice.dto.BookIdDto;
import com.bookstore.bookservice.exception.BookNotFoundException;
import com.bookstore.bookservice.repository.BookRepository;
import com.example.bookservice.dto.BookId;
import com.example.bookservice.dto.BookServiceGrpc;
import com.example.bookservice.dto.Isbn;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class BookGrpcService extends BookServiceGrpc.BookServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(BookGrpcService.class);
    private final BookRepository bookRepository;

    public BookGrpcService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void getBookIdByIsbn(Isbn isbn, StreamObserver<BookId> responseObserver) {
        logger.info("Grpc call received: " + isbn.getIsbn());
        BookIdDto bookId = bookRepository.findByIsbn(isbn.getIsbn())
                .map(book -> new BookIdDto(book.getId(), book.getIsbn()))
                .orElseThrow(() -> new BookNotFoundException("Book could not found by isbn: " + isbn));
        responseObserver.onNext(
                BookId.newBuilder()
                        .setBookId(bookId.getBookId())
                        .setIsbn(bookId.getIsbn())
                        .build());
        responseObserver.onCompleted();
    }
}
