import java.security.MessageDigest;

/**
 *
 */
public class MessageDigestor {
    public static void main(String[] args) throws Exception {
    }

    public static byte[] messageDigest(String input) throws Exception {

        java.security.MessageDigest hash = java.security.MessageDigest.getInstance("SHA1");

        hash.update(Utils.toByteArray(input));

        return hash.digest();
    }
}