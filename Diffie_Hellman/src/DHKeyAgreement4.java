/*
A Lisitsa, 2019, The code below was taken without any changes from 
https://docs.oracle.com/javase/8/docs/technotes/guides/security/crypto/CryptoSpec.html#DH3Ex
*/
/*
 * Copyright (c) 1997, 2017, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import javax.crypto.interfaces.*;
/*
 * This program executes the Diffie-Hellman key agreement protocol between
 * 3 parties: Alice, Bob, and Carol using a shared 2048-bit DH parameter.
 */
public class DHKeyAgreement4 {
    private DHKeyAgreement4() {}
    public static void main(String argv[]) throws Exception {
        // Alice creates her own DH key pair with 2048-bit key size
        System.out.println("ALICE: Generate DH keypair ...");
        KeyPairGenerator aliceKpairGen = KeyPairGenerator.getInstance("DH");
        aliceKpairGen.initialize(2048);
        KeyPair aliceKpair = aliceKpairGen.generateKeyPair();

        //Do you want to see the output from the shared key generation?
        Boolean showKeys = false;

        // This DH parameters can also be constructed by creating a
        // DHParameterSpec object using agreed-upon values
        DHParameterSpec dhParamShared = ((DHPublicKey)aliceKpair.getPublic()).getParams();
        // Bob creates his own DH key pair using the same params
        System.out.println("BOB: Generate DH keypair ...");
        KeyPairGenerator bobKpairGen = KeyPairGenerator.getInstance("DH");
        bobKpairGen.initialize(dhParamShared);
        KeyPair bobKpair = bobKpairGen.generateKeyPair();
        // Carol creates her own DH key pair using the same params
        System.out.println("CAROL: Generate DH keypair ...");
        KeyPairGenerator carolKpairGen = KeyPairGenerator.getInstance("DH");
        carolKpairGen.initialize(dhParamShared);
        KeyPair carolKpair = carolKpairGen.generateKeyPair();
        // David creates his own DH key pair using the same params
        System.out.println("CAROL: Generate DH keypair ...");
        KeyPairGenerator davidKpairGen = KeyPairGenerator.getInstance("DH");
        davidKpairGen.initialize(dhParamShared);
        KeyPair davidKpair = davidKpairGen.generateKeyPair();

        // Alice initialize
        System.out.println("ALICE: Initialize ...");
        KeyAgreement aliceKeyAgree = KeyAgreement.getInstance("DH");
        aliceKeyAgree.init(aliceKpair.getPrivate());
        // Bob initialize
        System.out.println("BOB: Initialize ...");
        KeyAgreement bobKeyAgree = KeyAgreement.getInstance("DH");
        bobKeyAgree.init(bobKpair.getPrivate());
        // Carol initialize
        System.out.println("CAROL: Initialize ...");
        KeyAgreement carolKeyAgree = KeyAgreement.getInstance("DH");
        carolKeyAgree.init(carolKpair.getPrivate());
        //David initialize
        System.out.println("DAVID: Initialize ...");
        KeyAgreement davidKeyAgree = KeyAgreement.getInstance("DH");
        davidKeyAgree.init(davidKpair.getPrivate());

        System.out.println("Iteration 0: All parties only have their own public keys");

        //gets previous persons Key
        System.out.println("Iteration 1: All parties pass public key forward one.");
        // Alice uses Davids's public key
        Key ad = aliceKeyAgree.doPhase(davidKpair.getPublic(), false);
        // Bob uses Alice's public key
        Key ba = bobKeyAgree.doPhase(aliceKpair.getPublic(), false);
        // Carol uses Bob's public key
        Key cb = carolKeyAgree.doPhase(bobKpair.getPublic(), false);
        // David uses Carol's public key
        Key dc = davidKeyAgree.doPhase(carolKpair.getPublic(), false);


        //gets previous previous persons key
        System.out.println("Iteration 2: All parties pass public key forward two.");
        // Alice uses David's result from above
        Key adc = aliceKeyAgree.doPhase(dc, false);
        // Bob uses Alice's result from above
        Key bad = bobKeyAgree.doPhase(ad, false);
        // Carol uses Bob's result from above
        Key cba = carolKeyAgree.doPhase(ba, false);
        //David uses Carols result from above
        Key dcb = davidKeyAgree.doPhase(cb, false);


        //gets previous previous previous persons Key
        System.out.println("Iteration 3: All parties pass public key forward Three.");
        // Alice uses David's result from above
        aliceKeyAgree.doPhase(dcb, true);
        // Bob uses Alice's result from above
        bobKeyAgree.doPhase(adc, true);
        // Carol uses Bob's result from above
        carolKeyAgree.doPhase(bad, true);
        //David uses Carol's result from above
        davidKeyAgree.doPhase(cba, true);

        System.out.println("All parties have all public keys for everyone in the network");


        // Alice, Bob and Carol compute their secrets
        System.out.println("Calculating shared secret key.");
        byte[] aliceSharedSecret = aliceKeyAgree.generateSecret();
        byte[] bobSharedSecret = bobKeyAgree.generateSecret();
        byte[] carolSharedSecret = carolKeyAgree.generateSecret();
        byte[] davidSharedSecret = davidKeyAgree.generateSecret();

        if (showKeys) {
            System.out.println("Alice secret: " + toHexString(aliceSharedSecret));
            System.out.println("Bob secret: " + toHexString(bobSharedSecret));
            System.out.println("Carol secret: " + toHexString(carolSharedSecret));
            System.out.println("David secret: " + toHexString(davidSharedSecret));
        }

        // Compare Alice and Bobx
        if (!java.util.Arrays.equals(aliceSharedSecret, bobSharedSecret))
            throw new Exception("Alice and Bob differ");
        System.out.println("Alice and Bob are the same");
        // Compare Bob and Carol
        if (!java.util.Arrays.equals(bobSharedSecret, carolSharedSecret))
            throw new Exception("Bob and Carol differ");
        System.out.println("Bob and Carol are the same");
        if (!java.util.Arrays.equals(carolSharedSecret, davidSharedSecret))
            throw new Exception("Carol and David differ");
        System.out.println("Carol and David are the same");
    }
    /*
     * Converts a byte to hex digit and writes to the supplied buffer
     */
    private static void byte2hex(byte b, StringBuffer buf) {
        char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        int high = ((b & 0xf0) >> 4);
        int low = (b & 0x0f);
        buf.append(hexChars[high]);
        buf.append(hexChars[low]);
    }
    /*
     * Converts a byte array to hex string
     */
    private static String toHexString(byte[] block) {
        StringBuffer buf = new StringBuffer();
        int len = block.length;
        for (int i = 0; i < len; i++) {
            byte2hex(block[i], buf);
            if (i < len-1) {
                buf.append(":");
            }
        }
        return buf.toString();
    }
}