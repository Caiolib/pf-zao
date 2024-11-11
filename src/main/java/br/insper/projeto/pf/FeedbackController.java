package br.insper.projeto.pf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import br.insper.projeto.pf.FeedbackService;
import br.insper.projeto.pf.Feedback;
import br.insper.projeto.pf.FeedbackDTO;
import br.insper.projeto.pf.UserResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    // POST /feedbacks (Acessível por ADMIN)
    @PostMapping
    public ResponseEntity<FeedbackDTO> criarFeedback(@RequestBody FeedbackDTO feedbackDTO, HttpServletRequest request) {
        UserResponse user = (UserResponse) request.getAttribute("user");
        if (user == null || !user.getPapel().equalsIgnoreCase("ADMIN")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Feedback feedback = new Feedback();
        feedback.setTitulo(feedbackDTO.getTitulo());
        feedback.setDescricao(feedbackDTO.getDescricao());
        feedback.setAutor(user.getNome());

        Feedback feedbackSalvo = feedbackService.salvarFeedback(feedback);

        FeedbackDTO retornoDTO = new FeedbackDTO();
        retornoDTO.setId(feedbackSalvo.getId());
        retornoDTO.setTitulo(feedbackSalvo.getTitulo());
        retornoDTO.setDescricao(feedbackSalvo.getDescricao());
        retornoDTO.setAutor(feedbackSalvo.getAutor());

        return new ResponseEntity<>(retornoDTO, HttpStatus.CREATED);
    }

    // GET /feedbacks (Acessível por ADMIN e DEVELOPERS)
    @GetMapping
    public ResponseEntity<List<FeedbackDTO>> listarFeedbacks(HttpServletRequest request) {
        UserResponse user = (UserResponse) request.getAttribute("user");
        if (user == null || !(user.getPapel().equalsIgnoreCase("ADMIN") || user.getPapel().equalsIgnoreCase("DEVELOPERS"))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<FeedbackDTO> feedbacks = feedbackService.listarFeedbacks().stream().map(feedback -> {
            FeedbackDTO dto = new FeedbackDTO();
            dto.setId(feedback.getId());
            dto.setTitulo(feedback.getTitulo());
            dto.setDescricao(feedback.getDescricao());
            dto.setAutor(feedback.getAutor());
            return dto;
        }).collect(Collectors.toList());

        return new ResponseEntity<>(feedbacks, HttpStatus.OK);
    }

    // GET /feedbacks/{id} (Acessível por ADMIN e DEVELOPERS)
    @GetMapping("/{id}")
    public ResponseEntity<FeedbackDTO> obterFeedback(@PathVariable String id, HttpServletRequest request) {
        UserResponse user = (UserResponse) request.getAttribute("user");
        if (user == null || !(user.getPapel().equalsIgnoreCase("ADMIN") || user.getPapel().equalsIgnoreCase("DEVELOPERS"))) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return feedbackService.obterFeedbackPorId(id).map(feedback -> {
            FeedbackDTO dto = new FeedbackDTO();
            dto.setId(feedback.getId());
            dto.setTitulo(feedback.getTitulo());
            dto.setDescricao(feedback.getDescricao());
            dto.setAutor(feedback.getAutor());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // DELETE /feedbacks/{id} (Apenas ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFeedback(@PathVariable String id, HttpServletRequest request) {
        UserResponse user = (UserResponse) request.getAttribute("user");
        if (user == null || !user.getPapel().equalsIgnoreCase("ADMIN")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        feedbackService.deletarFeedback(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(HttpServletRequest request) {
        UserResponse user = (UserResponse) request.getAttribute("user");

        // Se o usuário não estiver autenticado ou o token for inválido
        if (user == null) {
            return new ResponseEntity<>("Token inválido ou expirado", HttpStatus.UNAUTHORIZED);
        }

        // Retorna as informações do usuário autenticado
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}



