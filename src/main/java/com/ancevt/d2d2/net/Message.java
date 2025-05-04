package com.ancevt.d2d2.net;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class Message {
    public enum Type {
        FAST, RELIABLE, ACK
    }

    @SerializedName("type")
    public Type type;

    @SerializedName("id")
    public long id;  // only used for RELIABLE and ACK

    @SerializedName("payload")
    public byte[] payload;

    public Message() {
    }

    public Message(Type type, long id, byte[] payload) {
        this.type = type;
        this.id = id;
        this.payload = payload;
    }

    public static Message ack(long id) {
        return new Message(Type.ACK, id, null);
    }

    @Override
    public String toString() {
        return "Message{" +
                "type=" + type +
                ", id=" + id +
                ", payload=" + Arrays.toString(payload) +
                '}';
    }
}

