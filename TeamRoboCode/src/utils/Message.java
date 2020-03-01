package utils;

import java.io.Serializable;

public class Message implements Serializable {
    String sender;
    String receiver;
    String content;
    int tipo; // 0 -> just communication; 1 -> turn to enemy and shoot; 2 -> leader died, time do change;
    Position position; // 0 -> position of teammate; 1 -> position of the enemy selected;
    Enemy target;

    public final static int INFO = 0;
    public final static int ATTACK = 1;
    public final static int CHANGELEADER = 2;
    public final static int MOVETO = 3;
    public final static int HELP = 4;
    public final static int REQUEST_INFO = 5;


    public Message(){
        this.sender = "";
        this.receiver = "";
        this.content = "";
        this.tipo = 0;
    }

    public Message(int type){
        this.sender = "";
        this.receiver = "";
        this.content = "";
        this.tipo = type;
    }

    public Message(int type, Position pos){
        this.sender = "";
        this.receiver = "";
        this.content = "";
        this.tipo = type;
        this.position=pos;
    }

    public Message(int type, Enemy target){
        this.sender = "";
        this.receiver = "";
        this.content = "";
        this.tipo = type;
        this.target=target;
    }

    public Message(String sender, int tipo, Position pos){
        this.sender = sender;
        this.tipo = tipo;
        this.position = pos;
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

    public Enemy getTarget() {
        return target;
    }

}
