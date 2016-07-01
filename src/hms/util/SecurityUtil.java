package hms.util;

import org.apache.commons.codec.digest.DigestUtils;
import sun.misc.BASE64Encoder;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by iamaprin
 * on 2016/4/28.
 */
public class SecurityUtil {

    /**
     * 生成加密用的盐
     * @param num 盐的位数
     * @return
     */
    public static String generateSalt(int num) {
        Random random = new SecureRandom();
        byte[] salt = new byte[num];
        random.nextBytes(salt);
        String str = new BASE64Encoder().encode(salt);

        return str;
    }

    /**
     * 根据密码和盐生成加密后的密码
     * @param password 密码
     * @param salt 盐
     * @return
     */
    public static String SHA256Encode(String password, String salt) {
        String str = password + salt;
        return DigestUtils.sha256Hex(str);
    }

    public static void main(String[] args) {
        System.out.println(SecurityUtil.generateSalt(32).length());
        //System.out.println(SecurityUtil.SHA256Encode("123456", SecurityUtil.generateSalt(128)));
        String str = SecurityUtil.generateSalt(32);
        System.out.println(str);
        System.out.println(SecurityUtil.SHA256Encode("admin", str));
    }
}
