package ai.niksar.contract_wisor_api.util;

import jakarta.servlet.http.HttpServletRequest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.regex.Pattern;

public class Util {
    private Util(){}

    private static final    String ALGORITHM       = "AES";
    private static final    String TRANSFORMATION  = "AES";
    private static final    String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        return pattern.matcher(email).matches();
    }
    public static String getRealDate(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate currentDate = LocalDate.now();
        return currentDate.format(formatter);
    }
    public static String getRealTime(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss");
        LocalTime currentTime = LocalTime.now();
        return currentTime.format(formatter);
    }
    public static LocalDateTime dateFormatter(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        LocalDate localDate = LocalDate.parse(date, formatter);

        return localDate.atStartOfDay();
    }
    public static long getTimestamp(){
        return Instant.now().getEpochSecond();
    }

    public static String getDate(Date date){
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");
        String cancelDate = dateFormatter.format(date);
        return cancelDate;
    }

    public static String getTime(Date date){
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HHmmss");
        String cancelTime = timeFormatter.format(date);
        return cancelTime;
    }

    public static String encrypt(String input, String key) throws Exception{
        try {
            Cipher          cipher  = Cipher.getInstance(TRANSFORMATION);
            SecretKeySpec   keySpec = new SecretKeySpec(key.getBytes(),ALGORITHM);

            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encryptedBytes = cipher.doFinal(input.getBytes());

            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e){
            throw new Exception("An error occurred during encryption. \nError: " + e.getMessage());
        }
    }

    public static String decrypt(String input, String key) throws Exception{
        try {
            Cipher          cipher  = Cipher.getInstance(TRANSFORMATION);
            SecretKeySpec   keySpec = new SecretKeySpec(key.getBytes(), ALGORITHM);

            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decodedBytes     = Base64.getDecoder().decode(input);
            byte[] decryptedBytes   = cipher.doFinal(decodedBytes);

            return new String(decryptedBytes);
        } catch (Exception e){
            throw new Exception("An error occurred during decryption. \nError: " + e.getMessage());
        }
    }
    public static String getClientIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("X-Real-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        if (ipAddress != null && ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0];
        }
        return ipAddress;
    }

    public static String encode(String data) {
        return Base64.getEncoder().encodeToString(data.getBytes());
    }

    public static String decode(String encodedData) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedData);
        return new String(decodedBytes);
    }
    public static Boolean parseStringToBoolean(String value) {
        if ("true".equalsIgnoreCase(value)) {
            return true;
        } else if ("false".equalsIgnoreCase(value)) {
            return false;
        } else {
            return null;
        }
    }
}