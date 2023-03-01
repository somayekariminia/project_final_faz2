package ir.maktab.project_final_faz2.jwt;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UsernameAndPasswordAuthenticationRequest {

    private String username;
    private String password;
}