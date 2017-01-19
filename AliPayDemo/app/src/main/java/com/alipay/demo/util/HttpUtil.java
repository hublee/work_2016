package com.alipay.demo.util;


import org.apache.log4j.Logger;

import javax.net.ssl.*;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Vector;

/**
 * Created by hlib on 2015/9/16 0016.
 */
public class HttpUtil {

    private static String defaultContentEncoding = Charset.defaultCharset().name();
    private static Logger log = Logger.getLogger(HttpUtil.class.getName());


    public static SSLSocketFactory init() throws Exception {
        class MyX509TrustManager implements X509TrustManager {
            public MyX509TrustManager() throws Exception {
                // do nothing
            }
            @Override
            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
            @Override
            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
                /*
                log.info("authType is " + authType);
                log.info("cert issuers");

                try{
                    for (int i = 0; i < chain.length; i++) {
                        log.info("\t" + chain[i].getIssuerX500Principal().getName());
                        log.info("\t" + chain[i].getIssuerDN().getName());
                        chain[i].checkValidity();
                    }
                }catch(CertificateExpiredException ex){
                    log.error("checkDate: Certificate has expired");
                }catch(CertificateNotYetValidException yet){
                    log.error("checkDate: Certificate is not yet valid");
                }catch(Exception ee){
                    log.error("Error: "+ee.getMessage());
                }*/
            }
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[] {};
            }
        }
        TrustManager[] tm = { new MyX509TrustManager() };
        System.setProperty("https.protocols", "TLSv1");
        SSLContext sslContext = SSLContext.getInstance("TLSv1","SunJSSE");
        sslContext.init(null, tm, new SecureRandom());
        SSLSocketFactory ssf = sslContext.getSocketFactory();
        return ssf;
    }



    public static String doPostByHttps(String POST_URL,String content) {
        StringBuffer resp = new StringBuffer();
        HttpsURLConnection con = null;
        try {
            URL myURL = new URL(POST_URL);
            con = (HttpsURLConnection) myURL.openConnection();
            HostnameVerifier hostNameVerify = new HostnameVerifier() {
                /**
                 * Always return true
                 */
                public boolean verify(String urlHostName, SSLSession session) {
                    return true;
                }
            };
            //HttpsURLConnection.setDefaultHostnameVerifier(hostNameVerify);
            con.setHostnameVerifier(hostNameVerify);
            try {
                con.setSSLSocketFactory(init());
            } catch (Exception e1) {
//                throw new IOException(e1);
                log.error(e1);
            }
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("POST");
            con.setUseCaches(false);
            con.setInstanceFollowRedirects(true);
            con.setRequestProperty("Content-Type", "application/xml");
            con.connect();
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            content = URLEncoder.encode(content, "utf-8");
            out.writeBytes(content);
            out.flush();
            out.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                resp.append(line);
            }
            log.info("HTTPS POST response : " + resp);
            reader.close();
            con.disconnect();
        }catch (Exception e){
            e.printStackTrace();
            log.error("https post 请求出错", e);
        }finally {
            if(null != con){
                con.disconnect();
            }
        }
        return resp.toString();
    }


    public static String doGetByHttps(String addr){
        HttpsURLConnection conn = null;
        StringBuffer stb = new StringBuffer();
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] { new
                    X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        public void checkClientTrusted(X509Certificate[] certs,
                                                       String authType) {
                        }
                        public void checkServerTrusted(X509Certificate[] certs,
                                                       String authType) {
                        }
                    } };
            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            URL url = new URL(addr);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
            conn.connect();
            System.out.println(conn.getResponseCode() + " " + conn.getResponseMessage());
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                stb.append(line);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(conn != null){
                conn.disconnect();
            }
        }
        return stb.toString();
    }

    /**
     * 发送GET请求
     *
     * @param urlString
     *            URL地址
     * @return 响应对象
     * @throws IOException
     */
    public static HttpRespons sendGet(String urlString) throws IOException {
        return send(urlString, "GET", null, null);
    }

    /**
     * 发送GET请求
     *
     * @param urlString
     *            URL地址
     * @param params
     *            参数集合
     * @return 响应对象
     * @throws IOException
     */
    public static HttpRespons sendGet(String urlString, Map<String, String> params)
            throws IOException {
        return send(urlString, "GET", params, null);
    }

    /**
     * 发送GET请求
     *
     * @param urlString
     *            URL地址
     * @param params
     *            参数集合
     * @param propertys
     *            请求属性
     * @return 响应对象
     * @throws IOException
     */
    public static HttpRespons sendGet(String urlString, Map<String, String> params,
                               Map<String, String> propertys) throws IOException {
        return send(urlString, "GET", params, propertys);
    }

    /**
     * 发送POST请求
     *
     * @param urlString
     *            URL地址
     * @return 响应对象
     * @throws IOException
     */
    public static HttpRespons sendPost(String urlString) throws IOException {
        return send(urlString, "POST", null, null);
    }

    /**
     * 发送POST请求
     *
     * @param urlString
     *            URL地址
     * @param params
     *            参数集合
     * @return 响应对象
     * @throws IOException
     */
    public static HttpRespons sendPost(String urlString, Map<String, String> params)
            throws IOException {
        return send(urlString, "POST", params, null);
    }

    /**
     * 发送POST请求
     *
     * @param urlString
     *            URL地址
     * @param params
     *            参数集合
     * @param propertys
     *            请求属性
     * @return 响应对象
     * @throws IOException
     */
    public static HttpRespons sendPost(String urlString, Map<String, String> params,
                                Map<String, String> propertys) throws IOException {
        return send(urlString, "POST", params, propertys);
    }

    /**
     * 发送HTTP请求
     *
     * @param urlString
     * @return 响映对象
     * @throws IOException
     */
    private static HttpRespons send(String urlString, String method,
                             Map<String, String> parameters, Map<String, String> propertys)
            throws IOException {

        log.info("Http："  + method +"：" + urlString + "  params：" + parameters);
        HttpURLConnection urlConnection = null;

        if (method.equalsIgnoreCase("GET") && parameters != null) {
            StringBuffer param = new StringBuffer();
            int i = 0;
            for (String key : parameters.keySet()) {
                if (i == 0)
                    param.append("?");
                else
                    param.append("&");
                param.append(key).append("=").append(parameters.get(key));
                i++;
            }
            urlString += param;
        }
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod(method);
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setUseCaches(false);

        if (propertys != null)
            for (String key : propertys.keySet()) {
                urlConnection.addRequestProperty(key, propertys.get(key));
            }

        if (method.equalsIgnoreCase("POST") && parameters != null) {
            StringBuffer param = new StringBuffer();
            for (String key : parameters.keySet()) {
                param.append("&");
                param.append(key).append("=").append(String.valueOf(parameters.get(key)));
            }
            try {
                urlConnection.getOutputStream().write(param.toString().getBytes("utf-8"));
            }catch (Exception e){
                e.printStackTrace();
                log.error(e);
            }
            urlConnection.getOutputStream().flush();
            urlConnection.getOutputStream().close();
        }

        return makeContent(urlString, urlConnection);
    }


    /**
     * 得到响应对象
     *
     * @param urlConnection
     * @return 响应对象
     * @throws IOException
     */
    private static HttpRespons makeContent(String urlString,
                                    HttpURLConnection urlConnection) throws IOException {
        HttpRespons httpResponser = new HttpRespons();
        try {
            InputStream in = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(in,"UTF-8"));
            httpResponser.contentCollection = new Vector<String>();
            StringBuffer temp = new StringBuffer();
            String line = bufferedReader.readLine();
            while (line != null) {
                httpResponser.contentCollection.add(line);
                temp.append(line).append("\r\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();

            String ecod = urlConnection.getContentEncoding();
            if (ecod == null)
                ecod = defaultContentEncoding;

            httpResponser.urlString = urlString;

            httpResponser.defaultPort = urlConnection.getURL().getDefaultPort();
            httpResponser.file = urlConnection.getURL().getFile();
            httpResponser.host = urlConnection.getURL().getHost();
            httpResponser.path = urlConnection.getURL().getPath();
            httpResponser.port = urlConnection.getURL().getPort();
            httpResponser.protocol = urlConnection.getURL().getProtocol();
            httpResponser.query = urlConnection.getURL().getQuery();
            httpResponser.ref = urlConnection.getURL().getRef();
            httpResponser.userInfo = urlConnection.getURL().getUserInfo();
            httpResponser.content = new String(temp.toString().getBytes(), ecod);
            httpResponser.contentEncoding = ecod;
            httpResponser.code = urlConnection.getResponseCode();
            httpResponser.message = urlConnection.getResponseMessage();
            httpResponser.contentType = urlConnection.getContentType();
            httpResponser.method = urlConnection.getRequestMethod();
            httpResponser.connectTimeout = urlConnection.getConnectTimeout();
            httpResponser.readTimeout = urlConnection.getReadTimeout();

            return httpResponser;
        } catch (IOException e) {
            throw e;
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
    }

    /**
     * 默认的响应字符集
     */
    public String getDefaultContentEncoding() {
        return this.defaultContentEncoding;
    }

    /**
     * 设置默认的响应字符集
     */
    public void setDefaultContentEncoding(String defaultContentEncoding) {
        this.defaultContentEncoding = defaultContentEncoding;
    }


    //请求方法
    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        try {

            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            return buffer.toString();
        } catch (ConnectException ce) {
            System.out.println("连接超时：{}"+ ce);
        } catch (Exception e) {
            System.out.println("https请求异常：{}"+ e);
        }
        return null;
    }


}
