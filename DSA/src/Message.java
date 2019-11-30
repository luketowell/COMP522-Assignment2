import java.security.Key;

public class Message {
    private String originalMessage;
    private byte[] hashedMessage;
    private Key pubKey;

    public Message(String origMess, byte[] cipherText, Key senderPubKey){
        originalMessage = origMess;
        hashedMessage = cipherText;
        pubKey = senderPubKey;
    }

    public Key getPubKey() {
        return pubKey;
    }

    public String getOriginalMessage() {
        return originalMessage;
    }

    public byte[] getHashedMessage() {
        return hashedMessage;
    }
}

