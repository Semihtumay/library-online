package com.bookstore.libraryservice.service;

import com.bookstore.libraryservice.client.BookServiceClient;
import com.bookstore.libraryservice.dto.AddBookRequest;
import com.bookstore.libraryservice.dto.LibraryDto;
import com.bookstore.libraryservice.exception.LibraryNotFoundException;
import com.bookstore.libraryservice.model.Library;
import com.bookstore.libraryservice.repository.LibraryRepository;
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

    public void addBookToLibrary(AddBookRequest addBookRequest){
        String bookId = Objects.requireNonNull(bookServiceClient.getBookByIsbn(addBookRequest.getIsbn()).getBody()).getBookId();

        Library library = libraryRepository.findById(addBookRequest.getId())
                .orElseThrow(() -> new LibraryNotFoundException("Library could not found by id :" + addBookRequest.getId()));

        library.getUserBook().add(bookId);

        libraryRepository.save(library);
    }
}
