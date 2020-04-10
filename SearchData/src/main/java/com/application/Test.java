package com.application;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/** @author PG */
public class Test {
  public static void main(String[] args) throws Exception {

    /*Map<String, List<String>> value = new HashMap<>();
    List<String> test = new ArrayList<>();
    test.add("one");
    value.put("key", test);
    String result = JSONObject.toJSONString(value);
    JSONObject jsonObject = JSONObject.parseObject(result);
    Map<String, List<String>> one = JSONObject.toJavaObject(jsonObject, Map.class);
    System.out.println("数量为" + one.get("key").get(0));*/

    /* List<Integer> lists=new ArrayList<>();
    lists.add(1);
    lists.add(2);
    lists.remove(lists.size()-1);
    System.out.println(lists.get(lists.size()-1));*/

    /* String name="#加长才##凉菜#";
    String[] kind=name.split("#");
    for(String one : kind){
      if(!one.isEmpty())
      System.out.println(one);
    }*/

    /* Random random = new Random();
        for (int i = 0; i < 10; i++) System.out.println(random.nextLong());
    */
    /*
    String a="dadf";
    System.out.println(a.substring(1,3));


    Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
    Logger.getLogger("org.apache.http.client").setLevel(Level.OFF);

    java.util.logging.Logger.getLogger("org.apache.http.wire")
        .setLevel(java.util.logging.Level.FINEST);
    java.util.logging.Logger.getLogger("org.apache.http.headers")
        .setLevel(java.util.logging.Level.FINEST);*/
    /* System.setProperty(
            "org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.loggin g.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "ERROR");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "ERROR");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.headers", "ERROR");
    */
    String url = "https://www.meishij.net/zuofa/kelejichi_15.html";
    /* WebClient webClient = new WebClient();
    webClient.getOptions().setJavaScriptEnabled(true); // 启用JS解释器，默认为true
    webClient.getOptions().setCssEnabled(false); // 禁用css支持
    webClient.getOptions().setThrowExceptionOnScriptError(false); // js运行错误时，是否抛出异常
    webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
    webClient.getOptions().setTimeout(10 * 1000); // 设置连接超时时间

    WebRequest request = new WebRequest(new URL(url));
    request.setAdditionalHeader(
        "User-Agent",
        "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:62.0) Gecko/20100101 Firefox/62.0");
    request.setAdditionalHeader(
        "Cookie",
        "PLAY_LANG=cn; _plh=b9289d0a863a8fc9c79fb938f15372f7731d13fb; PLATFORM_SESSION=39034d07000717c664134556ad39869771aabc04-_ldi=520275&_lsh=8cf91cdbcbbb255adff5cba6061f561b642f5157&csrfToken=209f20c8473bc0518413c226f898ff79cd69c3ff-1539926671235-b853a6a63c77dd8fcc364a58&_lpt=%2Fcn%2Fvehicle_sales%2Fsearch&_lsi=1646321; _ga=GA1.2.2146952143.1539926675; _gid=GA1.2.1032787565.1539926675; _plh_notime=8cf91cdbcbbb255adff5cba6061f561b642f5157");

    try {

      HtmlPage page = webClient.getPage(request);
      Thread.sleep(1000);
      String pageAsXml = page.asXml();
      // webClient.waitForBackgroundJavaScript(30 * 1000); // 等待js后台执行30秒
      Document document = Jsoup.parse(pageAsXml);
      String viewClickNum = document.getElementById("viewclicknum").text();
      System.out.println("点击数量为：" + viewClickNum);
      String collectNum = document.getElementById("f_num").text();
      System.out.println("收藏的次数为：" + collectNum);
    } catch (Exception e) {
      System.out.println("爬取请求出现异常");
    }*/
  }
}
