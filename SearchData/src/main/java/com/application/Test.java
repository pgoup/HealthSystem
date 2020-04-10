package com.application;

import com.fasterxml.jackson.core.io.NumberInput;
import org.hibernate.query.criteria.internal.expression.function.AggregationFunction;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import javax.management.Query;
import java.util.*;

/** @author PG */
public class Test {
  public static void main(String[] args) {
    String value = "热量 278 大卡   分解后的个数为：2";
    String values="营养参数为：胆固醇 146 毫克   分解后的个数为：2";
    for (int i = 0; i < values.length(); i++) {
      System.out.println(values.charAt(i) -0);
    }
    char c=160;
    System.out.println("c"+c+"d");
  }

  /*public static Node compute(String bef, String mid) {
    if (bef.isEmpty()) return null;
    int num = 0;
    if (bef.length() == 1) return new Node(bef.charAt(0));
    Node node = new Node(bef.charAt(0));
    for (int i = 0; i < mid.length(); i++) {
      if (mid.charAt(i) == bef.charAt(0)) {
        num = i;
        break;
      }
    }
    if (num != 0) node.left = compute(bef.substring(1, num + 1), mid.substring(0, num));
    if (num < bef.length()) node.right = compute(bef.substring(num + 1), mid.substring(num + 1));
    return node;
  }

  public static void scan(Node node) {
    if (node == null) return;
    scan(node.left);
    scan(node.right);
    System.out.print(node.value);
  }*/

  /*  String before = "ABDHIEJCFKG";
  String mid = "HDIBEJAFKFG";
  System.out.println(before.substring(0, 2) + "   " + before.substring(before.length() - 1));
  scan(compute(before, mid));
  Stack<Integer> stack = new Stack();
  stack.push(1);
  // stack.pop();
  System.out.println(stack.peek());*/

  /* Map<String, Integer> map = new HashMap<>();
   */
  /*DecimalFormat decimalFormat = new DecimalFormat("0.00");
  float d = Float.parseFloat(decimalFormat.format((float) 13 / 100));
  System.out.println(d);*/
  /*

  String a = "123";
  System.out.println(a.hashCode());*/
  /* Field field = String.class.getDeclaredField("value");
  field.setAccessible(true);
  char[] value = (char[]) field.get(a);
  value[0] = '2';
  System.out.println(a);*/

  //   System.out.println(a.hashCode());

  /*String values =
      "热量 278 大卡/钾 230 毫克/胆固醇 146 毫克/磷 135 毫克/钠 62.6 毫克/脂肪 23.1 克/蛋白质 16.7 克/镁 14 毫克/钙 14 毫克/硒 11.05 微克/维生素A 5 微克/烟酸 4.5 毫克/锌 3.36 毫克/铁 1.4 毫克/碳水化合物 0.7 克/维生素B1 0.3 毫克/铜 0.17 毫克/维生素B2 0.16 毫克/维生素E 0.11 毫克/锰 0.02 毫克/维生素B6 0/维生素B12 0/叶酸 0/膳食纤维 0/维生素C 0/胡萝卜素 0/碘 0/";

  String m = values.replaceAll("\\u00A0", "&");
  System.out.println(m);*/

  /*

      Map<String, String> map = new LinkedHashMap<>();
      map.put("yi ", "yi");
      map.put("er", "er ");
      map.put("three", "three");
      map.put("sdfkljsl", "fsafsdf");
      map.put("fsafdser", "fdsafsdf");
      map.put("three", "on");
      Iterator iterator = map.keySet().iterator();
      while (iterator.hasNext()) {
        String key = iterator.next().toString();
        System.out.println("key :" + key + "  value:" + map.get(key));
      }
  */

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
