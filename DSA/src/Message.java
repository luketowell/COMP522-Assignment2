import java.security.Key;
import java.security.PublicKey;

public class Message {
    private String originalMessage;
    private byte[] signedHash;
    private PublicKey pubKey;

    public Message(String origMess, byte[] senderSignedHash, PublicKey senderPubKey){
        originalMessage = origMess;
        signedHash = senderSignedHash;
        pubKey = senderPubKey;
    }

    public PublicKey getPubKey() {
        return pubKey;
    }

    public String getOriginalMessage() {
        return originalMessage;
    }

    public byte[] getSignedHash() {
        return signedHash;
    }
}

