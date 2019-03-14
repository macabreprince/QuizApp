
package com.example.myapplication;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class QuestionsReference {

    @SerializedName("message")
    private List<Message> mMessage;

    public List<Message> getMessage() {
        return mMessage;
    }

    public void setMessage(List<Message> message) {
        mMessage = message;
    }

}
