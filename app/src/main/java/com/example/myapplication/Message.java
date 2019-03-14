
package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Message {

    @SerializedName("ans1")
    private String mAns1;
    @SerializedName("ans2")
    private String mAns2;
    @SerializedName("ans3")
    private String mAns3;
    @SerializedName("ans4")
    private String mAns4;
    @SerializedName("correct_answer")
    private int mCorrectAnswer;
    @SerializedName("question")
    private String mQuestion;

    public String getAns1() {
        return mAns1;
    }

    public void setAns1(String ans1) {
        mAns1 = ans1;
    }

    public String getAns2() {
        return mAns2;
    }

    public void setAns2(String ans2) {
        mAns2 = ans2;
    }

    public String getAns3() {
        return mAns3;
    }

    public void setAns3(String ans3) {
        mAns3 = ans3;
    }

    public String getAns4() {
        return mAns4;
    }

    public void setAns4(String ans4) {
        mAns4 = ans4;
    }

    public int getCorrectAnswer() {
        return mCorrectAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        mCorrectAnswer = correctAnswer;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public void setQuestion(String question) {
        mQuestion = question;
    }

}
