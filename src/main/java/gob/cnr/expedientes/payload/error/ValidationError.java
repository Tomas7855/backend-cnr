package gob.cnr.expedientes.payload.error;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidationError {

    private String field;
    private String message;
    private Object rejectedValue;
}
