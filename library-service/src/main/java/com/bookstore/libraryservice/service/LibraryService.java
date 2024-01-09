package com.bookstore.libraryservice.service;

import com.bookstore.libraryservice.client.BookServiceClient;
import com.bookstore.libraryservice.dto.AddBookRequest;
import com.bookstore.libraryservice.dto.LibraryDto;
import com.bookstore.libraryservice.exception.LibraryNotFoundException;
import com.bookstore.libraryservice.model.Library;
import com.bookstore.libraryservice.repository.LibraryRepository;
import com.example.bookservice.dto.BookId;
import com.example.bookservice.dto.BookServiceGrpc;
import com.example.bookservice.dto.Isbn;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class LibraryService {
    private final LibraryRepository libraryRepository;
    private final BookServiceClient bookServiceClient;

    public LibraryService(LibraryRepository libraryRepository, BookServiceClient bookServiceClient) {
        this.libraryRepository = libraryRepository;
        this.bookServiceClient = bookServiceClient;
    }

    @GrpcClient("book-service")
    private BookServiceGrpc.BookServiceBlockingStub bookServiceBlockingStub;

    public LibraryDto getAllBooksInLibraryById(String id){
        Library library = libraryRepository.findById(id)
                .orElseThrow(() -> new LibraryNotFoundException("Library could not found by id :" +id));

        return new LibraryDto(library.getId(), library.getUserBook()
                .stream()
                .map(bookServiceClient::getBookById)
                .map(ResponseEntity::getBody)
                .collect(Collectors.toList()));
    }

    public LibraryDto create(){
        Library library = libraryRepository.save(new Library());

        return new LibraryDto(library.getId());
    }

    public void addBookToLibrary(AddBookRequest request){
        BookId bookIdByIsbn = bookServiceBlockingStub.getBookIdByIsbn(Isbn.newBuilder().setIsbn(request.getIsbn()).build());
        String bookId = bookIdByIsbn.getBookId();

        Library library = libraryRepository.findById(request.getId())
                .orElseThrow(() -> new LibraryNotFoundException("Library could not found by id: " + request.getId()));

        library.getUserBook()
                .add(bookId);

        libraryRepository.save(library);
    }
}
