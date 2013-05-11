package me.yaoyuan.sierpinski.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;

public class TextResourceReader {
    public static String readTextFileFromResource(Context context, int resourceId) {
        InputStream inputStream = context.getResources().openRawResource(resourceId);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder body = new StringBuilder();
        String nextLine;
        try {
            while ((nextLine = bufferedReader.readLine()) != null) {
                body.append(nextLine).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not open resource: " + resourceId);
        }
        return body.toString();
    }
}
