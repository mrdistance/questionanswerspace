package com.joshuawgucapstone.questionanswerspace;

public class Quiz{
    private int quizId;
    private int accountId;
    private String topic;
    private String title;
    private String author;
    private boolean isPublic;

    public Quiz(int quizId, int accountId, String topic, String title, String author, boolean isPublic){
        this.quizId = quizId;
        this.accountId = accountId;
        this.topic = topic;
        this.title = title;
        this.author = author;
        this.isPublic = isPublic;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}
