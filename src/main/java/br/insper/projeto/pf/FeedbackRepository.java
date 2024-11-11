package br.insper.projeto.pf;

import org.springframework.data.mongodb.repository.MongoRepository;
import br.insper.projeto.pf.Feedback;

public interface FeedbackRepository extends MongoRepository<Feedback, String> {
}
