package com.example.mota;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;


public class Utility {
    public static String timeStampToString(Timestamp timestamp) {
        if (timestamp != null) {
            return new SimpleDateFormat("dd MMMM yyyy, EEEE").format(timestamp.toDate());
        } else {
            return "";
        }
    }
    public static String encrypt(String plaintext, int shiftKey) {
        StringBuilder encryptedText = new StringBuilder();
        for (char c : plaintext.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                encryptedText.append((char) ((c - base + shiftKey) % 26 + base));
            } else {
                encryptedText.append(c);
            }
        }
        return encryptedText.toString();
    }

    public static String decrypt(String encryptedText, int shiftKey) {
        return encrypt(encryptedText, 26 - shiftKey); // Decryption is just encryption with the inverse shift
    }
}
