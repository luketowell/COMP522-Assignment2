
import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.DSAParameterSpec;
import java.util.Scanner;

public class Sender {
    public static void main(String[] args) throws Exception {

        //declare requirements
        Scanner input = new Scanner(System.in);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        byte[] hashedInput = "".getBytes();
        SecureRandom random = new SecureRandom();

        //Initiate KeyPairGenerator and Set signature type.
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
        Signature dsa = Signature.getInstance("SHA256withDSA");

        // take the input text from user.
        System.out.println("Please enter text:");
        String inputPlainText = input.nextLine();


        // retrieve the hash of the input text or if that fails then exit the program.
        try{
            hashedInput = MessageDigestor.messageDigest(inputPlainText);
        }catch (Exception e ){
            System.out.println("error: "+ e);
            System.exit(1);
        }
        System.out.println("digest : " + Utils.toHex(hashedInput));

        //generate the keys

        //keys
        keyGen.initialize(2048, random);
        KeyPair pair = keyGen.generateKeyPair();
        PublicKey pubKey = pair.getPublic();
        PrivateKey privKey = pair.getPrivate();

        //Sign the hash using the private key
        dsa.initSign(privKey);
        dsa.update(hashedInput);
        byte[] signedHash = dsa.sign();

        System.out.println("DSA signature hash: \n" + Utils.toHex(signedHash));

        //generate the message
        Message senderMessage = new Message(inputPlainText, signedHash, pubKey);

        //send the message to the verifier
        Verifier.verifyMessage(senderMessage);

    }
}
