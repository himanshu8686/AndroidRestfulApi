package com.yash.androidrestfulapi.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHelper {
    public static String downloadUrl(String address) {
        InputStream inputStream = null;
        try {
            URL url = new URL(address);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
          //  httpURLConnection.setReadTimeout(15000);
          //  httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode != 200) {
                throw new Exception("Error : Got response code " + responseCode);
            }

            inputStream = httpURLConnection.getInputStream();
            return readStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static String readStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream out = null;

        try {
            int length = 0;
            out = new BufferedOutputStream(byteArrayOutputStream);
            while (((length = inputStream.read(buffer)) > 0)) {
                out.write(buffer, 0, length);
            }
            out.flush();
            return byteArrayOutputStream.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        finally {
            if (out != null)
                out.close();
        }
    }
}

