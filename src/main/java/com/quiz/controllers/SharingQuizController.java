package com.quiz.controllers;


import com.quiz.dto.QuizDto;
import com.quiz.entities.Quiz;
import com.quiz.entities.ResponcePaginatedList;
import com.quiz.entities.ResponseText;
import com.quiz.entities.User;
import com.quiz.service.PaginationService;
import com.quiz.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/quizzes")
public class SharingQuizController {

    @Autowired
    QuizService quizService;
    @Autowired
    PaginationService paginationService;

    @GetMapping("/{quizId}")
    public ResponseEntity<Quiz> getQuiz(@PathVariable int quizId) {
        return ResponseEntity.ok(quizService.findQuizById(quizId));
    }

    @GetMapping("/{pageSize}/{pageNumber}")
    public ResponseEntity<ResponcePaginatedList<Quiz>> getAllQuizzes(@PathVariable int pageSize, @PathVariable int pageNumber) {
        List<Quiz> quizzes = quizService.findAllQuizzes();
        return ResponseEntity.ok(new ResponcePaginatedList<Quiz>(paginationService.paginate(quizzes, pageSize, pageNumber), quizzes.size()));
    }

    @GetMapping("/categories/{categoryId}/{pageSize}/{pageNumber}")
    public ResponseEntity<ResponcePaginatedList<Quiz>> getQuizzesByCategory(@PathVariable int categoryId, @PathVariable int pageSize, @PathVariable int pageNumber) {
        List<Quiz> quizzes = quizService.findQuizzesByCategory(categoryId);
        return ResponseEntity.ok(new ResponcePaginatedList<Quiz>(paginationService.paginate(quizzes, pageSize, pageNumber), quizzes.size()));
    }

    @GetMapping("/tags/{tagId}")
    public ResponseEntity<List<Quiz>> getQuizzesByTag(@PathVariable int tagId) {
        return ResponseEntity.ok(quizService.findQuizzesByTag(tagId));
    }

    @GetMapping("/{name}")
    public ResponseEntity<List<Quiz>> getQuizzesByName(@PathVariable String name) {
        return ResponseEntity.ok(quizService.findQuizzesByName(name));
    }

    @PostMapping("/update_quiz")
    public ResponseEntity<Quiz> updateQuizInfo(@RequestBody Quiz quiz) {

        boolean isRecordAffected = quizService.updateQuiz(quiz);

        if (isRecordAffected) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PostMapping("/new_quiz")
    public ResponseEntity<QuizDto> insert(@RequestBody Quiz quiz) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(quizService.insertQuiz(quiz));
    }

    @GetMapping("/get_image/{quizId}")
    public ResponseEntity<ResponseText> getQuizImage(@PathVariable int quizId) {
        return ResponseEntity.ok(new ResponseText(new String(Base64.getEncoder().encode(quizService.getImageByQuizId(quizId)))));
    }

    @PostMapping("/new_image/{quizId}")
    public ResponseEntity<String> changeQuizImage(@RequestParam(value = "image") MultipartFile image, @PathVariable int quizId) {
        boolean isRecordAffected = quizService.updateQuizImage(image, quizId);
        if (isRecordAffected) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }


    @PostMapping("/add_tag")
    public ResponseEntity<String> addTagToQuiz(@RequestParam(value = "quizId") int quizId, @RequestParam(value = "tagId") int tagId) {
        boolean isRecordAffected = quizService.addTag(quizId, tagId);
        return ResponseEntity.status(isRecordAffected ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/top_quizzes")
    public ResponseEntity<List<Quiz>> getTopQuizzes(@RequestParam(value = "limit") int limit) {
        return ResponseEntity.ok(quizService.findTopPopularQuizzes(limit));
    }

    @GetMapping("/top_quizzes/{categoryId}")
    public ResponseEntity<List<Quiz>> getTopQuizzesByCategory(@RequestParam(value = "limit") int limit, @PathVariable int categoryId) {
        return ResponseEntity.ok(quizService.findTopPopularQuizzesByCategory(categoryId, limit));
    }

    @GetMapping("/recent_quizzes/{userId}")
    public ResponseEntity<List<Quiz>> getRecentQuizzes(@RequestParam(value = "limit") int limit, @PathVariable int userId) {
        return ResponseEntity.ok(quizService.findRecentGames(userId, limit));
    }
}
