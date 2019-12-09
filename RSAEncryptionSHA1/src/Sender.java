
import javax.crypto.Cipher;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.util.Scanner;

public class Sender {
    public static void main(String[] args) throws Exception {

        // declare requirements
        Scanner input = new Scanner(System.in);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        byte[] hashedInput = "".getBytes();
        SecureRandom random = new SecureRandom();
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");

        // take the input text
        System.out.println("Please enter text:");
        String inputPlainText = input.nextLine();

        // retrieve the hash of the input text or if that fails then exit the program.
        try {
            hashedInput = MessageDigestor.messageDigest(inputPlainText);
        } catch (Exception e) {
            System.out.println("error: " + e);
            System.exit(1);
        }
        System.out.println("sender digest : " + Utils.toHex(hashedInput));

        // generate the keys
        generator.initialize(512, random);

        // keys
        KeyPair pair = generator.generateKeyPair();
        Key pubKey = pair.getPublic();
        Key privKey = pair.getPrivate();

        // encrypt the hashed input and print out
        cipher.init(Cipher.ENCRYPT_MODE, privKey);
        byte[] cipherText = cipher.doFinal(hashedInput);
        System.out.println("cipher: " + Utils.toHex(cipherText));
        System.out.println("Message sent by sender: " + inputPlainText);


        // generate the message
        Message senderMessage = new Message(inputPlainText, cipherText, pubKey);

        // send the message to the verifier
        Verifier.verifyMessage(senderMessage);

    }
}
