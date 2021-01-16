package vip.testops.account.utils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.util.Base64;

public class AESUtil {

    public static final String KEY_ALGORITHM = "AES";
    public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    public static final String IV_STRING = "testops.vip.liud";

    public static String decrypt(String encryptValue, Key key) throws Exception{
        return aesDecryptByBytes(base64Decode(encryptValue), key);
    }

    public static String encrypt(String value, Key key) throws Exception{
        return base64Encode(aesEncryptToBytes(value, key));
    }

    public static Key generateKey(String aesKey) throws Exception{
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGenerator.init(128, new SecureRandom(aesKey.getBytes(StandardCharsets.UTF_8)));
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] encodeFormat =secretKey.getEncoded();
        return new SecretKeySpec(encodeFormat, KEY_ALGORITHM);
    }

    private static String base64Encode(byte[] bytes){
        return new String(Base64.getEncoder().encode(bytes));
    }

    private static byte[] base64Decode(String base64Code){
        if(base64Code == null){
            return null;
        }
        return Base64.getDecoder().decode(base64Code);
    }

    private static AlgorithmParameters generateIV(String ivVal) throws NoSuchAlgorithmException, InvalidParameterSpecException {
        byte[] iv = ivVal.getBytes(StandardCharsets.UTF_8);
        AlgorithmParameters parameters = AlgorithmParameters.getInstance(KEY_ALGORITHM);
        parameters.init(new IvParameterSpec(iv));
        return parameters;
    }

    private static byte[] aesEncryptToBytes(String content, Key key) throws Exception{
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        AlgorithmParameters iv = generateIV(IV_STRING);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        return cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
    }

    private static String aesDecryptByBytes(byte[] encryptBytes, Key key) throws Exception{
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        AlgorithmParameters iv = generateIV(IV_STRING);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }

}
