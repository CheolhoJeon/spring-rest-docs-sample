package io.charlie.restdocs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HttpResponseBody<T> {
  private String code;
  private String message;
  private T data;
}
