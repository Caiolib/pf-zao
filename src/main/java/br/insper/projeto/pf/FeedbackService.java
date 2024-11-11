package br.insper.projeto.pf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.insper.projeto.pf.FeedbackRepository;
import br.insper.projeto.pf.Feedback;
import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    public Feedback salvarFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    public List<Feedback> listarFeedbacks() {
        return feedbackRepository.findAll();
    }

    public Optional<Feedback> obterFeedbackPorId(String id) {
        return feedbackRepository.findById(id);
    }

    public void deletarFeedback(String id) {
        feedbackRepository.deleteById(id);
    }
}
