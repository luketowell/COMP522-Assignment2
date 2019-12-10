import javax.crypto.Cipher;
import java.security.Signature;

public class Verifier {
    public static void main(String[] args) {

    }
    public static void verifyMessage (Message senderMessage) throws Exception{
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        Signature dsa = Signature.getInstance("SHA256withDSA");

        //Initiate verifier hashed input.
        byte[] verifierHashedInput = "".getBytes();

        //If the message has been intercepted and changed then again the signatures will not match when they are generated.
        String interceptedMessage = "This is not a sample message";

        //assign received plain text to verifier variable
        String verifierPlainText = senderMessage.getOriginalMessage();

        System.out.println("sent plainText: " + senderMessage.getOriginalMessage());
        System.out.println("received plainText: " + verifierPlainText);
        System.out.println("received signature: \n" + Utils.toHex(senderMessage.getSignedHash()));
        System.out.println("Original senders signed hash: \n" + Utils.toHex(senderMessage.getSignedHash()));

        //calculates own hashed message using the input that you have provided in the message being received
        try{
            verifierHashedInput = MessageDigestor.messageDigest(verifierPlainText);
        }catch (Exception e ){
            System.out.println("error: "+ e);
            System.exit(1);
        }

        //start verify password using the public key from the message
        dsa.initVerify(senderMessage.getPubKey());
        //run through the DSA using the hashed value that verifier has generated
        dsa.update(verifierHashedInput);

        //then verify the updated dsa against the key that was passed with the sender.
        //If the values verify then they will return true otherwise they will return false.
        boolean verifies = dsa.verify(senderMessage.getSignedHash());

        //verifier using the original message matches the sender hash which was decrypted using the public key compares the signatures to see if they match
        if(verifies){
            System.out.println("key values match");
        } else {
            System.out.println("hashed values dont match this message is not trustworthy.");
        }




    }
}
