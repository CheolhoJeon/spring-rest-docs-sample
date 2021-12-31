package io.charlie.restdocs;

import io.charlie.restdocs.dto.BookRequestDto;
import io.charlie.restdocs.dto.BookResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

@WebMvcTest(BookController.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class BookWebLayerTest {

    @Autowired
    private MockMvc mockMvc;

    private WebTestClient webTestClient;

    private final FieldDescriptor[] bookResponseFieldSnippet = new FieldDescriptor[]{
            fieldWithPath("id").description("ID description"),
            fieldWithPath("title").description("Title description"),
            fieldWithPath("author").description("Author description"),
            fieldWithPath("price").description("Price description"),
            fieldWithPath("description").description("Description description").optional()
    };

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        this.webTestClient = MockMvcWebTestClient
                .bindTo(mockMvc)
                .baseUrl("https://api.charlie.io")
                .filter(
                        documentationConfiguration(restDocumentation)
                                .operationPreprocessors()
                                .withRequestDefaults(prettyPrint())
                                .withResponseDefaults(prettyPrint()))
                .build();
    }

    @Test
    public void registerTest() {
        this.webTestClient
                .post()
                .uri("/books")
                .bodyValue(new BookRequestDto.Register("자료구조", "세종", 18000L, "세종대왕의 자료구조 교재"))
                .exchange()
                .expectStatus().isEqualTo(CREATED)
                .expectBody(new ParameterizedTypeReference<HttpResponseBody<BookResponseDto.Register>>() {
                })
                .consumeWith(response -> {
                    HttpResponseBody<BookResponseDto.Register> responseBody = response.getResponseBody();
                    // Assertions...
                })
                .consumeWith(
                        document(
                                "books/register",
                                requestFields(
                                        fieldWithPath("title").description("Title description"),
                                        fieldWithPath("author").description("Author description"),
                                        fieldWithPath("price").description("Price description"),
                                        fieldWithPath("description").description("Description description").optional()
                                ),
                                responseFields(
                                        beneathPath("data").withSubsectionId("data"),
                                        fieldWithPath("id").description("Id description")
                                )
                        )
                );
    }

    @Test
    public void findAllTest() {
        this.webTestClient
                .get()
                .uri("/books")
                .exchange()
                .expectStatus().isEqualTo(OK)
                .expectBody(new ParameterizedTypeReference<HttpResponseBody<List<BookResponseDto.Find>>>() {
                })
                .consumeWith(response -> {
                    HttpResponseBody<List<BookResponseDto.Find>> responseBody = response.getResponseBody();
                    // Assertions...
                })
                .consumeWith(
                        document(
                                "books/findAll",
                                responseFields(
                                        beneathPath("data").withSubsectionId("data"),
                                        bookResponseFieldSnippet
                                )
                        )
                );
    }

    @Test
    public void findByIdTest() {
        this.webTestClient
                .get()
                .uri("/books/{id}", 1)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(OK)
                .expectBody(new ParameterizedTypeReference<HttpResponseBody<BookResponseDto.Find>>() {
                })
                .consumeWith(response -> {
                    HttpResponseBody<BookResponseDto.Find> responseBody = response.getResponseBody();
                    // Assertions...
                })
                .consumeWith(
                        document(
                                "books/findById",
                                responseFields(
                                        beneathPath("data").withSubsectionId("data"),
                                        bookResponseFieldSnippet
                                ),
                                pathParameters(
                                        parameterWithName("id").description("도서의 식별자")
                                )
                        )
                );
    }

}
