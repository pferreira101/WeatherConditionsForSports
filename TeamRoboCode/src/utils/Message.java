package utils;

import java.io.Serializable;

public class Message implements Serializable {
    String sender;
    String receiver;
    String content;

    public Message(String sender, String receiver, String content){
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;

    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getContent() {
        return content;
    }

}
