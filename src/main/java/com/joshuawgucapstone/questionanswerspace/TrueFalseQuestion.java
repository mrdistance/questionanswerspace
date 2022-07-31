package com.joshuawgucapstone.questionanswerspace;

import com.joshuawgucapstone.questionanswerspace.IQuestion;
import com.joshuawgucapstone.questionanswerspace.QuestionStub;

import java.util.ArrayList;
import java.util.List;

public class TrueFalseQuestion implements IQuestion
{
    private List<String> _answerList = new ArrayList<String>();

    public com.joshuawgucapstone.questionanswerspace.QuestionStub QuestionStub;

    public List<String> AnswerList;

    public TrueFalseQuestion(QuestionStub questionStub)
    {
        QuestionStub = questionStub;
        AnswerList = BuildAnswers();
    }

    private List<String> BuildAnswers()
    {
        _answerList.add("true");
        _answerList.add("false");
        return (ArrayList<String>) _answerList;
    }
}
