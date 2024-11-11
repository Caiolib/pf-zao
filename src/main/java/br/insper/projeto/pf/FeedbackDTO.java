package br.insper.projeto.pf;

import lombok.Data;

@Data
public class FeedbackDTO {
    private String id;
    private String titulo;
    private String descricao;
    private String autor;
}
