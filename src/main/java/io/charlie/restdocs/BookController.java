package io.charlie.restdocs;

import io.charlie.restdocs.dto.BookRequestDto;
import io.charlie.restdocs.dto.BookResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/books")
public class BookController {

    private static TreeMap<Long, Book> books = new TreeMap<>();

    static {
        books.put(1L, new Book(1L, "데이터베이스", "홍길동", 25000L, "홍길동의 데이터베이스 시스템 교재"));
        books.put(2L, new Book(2L, "운영체제", "이몽룡", 23000L, null));
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public HttpResponseBody<BookResponseDto.Register> register(@RequestBody BookRequestDto.Register request) {
        Book newBook = new Book(
            books.lastKey() + 1L,
            request.getTitle(),
            request.getAuthor(),
            request.getPrice(),
            request.getDescription()
        );
        books.put(newBook.getId(), newBook);

        return HttpResponseBody.<BookResponseDto.Register>builder()
            .code("BOOK-RES001")
            .message("도서를 성공적으로 등록하였습니다.")
            .data(new BookResponseDto.Register(newBook.getId()))
            .build();
    }

    @GetMapping
    public HttpResponseBody<List<BookResponseDto.Find>> findAll(@RequestParam final int count) {
        return HttpResponseBody.<List<BookResponseDto.Find>>builder()
            .code("BOOK-RES002")
            .message("요청이 성공적으로 수행되었습니다.")
            .data(
                books.values()
                    .stream()
                    .map(Book::toResponseFindDto)
                    .limit(count)
                    .collect(Collectors.toList())
            )
            .build();
    }

    @GetMapping("/{id}")
    public HttpResponseBody<BookResponseDto.Find> findById(@PathVariable final Long id) {
        return HttpResponseBody.<BookResponseDto.Find>builder()
            .code("BOOK-RES002")
            .message("요청이 성공적으로 수행되었습니다.")
            .data(books.get(id).toResponseFindDto())
            .build();
    }

}
