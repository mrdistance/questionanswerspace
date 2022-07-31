package com.joshuawgucapstone.questionanswerspace;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuizBuilder {

    private Random _randomGenerator;

    private List<QuestionStub> _questions;

    public QuizBuilder(List<QuestionStub> questions)
    {
        _questions = questions;
        _randomGenerator = new Random();
    }

    public List<IQuestion> GetQuestionsWithAnswers()
    {
        List<IQuestion> questionsWithAnswers = new ArrayList<>();

        Shuffle(_questions);

        for(QuestionStub question : _questions)
        {
            questionsWithAnswers.add(BuildQuestionForQuiz(question));
        }
        return questionsWithAnswers;
    }

    private IQuestion BuildQuestionForQuiz(QuestionStub questionStub)
    {
        if (IsTrueFalseQuestion(questionStub.getAnswer()))
        {
            return new TrueFalseQuestion(questionStub);
        }
        else if (IsFillInTheBlankQuestion(questionStub.getText(), questionStub.getAnswer()))
        {
            return new FillInTheBlankQuestion(questionStub);
        }
        else if (IsMultipleChoiceNumberQuestion(questionStub.getAnswer()))
        {
            return new MultipleChoiceNumberQuestion(questionStub);
        }
        else
        {
            return new MultipleChoiceWordQuestion(questionStub, BuildWordAnswerPool());
        }
    }

    private List<String> BuildWordAnswerPool()
    {
        var multipleChoiceWordAnswerPool = new ArrayList<String>();
        for(QuestionStub questionStub : _questions)
        {
            if (IsMultipleChoiceWordQuestion(questionStub.getText(), questionStub.getAnswer()))
            {
                multipleChoiceWordAnswerPool.add(questionStub.getAnswer());
            }
        }
        return multipleChoiceWordAnswerPool;
    }

    private boolean IsMultipleChoiceWordQuestion(String text, String answer)
    {
        return !(IsTrueFalseQuestion(answer) || IsFillInTheBlankQuestion(text, answer) || IsMultipleChoiceNumberQuestion(answer));
    }

    private boolean IsTrueFalseQuestion(String answer)
    {
        return (answer.equalsIgnoreCase("true") || answer.equalsIgnoreCase("false"));
    }

    private boolean IsFillInTheBlankQuestion(String text, String answer)
    {
        return text.contains(answer);
    }

    private boolean IsMultipleChoiceNumberQuestion(String answer)
    {
        try {
            Double.parseDouble(answer);
            return true;
        }catch(NumberFormatException nfe) {
            return false;
        }
    }

    private void Shuffle(List<QuestionStub> questions)
    {
        for (int i = 0; i < questions.size(); i++)
        {
            int indexOne = _randomGenerator.nextInt(questions.size());
            int indexTwo = _randomGenerator.nextInt(questions.size());
            Swap(indexOne, indexTwo, questions);
        }
    }

    private void Swap(int indexOne, int indexTwo, List<QuestionStub> questions)
    {
        QuestionStub temp = questions.get(indexOne);
        questions.set(indexOne, questions.get(indexTwo));
        questions.set(indexTwo, temp);
    }
}

