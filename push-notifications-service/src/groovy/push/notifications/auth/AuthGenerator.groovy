package push.notifications.auth

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import java.security.NoSuchAlgorithmException

class AuthGenerator {

    public static void main(String[] args) {
        println generateAuthToken()
    }

    static String generateAuthToken() {
        String url = 'http://notification-hub-spike-ns.servicebus.windows.net/notification-hub-spike'
        String keyName = 'DefaultFullSharedAccessSignature'
        String key = '2RGERRaCmfG4O/fHTVts29RwCVZ1WcfhabxaAAgyOpc='

        try {
            url = URLEncoder.encode(url, 'UTF-8').toLowerCase(Locale.getDefault());
        } catch (UnsupportedEncodingException e) {
            // this shouldn't happen because of the fixed encoding
        }

        // Set expiration in seconds
        Calendar expireDate = Calendar.getInstance(TimeZone.getTimeZone('UTC'));
        expireDate.add(Calendar.MINUTE, 3600);
        long expires = expireDate.getTimeInMillis() / 1000;

        // sign
        String toSign = url + '\n' + expires;
        byte[] bytesToSign = toSign.getBytes();
        Mac mac = null;
        try {
            mac = Mac.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            // This shouldn't happen because of the fixed algorithm
        }

        SecretKeySpec secret = new SecretKeySpec(key.getBytes(), mac.getAlgorithm());
        mac.init(secret);
        byte[] signedHash = mac.doFinal(bytesToSign);
        String base64Signature = Base64.encoder.encodeToString(signedHash);
        base64Signature = base64Signature.trim();
        try {
            base64Signature = URLEncoder.encode(base64Signature, 'UTF-8');
        } catch (UnsupportedEncodingException e) {
            // this shouldn't happen because of the fixed encoding
        }

        // construct authorization string
        String token = "SharedAccessSignature sr=" + url + "&sig=" + base64Signature + "&se=" + expires + "&skn=" + keyName;

        return token;
    }
}
