package gob.cnr.expedientes.payload.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private String token;
    @Builder.Default
    private String type = "Bearer";
    private String username;
    private List<String> roles;
    private boolean authenticated;

    public LoginResponse(String token, String username, List<String> roles, boolean authenticated) {
        this.token = token;
        this.type = "Bearer";
        this.username = username;
        this.roles = roles;
        this.authenticated = authenticated;
    }
}
