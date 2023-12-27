package com.bookstore.libraryservice.controller;

import com.bookstore.libraryservice.dto.AddBookRequest;
import com.bookstore.libraryservice.dto.LibraryDto;
import com.bookstore.libraryservice.service.LibraryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/library")
public class LibraryRestController {
    private final LibraryService libraryService;

    public LibraryRestController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibraryDto> getLibraryById(@PathVariable String id){
        return ResponseEntity.ok(libraryService.getAllBooksInLibraryById(id));
    }

    @PostMapping
    public ResponseEntity<LibraryDto> create(){
        return ResponseEntity.ok(libraryService.create());
    }

    @PutMapping
    public ResponseEntity<Void> addBookToLibrary(@RequestBody AddBookRequest addBookRequest){
         libraryService.addBookToLibrary(addBookRequest);
         return ResponseEntity.ok().build();
    }


}
