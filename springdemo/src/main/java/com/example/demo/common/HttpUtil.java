package com.example.demo.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * <B>系统名称：</B<BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 * 
 * @author 中科软科技
 * @since 2019年7月3日
 */
@Slf4j
public class HttpUtil {

    private static final String USER_AGENT = "user-agent";
    private static final String USER_AGENT_VALUE = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)";
    private static final String CONNECTION = "connection";
    private static final String CONNECTION_VALUE = "Keep-Alive";
    private static final String ACCEPT = "accept";
    private static final String UTF8 = "utf-8";
    private static final String ACCEPT_CHARSET = "Accept-Charset";
    private static final String CONTENTTYPE = "contentType";
    private static final String SSL = "ssl";
    private static final Integer CODE = 300;

    protected HttpUtil() {

    }

    /**
     * 向指定 URL 发送GET方法的请求
     *
     * @param url 发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) throws IOException {
        String urlNameString = url;
        if (StringUtils.isNotBlank(param)) {
            urlNameString += "?" + param;
        }
        URL realUrl = new URL(urlNameString);
        URLConnection connection = realUrl.openConnection();
        StringBuilder result = new StringBuilder();
        connection.setRequestProperty(USER_AGENT, USER_AGENT_VALUE);
        connection.setRequestProperty(CONNECTION, CONNECTION_VALUE);
        connection.setRequestProperty(ACCEPT, "*/*");
        connection.connect();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        }
        catch (Exception e) {
            log.error("发送GET请求出现异常！", e);
        }
        return result.toString();
    }

    /**
     * 向指定 URL 发送GET方法的请求
     *
     * @param url 发送请求的 URL
     * @return 所代表远程资源的响应结果
     */
    public static String sendGet(String url) throws IOException {
        return sendGet(url, null);
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url 发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) throws IOException {
        StringBuilder result = new StringBuilder();

        String urlNameString = url + "?" + param;
        URL realUrl = new URL(urlNameString);
        URLConnection conn = realUrl.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty(CONTENTTYPE, UTF8);
        conn.setRequestProperty(ACCEPT_CHARSET, UTF8);
        conn.setRequestProperty(USER_AGENT, USER_AGENT_VALUE);
        conn.setRequestProperty(CONNECTION, CONNECTION_VALUE);
        conn.setRequestProperty(ACCEPT, "*/*");
        try (PrintWriter out = new PrintWriter(conn.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),
                        StandardCharsets.UTF_8))) {
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            out.flush();
            out.print(param);
        }
        catch (Exception e) {
            log.error("发送 POST 请求出现异常！", e);
        }
        return result.toString();
    }

    public static String sendSSLPost(String url, String param) {
        StringBuilder result = new StringBuilder();
        String urlNameString = url + "?" + param;
        try {
            SSLContext sc = SSLContext.getInstance(SSL);
            sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());
            URL console = new URL(urlNameString);
            HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
            conn.setRequestProperty(ACCEPT, "*/*");
            conn.setRequestProperty(CONNECTION, CONNECTION_VALUE);
            conn.setRequestProperty(USER_AGENT, USER_AGENT_VALUE);
            conn.setRequestProperty(ACCEPT_CHARSET, UTF8);
            conn.setRequestProperty(CONTENTTYPE, UTF8);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedReader indata = new BufferedReader(new InputStreamReader(is));
            String ret = "";
            while (ret != null) {
                ret = indata.readLine();
                if (ret != null && !ret.trim().equals("")) {
                    result.append(ret);
                }
            }
            conn.disconnect();
            indata.close();
        }
        catch (Exception e) {
            log.error("发送SSL POST 请求出现异常！", e);
        }
        return result.toString();
    }

    public static String httpsPost(String url, String params) throws Exception {
        // 构建请求参数
        String result = "";
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            trustAllHosts();
            URL url2 = new URL(url);

            HttpsURLConnection urlCon = (HttpsURLConnection) url2.openConnection();
            urlCon.setHostnameVerifier(DO_NOT_VERIFY);
            urlCon.setDoOutput(true);
            urlCon.setDoInput(true);
            urlCon.setRequestMethod("POST");
            urlCon.setRequestProperty("Content-type", "application/json;charset=UTF-8");
            // 发送POST请求必须设置如下两行
            urlCon.setDoOutput(true);
            urlCon.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            OutputStream os = urlCon.getOutputStream();
            //参数是键值队  , 不以"?"开始 
            os.write(params.getBytes());
            //os.write("googleTokenKey=&username=admin&password=5df5c29ae86331e1b5b526ad90d767e4".getBytes()); 
            os.flush();
           
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(urlCon.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {// 使用finally块来关闭输出流、输入流
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public void uploadOss(String filePath, String urloss) {
        FileInputStream fileInputStream = null;
        OutputStream outputStream = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        try {
            urloss = URLDecoder.decode(urloss, "UTF-8");
            URL url = new URL(urloss);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type","application/octet-stream");                  
            urlConnection.setRequestMethod("PUT");
            File file = new File(filePath);
            //  不是必传项
            fileInputStream = new FileInputStream(file);
            outputStream = urlConnection.getOutputStream();
            byte data[] = new byte[100];
            int size = -1;
           while (-1 != (size = fileInputStream.read(data))) {
                outputStream.write(data, 0, size);
            }
            outputStream.flush();
            //  响应失败
            if (urlConnection.getResponseCode() >= CODE) {
                throw new Exception("HTTP Request is not success, Response code is " + urlConnection.getResponseCode());
            }
            //  接收响应流
            BufferedReader reader = null;
            StringBuffer resultBuffer = new StringBuffer();
            String tempLine = null;
            inputStream = urlConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }
            System.out.println(resultBuffer.toString());
            outputStream.close();
            fileInputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != outputStream) {
                try {
                    outputStream.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != fileInputStream) {
                try {
                    fileInputStream.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != inputStreamReader) {
                try {
                    inputStreamReader.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void uploadFile(String urll, String getpath) {
        try {
            URL url = new URL(urll);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);

            
            //urlConnection.setRequestProperty("Content-Type", "application/octet-stream");
            urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            urlConnection.setRequestMethod("PUT");
            File file = new File(getpath);

            // 不是必传项
            FileInputStream inputStream = new FileInputStream(file);
            OutputStream outputStream = urlConnection.getOutputStream();

            byte data[] = new byte[100];
            int size = -1;
            while (-1 != (size = inputStream.read(data))) {
                outputStream.write(data, 0, size);
            }
            outputStream.flush();

            // 响应失败
           /* if (urlConnection.getResponseCode() >= CODE) {
                throw new Exception("HTTP Request is not success, Response code is " + urlConnection.getResponseCode());
            }*/
            // 接收响应流
            InputStream inputStream2 = null;
            InputStreamReader inputStreamReader = null;
            BufferedReader reader = null;
            StringBuffer resultBuffer = new StringBuffer();
            String tempLine = null;

            inputStream2 = urlConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream2);
            reader = new BufferedReader(inputStreamReader);

            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }

            outputStream.close();
            inputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class TrustAnyTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
            //trust anything
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
            //trust anything
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }
        } };
        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
    
    public static void uploadFiles(String url,String fileName) throws Exception{
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPut httpput = new HttpPut(url);

            FileBody bin = new FileBody(new File(fileName));
            StringBody comment = new StringBody("A binary file of some kind", ContentType.TEXT_PLAIN);

            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addPart("bin", bin)
                    .addPart("comment", comment)
                    .build();


            httpput.setEntity(reqEntity);

            System.out.println("executing request " + httpput.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httpput);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    System.out.println("Response content length: " +    resEntity.getContentLength());
                }
                EntityUtils.consume(resEntity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }

    }
  
}
