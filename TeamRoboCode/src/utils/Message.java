package utils;

import java.io.Serializable;

public class Message implements Serializable {
    String sender;
    String receiver;
    String content;
    int tipo; // 0 -> just communication; 1 -> turn to enemy and shoot;
    Position position; // 1 -> position of the enemy selected;

    public Message(){
        this.sender = "";
        this.receiver = "";
        this.content = "";
        this.tipo = 0;
    }

    public Message(String sender, String receiver, String content, int tipo){
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.tipo = tipo;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
