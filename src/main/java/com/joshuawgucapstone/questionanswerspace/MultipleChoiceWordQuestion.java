package com.joshuawgucapstone.questionanswerspace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MultipleChoiceWordQuestion implements IQuestion {

        private List<String> _answerList = new ArrayList<String>();

        public List<String> LocalAnswerPool;

        public com.joshuawgucapstone.questionanswerspace.QuestionStub QuestionStub;

        public List<String> AnswerList;

        public MultipleChoiceWordQuestion(QuestionStub questionStub, List<String> globalAnswerPool)
        {
            //todo this might reference same list
            LocalAnswerPool = globalAnswerPool;
            QuestionStub = questionStub;
            AnswerList = BuildAnswers();
        }

        private ArrayList<String> BuildAnswers()
        {
            int numberOfAnswerChoices = 4;
            if (LocalAnswerPool.size() < numberOfAnswerChoices)
            {
            InsertDefaultAnswers(numberOfAnswerChoices - LocalAnswerPool.size());
            }
            InsertIncorrectAnswers(numberOfAnswerChoices);
            InsertCorrectAnswer();
            return (ArrayList<String>) _answerList;
        }

        private void InsertDefaultAnswers(int answerSlotsRemaining)
        {
            var defaultAnswers = new ArrayList<String>(Arrays.asList(

                    "That is a good question...",
                    "How about you tell me!",
                    "If I knew that, I would not be sitting here..."
            ));

            for (int i = 0; i < answerSlotsRemaining; i++)
            {
            LocalAnswerPool.add(defaultAnswers.get(i));
            }
        }

        private void InsertIncorrectAnswers(int numberOfAnswerChoices)
        {
            for (int i = 0; i < numberOfAnswerChoices; i++)
            {
                Random generator = new Random();
                int answerPoolLength = LocalAnswerPool.size();
                int withdrawIndex = generator.nextInt(answerPoolLength);
                String answerFromPool = LocalAnswerPool.get(withdrawIndex);
                _answerList.add(answerFromPool);
                LocalAnswerPool.remove(answerFromPool);
            }
        }

        private void InsertCorrectAnswer()
        {
            if (_answerList.contains(QuestionStub.getAnswer()))
            {
                return;
            }
            Random generator = new Random();
            var insertIndex = generator.nextInt(_answerList.size());
            _answerList.add(insertIndex, QuestionStub.getAnswer());
        }
}

