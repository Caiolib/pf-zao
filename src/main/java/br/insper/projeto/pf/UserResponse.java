package br.insper.projeto.pf;

import lombok.Data;

@Data
public class UserResponse {
    private String nome;
    private String cpf;
    private String email;
    private String password;
    private String papel;
}
