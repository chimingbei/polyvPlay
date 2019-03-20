package com.ariix.caremawx.utils;

import android.util.Log;

import com.ariix.caremawx.BaseCallback;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class UploadUtils {

    public static void uploadToPolyV(File file, BaseCallback callback) {
        String polyVuploadUrl = "http://v.polyv.net/uc/services/rest?method=uploadfile";
        String status = "OK";
        try {
            URL url = new URL(polyVuploadUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            connection.setRequestMethod("POST");

            connection.setRequestProperty("Connection", "Keep-Alive");

            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Content-Type", "text/plain");


            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("writetoken", "a4ff3c39-d24c-4825-b6e2-611880f0daf9");
            jsonObject.put("JSONRPC","{\"title\": \"android test\", \"tag\":\"android tag\",\"desc\":\"android description\"}");
            jsonObject.put("Filedata", ".mp4");
            String jsonStr = java.net.URLEncoder.encode(jsonObject.toString(), "utf-8");
            dataOutputStream.writeBytes(jsonStr);
            FileInputStream fileInputStream = new FileInputStream(file);

            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
            while ((length = fileInputStream.read(buffer)) != -1) {
                dataOutputStream.write(buffer, 0, length);

            }
            dataOutputStream.flush();
            fileInputStream.close();
            dataOutputStream.close();
            if (connection.getResponseCode() == 200) {
                Log.d("UploadUtils", "文件上传成功--"+file.getName());
                callback.onSucceed(status);
            }else {
                callback.onFailed("Fail");
            }


            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String lines;
            StringBuffer stringBuffer = new StringBuffer();
            while ((lines = reader.readLine()) != null) {
                lines = URLDecoder.decode(lines, "UTF-8");
                stringBuffer.append(lines);
            }
            Log.d("Upload polyv", "return message---" + stringBuffer.toString());
            connection.disconnect();
        } catch (Exception e) {
            status = "Fail";
            e.printStackTrace();
            callback.onFailed("Fail");
            Log.d("UploadUtils", "文件上传失败--"+file.getName());

        }

    }

    public static String uploadToPolyV1(File file) {

        HashMap<String, String> map = new HashMap<>();

        map.put("writetoken", "a4ff3c39-d24c-4825-b6e2-611880f0daf9");
        map.put("JSONRPC", "{\"title\": \"android test\", \"tag\":\"android tag\",\"desc\":\"android description\"}");

        String polyVuploadUrl = "http://v.polyv.net/uc/services/rest?method=uploadfile";

        StringBuilder sb2 = new StringBuilder();
        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";

        try {
            URL url = new URL(polyVuploadUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(5 * 1000); // 缓存的最长时间
            conn.setDoInput(true);// 允许输入
            conn.setDoOutput(true);// 允许输出
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
                    + ";boundary=" + BOUNDARY);

            // 首先组拼文本类型的参数
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINEND);
                sb.append("Content-Disposition: form-data; name=\""
                        + entry.getKey() + "\"" + LINEND);
                sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
                sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
                sb.append(LINEND);
                sb.append(entry.getValue());
                sb.append(LINEND);
            }

            DataOutputStream outStream = new DataOutputStream(
                    conn.getOutputStream());
            outStream.write(sb.toString().getBytes());
            InputStream in = null;

            StringBuilder sb1 = new StringBuilder();
            sb1.append(PREFIX);
            sb1.append(BOUNDARY);
            sb1.append(LINEND);
            // name是post中传参的键 filename是文件的名称
            sb1.append("Content-Disposition: form-data; name=\"Filedata\"; filename=\""
                    + file.getName() + "\""  + LINEND);
            sb1.append("Content-Type: application/octet-stream; charset="
                    + CHARSET + LINEND);
            sb1.append(LINEND);
            outStream.write(sb1.toString().getBytes());

            InputStream is = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }

            is.close();
            outStream.write(LINEND.getBytes());

            // 请求结束标志
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
            outStream.write(end_data);
            outStream.flush();
            // 得到响应码
            int res = conn.getResponseCode();
            if (res == 200) {
                // 读取返回数据
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream(), "UTF-8")); //$NON-NLS-1$
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb2.append(line).append("\n"); //$NON-NLS-1$
                }
                reader.close();
            }
            outStream.close();
            conn.disconnect();

            return sb2.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Fail";

    }


}
