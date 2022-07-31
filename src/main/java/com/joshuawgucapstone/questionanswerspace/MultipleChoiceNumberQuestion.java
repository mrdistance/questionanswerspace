package com.joshuawgucapstone.questionanswerspace;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MultipleChoiceNumberQuestion implements IQuestion
    {
        private List<String> _answerList = new ArrayList<>();

        public com.joshuawgucapstone.questionanswerspace.QuestionStub QuestionStub;

        public List<String> AnswerList;

    public MultipleChoiceNumberQuestion(QuestionStub questionStub)
    {
        QuestionStub = questionStub;

        AnswerList = BuildAnswers();
    }

    private List<String> BuildAnswers()
    {
        int numberOfAnswerChoices = 4;
        InsertIncorrectAnswers(numberOfAnswerChoices);
        InsertCorrectAnswer();
        return _answerList;
    }

    private void InsertIncorrectAnswers(int numberOfAnswerChoices)
    {
        for (int i = 0; i < numberOfAnswerChoices; i++)
        {
            InsertUniqueIncorrectAnswer();
        }
    }

    private void InsertUniqueIncorrectAnswer()
    {
        var incorrectAnswerCandidate = GetIncorrectAnswerCandidate();
        if (!(_answerList.contains(String.valueOf(incorrectAnswerCandidate))))
        {
            _answerList.add(String.valueOf(incorrectAnswerCandidate));
        return;
        }
        InsertUniqueIncorrectAnswer();
    }

    private double GetIncorrectAnswerCandidate()
    {
        var answer = Double.parseDouble(QuestionStub.getAnswer());
        var maxOffset = GetMaxOffset(answer);
        Random generator = new Random();
        var answerOffset = generator.nextInt(maxOffset) * 2 - maxOffset;
        return answer - answerOffset;
    }

    private int GetMaxOffset(double answer)
    {
        var offset = 1;
        while (answer > 10)
        {
            answer /= 10;
            offset *= 10;
        }
        return offset > 1 ? offset : 5;
    }

    private void InsertCorrectAnswer()
    {
        Random generator = new Random();
        var index = generator.nextInt(_answerList.size());
        _answerList.add(index, String.valueOf(Double.parseDouble(QuestionStub.getAnswer())));
    }
}

