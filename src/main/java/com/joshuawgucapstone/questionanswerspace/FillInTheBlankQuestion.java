package com.joshuawgucapstone.questionanswerspace;

import java.util.ArrayList;

public class FillInTheBlankQuestion implements IQuestion
{
    public com.joshuawgucapstone.questionanswerspace.QuestionStub QuestionStub;

    public ArrayList<String> AnswerList;

    public FillInTheBlankQuestion(QuestionStub questionStub)
    {
        QuestionStub = questionStub;
        AnswerList = new ArrayList<String>();
    }
}