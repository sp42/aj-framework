package com.ajaxjs.api.time_signature;

import com.ajaxjs.util.cryptography.AesCrypto;
import lombok.Data;

/**
 * Used for:
 *  1) App public API, not browser 2) make a link for password reset
 */
@Data
public class TimeSignature {
    /**
     * 秘钥，需要保密
     */
    private String secretKey = "your_secret_key";

    /**
     * 默认 15分钟的过期时间
     */
    private int expirationMin = 15;

    private long expirationTime = expirationMin * 60 * 1000;

    /**
     * Verify the time signature, check the time if is overtime.
     *
     * @param signature The signature
     * @return Whether the signature is valid
     */
    public boolean verifySignature(String signature) {
        String timestampStr;
        try {
            timestampStr = AesCrypto.getInstance().AES_decode(signature, secretKey);
        } catch (Exception e) {
            throw new SecurityException("Invalid signature: " + signature);
        }

        long timestamp;

        try {
            timestamp = Long.parseLong(timestampStr);
        } catch (NumberFormatException e) {
            throw new SecurityException("Invalid timestamp format.");
        }

        return Math.abs(System.currentTimeMillis() - timestamp) <= expirationTime;
    }

    /**
     * Generate the signature.
     *
     * @param timestamp The time stamp
     * @return signature
     */
    public String generateSignature(long timestamp) {
        String timestampStr = String.valueOf(timestamp);

        return AesCrypto.getInstance().AES_encode(timestampStr, secretKey);
    }

    /**
     * Generate the signature.
     *
     * @return signature
     */
    public String generateSignature() {
        return generateSignature(System.currentTimeMillis());
    }
}
