package ee.kull.sms.controller;

import ee.kull.sms.entity.Question;
import ee.kull.sms.entity.QuestionForm;
import ee.kull.sms.service.QuestionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class QuestionController {

    @Autowired
    private QuestionServiceImpl service;


    @GetMapping("/questions")
    public String listOfQuestions(Model model, String topic){
        if(topic!=null){
            model.addAttribute("questions",service.getQuestionsByTopic(topic));}
        else {
        model.addAttribute("questions",service.getAllQuestions());}

        return "questions";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String QuestionsByTopic(@RequestParam(value="topic") String topic, Model model){
        model.addAttribute("questions",service.getQuestionsByTopic(topic));
        return "questions";
    }

    @GetMapping("/questions/new")
    public String createQuestionForm(Model model){
        Question question=new Question();
        model.addAttribute("question",question);
        return "create_question";
    }

    @PostMapping("/questions")
    public String saveQuestion(@ModelAttribute("question") Question question){
        service.saveQuestion(question);
        return "redirect:/questions";
    }

    @GetMapping("/questions/edit/{id}")
    public String editQuestionForm(@PathVariable Long id, Model model){
        model.addAttribute("question",service.getQuestionById(id));
        return "edit_question";
    }

    @PostMapping("/questions/{id}")
    public String updateQuestion(@PathVariable Long id,
                                 @ModelAttribute("question")
                                Question question,
                                 Model model){
        Question existingQuestion = new Question();
                existingQuestion.setId(id);
                existingQuestion.setTopic(question.getTopic());
                existingQuestion.setRank(question.getRank());
                existingQuestion.setTitle(question.getTitle());
                existingQuestion.setAnswerA(question.getAnswerA());
                existingQuestion.setAnswerB(question.getAnswerB());
                existingQuestion.setAnswerC(question.getAnswerC());
        service.updateQuestion(existingQuestion);
        return "redirect:/questions";
    }

    @GetMapping("/questions/{id}")
    public String deleteQuestion(@PathVariable("id") Long id){
        service.deleteQuestionById(id);
        return "redirect:/questions";
    }


    @GetMapping("/")
    public String start() {
        return "start";
}
    @PostMapping("/quiz")
    public String quiz(@RequestParam String username, Model m, RedirectAttributes ra) {
        if(username.equals("")) {
            ra.addFlashAttribute("warning", "Please, enter your name");
            return "redirect:/";
        }
        QuestionForm qForm = service.getQuestions();
        m.addAttribute("qForm", qForm);

        return "quiz";
    }
}
