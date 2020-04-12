package de.simpleprojectmanager.simpleprojectmanager.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EncryptionUtil {

    private static EncryptionUtil instance;

    private EncryptionUtil(){}

    public static EncryptionUtil getInstance() {
        if(instance==null)
            instance=new EncryptionUtil();
        return instance;
    }

    /**
     * Encrypts the given string using sha512
     *
     * @param input the input to hash
     *
     * @link https://www.geeksforgeeks.org/sha-512-hash-in-java/
     * */
    public Optional<String> hashSHA512(String input) {
        try {
            // getInstance() method is called with algorithm SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32)
                hashtext = "0" + hashtext;

            // return the HashText
            return Optional.of(hashtext);
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            return Optional.empty();
        }
    }

    /**
     * Generates a random ascii string
     *
     * @param length length of the generates string
     * */
    public String genRandomAscii(int length){
        //Creates a stream with the given length
        return IntStream.range(0,length)
                //Creates a random ascii string
                .mapToObj(i->String.valueOf((char) ThreadLocalRandom.current().nextInt(0x20,0x7f)))
                .collect(Collectors.joining());
    }

}
