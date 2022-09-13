package ee.kull.sms.service;

import ee.kull.sms.entity.Question;

import java.util.List;

public interface QuestionServise {

    List<Question> getAllQuestions();
    List<Question> getQuestionsByTopic(String topic);

    Question saveQuestion(Question question);

    Question updateQuestion(Question question);

    Question getQuestionById(Long id);

    void deleteQuestionById(Long id);




}
