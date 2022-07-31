package com.joshuawgucapstone.questionanswerspacetests;

import com.joshuawgucapstone.questionanswerspace.MultipleChoiceNumberQuestion;
import com.joshuawgucapstone.questionanswerspace.QuestionStub;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


public class MultipleChoiceNumberQuestionTest {

    @Test
    public void MultipleChoiceNumberQuestion_AnswersGeneratedRandomly_AllAnswersDiffer()
    {
        QuestionStub _questionStub = new QuestionStub(1, "", "9", true);
        var testQuestion = new MultipleChoiceNumberQuestion(_questionStub);
        var answers = testQuestion.AnswerList;
        var answer0 = Double.parseDouble(answers.get(0));
        var answer1 =  Double.parseDouble(answers.get(1));
        var answer2 =  Double.parseDouble(answers.get(2));
        var answer3 =  Double.parseDouble(answers.get(3));
        assertTrue(answer0 != answer1 && answer0 != answer2 && answer0 != answer3 && answer1 != answer2
                && answer1 != answer3 && answer2 != answer3);
    }
}