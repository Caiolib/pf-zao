package br.insper.projeto.pf;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@Document(collection = "feedbacks")
@Data
public class Feedback {
    @Id
    private String id;
    private String titulo;
    private String descricao;
    private String autor;
}
