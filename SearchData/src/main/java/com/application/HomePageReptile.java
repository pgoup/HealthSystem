package com.application;

import com.application.dao.*;
import com.application.entity.server.*;
import com.application.util.DownPicUtils;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.deploy.security.DeploySSLCertStore;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;

/** @author PG */
@Controller
public class HomePageReptile {
  public static final String DEFAULT_URL = "https://www.meishij.net";

  @Resource private RecipeDao recipeDao;

  @Resource private FunctionalBenefitDao functionalBenefitDao;

  @Resource private HealthDietDao healthDietDao;

  @Resource private RecipeKindDao recipeKindDao;
  @Resource private RecipeKindRecipesDao kindRecipesDao;
  @Resource private FunctionalBenefitRecipesDao benefitRecipesDao;

  @RequestMapping("/search")
  @ResponseBody()
  public String search() throws Exception {
    String URL = "https://www.meishij.net/chufang/diy/";
    System.out.println("进入控制端");
    Connection connection = Jsoup.connect(URL);
    connection.timeout(30000);
    Document docu = connection.get();

    Elements elements = docu.getElementsByClass("listnav_con clearfix");
    for (Element element1 : elements) {
      // System.out.println(element1.select("a").size());
      for (Element element2 : element1.select("a")) {
        String kind = element2.text();
        String link = element2.select("a").attr("href");
        Connection connection1 = Jsoup.connect(link);
        connection1.timeout(30000);
        Document document = connection1.get();
        Elements elements1 = document.getElementsByClass("paixu");
        String url = DEFAULT_URL + elements1.get(0).select("a").get(1).attr("href");
        Document document2 = Jsoup.connect(url).get();
        Elements elements2 = document2.getElementsByClass("listtyle1_page_w").select("a");
        saveRecipe(kind, url);
        if (elements2.size() == 2) {
          String url2 = HomePageReptile.DEFAULT_URL + elements2.get(1).attr("href");
          saveRecipe(kind, url2);
        }
        if (elements2.size() == 3) {
          String url2 = HomePageReptile.DEFAULT_URL + elements2.get(1).attr("href");
          saveRecipe(kind, url2);
          String url3 = HomePageReptile.DEFAULT_URL + elements2.get(2).attr("href");
          saveRecipe(kind, url3);
        }
        if (elements2.size() > 3) {
          String url2 = HomePageReptile.DEFAULT_URL + elements2.get(1).attr("href");
          saveRecipe(kind, url2);
          String url3 = HomePageReptile.DEFAULT_URL + elements2.get(2).attr("href");
          saveRecipe(kind, url3);
          String url4 = HomePageReptile.DEFAULT_URL + elements2.get(3).attr("href");
          saveRecipe(kind, url4);
        }
        /* if (elements2.size() == 5) {
          String url2 = HomePageReptile.DEFAULT_URL + elements2.get(1).attr("href");
          saveRecipe(kind, url2);
          String url3 = HomePageReptile.DEFAULT_URL + elements2.get(2).attr("href");
          saveRecipe(kind, url3);
          String url4 = HomePageReptile.DEFAULT_URL + elements2.get(3).attr("href");
          saveRecipe(kind, url4);
          String url5 = HomePageReptile.DEFAULT_URL + elements2.get(4).attr("href");
          saveRecipe(kind, url5);
        }
        if (elements2.size() == 6) {
          String url2 = HomePageReptile.DEFAULT_URL + elements2.get(1).attr("href");
          saveRecipe(kind, url2);
          String url3 = HomePageReptile.DEFAULT_URL + elements2.get(2).attr("href");
          saveRecipe(kind, url3);
          String url4 = HomePageReptile.DEFAULT_URL + elements2.get(3).attr("href");
          saveRecipe(kind, url4);
          String url5 = HomePageReptile.DEFAULT_URL + elements2.get(4).attr("href");
          saveRecipe(kind, url5);
          String url6 = HomePageReptile.DEFAULT_URL + elements2.get(5).attr("href");
          saveRecipe(kind, url6);
        }
        if (elements2.size() == 7) {
          String url2 = HomePageReptile.DEFAULT_URL + elements2.get(1).attr("href");
          saveRecipe(kind, url2);
          String url3 = HomePageReptile.DEFAULT_URL + elements2.get(2).attr("href");
          saveRecipe(kind, url3);
          String url4 = HomePageReptile.DEFAULT_URL + elements2.get(3).attr("href");
          saveRecipe(kind, url4);
          String url5 = HomePageReptile.DEFAULT_URL + elements2.get(4).attr("href");
          saveRecipe(kind, url5);
          String url6 = HomePageReptile.DEFAULT_URL + elements2.get(5).attr("href");
          saveRecipe(kind, url6);
          String url7 = HomePageReptile.DEFAULT_URL + elements2.get(6).attr("href");
          saveRecipe(kind, url7);
        }
        if (elements2.size() > 7) {
          String url2 = HomePageReptile.DEFAULT_URL + elements2.get(1).attr("href");
          saveRecipe(kind, url2);
          String url3 = HomePageReptile.DEFAULT_URL + elements2.get(2).attr("href");
          saveRecipe(kind, url3);
          String url4 = HomePageReptile.DEFAULT_URL + elements2.get(3).attr("href");
          saveRecipe(kind, url4);
          String url5 = HomePageReptile.DEFAULT_URL + elements2.get(4).attr("href");
          saveRecipe(kind, url5);
          String url6 = HomePageReptile.DEFAULT_URL + elements2.get(5).attr("href");
          saveRecipe(kind, url6);
          String url7 = HomePageReptile.DEFAULT_URL + elements2.get(6).attr("href");
          saveRecipe(kind, url7);
          String url8 = HomePageReptile.DEFAULT_URL + elements2.get(7).attr("href");
          saveRecipe(kind, url8);
        }*/
      }
    }
    return URL;
  }

  private void getPaixu(String URL, String firstKind, String secondKind) throws Exception {
    /*getData(firstKind, secondKind, url);
    getData(firstKind, secondKind, url + "&page=2");
    getData(firstKind, secondKind, url + "&page=3");
    getData(firstKind, secondKind, url + "&page=4");*/
  }

  public void saveRecipe(String kind, String URL) throws Exception {
    Connection connection = Jsoup.connect(URL);
    connection.timeout(30000);
    Document document = connection.get();
    Elements elements = document.getElementsByClass("listtyle1");
    // System.out.println("kind:" + kind + "   食谱个数为：" + elements.size());
    String savePath = "C:\\Users\\PG\\Desktop\\HealthRecipe\\recipe";
    /* Long n = recipeDao.getMaxRecipeNum();
    long recipeNum = n == null ? 10000 : n + 1;*/
    for (int j = 0; j < elements.size() - 4; j++) {
      Set<String> kinds = new HashSet<>();
      // kinds.add(kind);
      Element element = elements.get(j);
      String url = element.getElementsByClass("big").attr("href");
      Connection connection1 = Jsoup.connect(url);
      connection1.timeout(30000);
      Document document1 = connection1.get();
      String recipeImage = document1.getElementsByClass("cp_headerimg_w").select("img").attr("src");
      // System.out.println(document1.getElementsByClass("cp_headerimg_w").size() + " " + imageSrc);
      String recipeName = document1.getElementsByClass("title").text();
      System.out.println("菜单名为：" + recipeName);
      Long recipeNum = recipeDao.getNumByName(recipeName);
      if(recipeNum==null)
        continue;
      Elements elements2 = document1.getElementsByClass("pathstlye1");
      for (Element e : elements2) {
        String kindNames = e.select("li").get(e.select("li").size() - 1).text();
        System.out.println("kindnames为：" + kindNames);
        String[] names = kindNames.split("#");
        for (String name : names) {
          System.out.println("name 为" + name);
          if (name.trim().length() != 0) kinds.add(name);
        }
      }
      Iterator iterator = kinds.iterator();
      System.out.println("类别个数为：" + kinds.size() + "为：");
      while (iterator.hasNext()) {
        String kindName = iterator.next().toString();
        int recipeKindNum;
        if (recipeKindDao.getNumByName(kindName) == null) {
          Integer kindNum = recipeKindDao.getNum();
          RecipeKind recipeKind = new RecipeKind(kindNum == null ? 1000 : kindNum + 1, kindName);
          recipeKindDao.save(recipeKind);
          recipeKindNum = recipeKindDao.getNum() + 1;
        } else {
          recipeKindNum = recipeKindDao.getNumByName(kindName);
        }
        RecipeKindRecipes kindRecipes = new RecipeKindRecipes(recipeKindNum, recipeNum);
        if (kindRecipesDao.getRecipeId(recipeNum, recipeKindNum) == null)
          kindRecipesDao.save(kindRecipes);
      }

      /*String functionBenefits = document1.getElementsByClass("yj_tags clearfix").text();
      System.out.println("好处为：" + functionBenefits);
      List<FunctionalBenefit> benefits = new ArrayList<>();
      if (!functionBenefits.isEmpty()) {
        for (String value : functionBenefits.split(" ")) {
          FunctionalBenefit benefit = new FunctionalBenefit();
          benefit.setBenefitName(value);
          if (!value.isEmpty() && functionalBenefitDao.getByName(value) == null) {
            Integer benefitNum = functionalBenefitDao.getNum();
            benefit.setBenefitNum(benefitNum == null ? 1000 : benefitNum + 1);
            functionalBenefitDao.save(benefit);
          } else {
            benefit.setBenefitNum(functionalBenefitDao.getNumByName(value));
          }
          benefits.add(benefit);
        }
      }

      // System.out.println(document1.getElementsByClass("yj_tags clearfix").size());
      // 主料
      StringBuilder mainIngredient = new StringBuilder();
      for (Element e : document1.getElementsByClass("yl zl clearfix")) {
        for (Element element1 : e.getElementsByClass("clearfix").select("li")) {
          String content = element1.select("a").text();
          String amount = element1.select("span").text();
          // System.out.println("主料为:" + content + "  含量为：" + amount);
          mainIngredient.append(element1.select("a").text());
          mainIngredient.append("&&");
          mainIngredient.append(element1.select("span").text());
          mainIngredient.append("##");
          // System.out.println(element1.select("a").text() + "含量：" +
          // element1.select("span").text());
        }
      }
      // 辅料
      StringBuilder accessories = new StringBuilder();
      for (Element element1 : document1.getElementsByClass("yl fuliao clearfix")) {
        for (Element e : element1.getElementsByClass("clearfix").select("li")) {
          String content = e.select("a").text();
          String amount = e.select("span").text();
          accessories.append(e.select("a").text());
          accessories.append("&&");
          accessories.append(e.select("span").text());
          accessories.append("##");
        }
      }
      //  System.out.println(accessories.toString());

      // 保存食谱方法
      StringBuilder methods = new StringBuilder();
      String className = "edit edit_class_0";
      String className1 = "edit edit_class_0 edit_class_13";
      String className2 = "editnew edit";
      String className3 = "edit edit_class_0 edit_class_2";
      Elements elements1 = document1.getElementsByClass(className);
      System.out.println("元素个数为：" + elements1.size());
      if (elements1.size() == 0) {
        elements1 = document1.getElementsByClass(className1);
        System.out.println("元素个数为：" + elements1.size());
      }
      if (elements1.size() == 0) {
        elements1 = document1.getElementsByClass(className2);
        System.out.println("元素个数为：" + elements1.size());
      }
      if (elements1.size() == 0) {
        elements1 = document1.getElementsByClass(className3);
        System.out.println("元素个数为：" + elements1.size());
      }

      int status = 0;
      int pic = 0;
      int i = 1;
      for (Element element1 : elements1.select("p")) {
        if (!element1.text().isEmpty()) {
          if (pic == 1) {
            methods.append("无图片##");
            i++;
          }
          status = 0;
          pic = 1;
          String method = element1.text();
          */
      /* if (method.charAt(0) - '0' >= 0 || method.charAt(0) - '0' <= 9) {
        char[] method1 = method.toCharArray();
        method1[1] = '、';
        method = String.valueOf(method1);
      }*/
      /*
                // System.out.println(method);
                methods.append(method);
                methods.append("&&");
              } else {
                String path = savePath + "\\measure\\" + recipeNum;
                String imageResource = element1.select("img").attr("src");
                // System.out.println(method + " " + path + " " + imageResource);
                String picName = String.valueOf(recipeNum) + "_" + i + ".png";
                methods.append(path + "\\" + picName);
                // methods.append("有图片");
                methods.append("##");
                // System.out.println("图片 path " + picName);

                new Thread(
                        new Runnable() {
                          @Override
                          public void run() {
                            try {
                              DownPicUtils.downFile(imageResource, path, picName);
                            } catch (Exception e) {
                            }
                          }
                        })
                    .start();
                i++;
                status++;
                pic = 0;
              }

              if (status >= 2) break;
            }

            System.out.println(methods.toString());

            Element element1 = document1.getElementById("tongji_author");
            String author = "健康食谱";
            if (element1 != null) author = element1.text();
      */
      // 获取食谱的收藏和观看次数
      /* String result = getAjaxRequest(url);
      Document document3 = Jsoup.parse(result);
      String viewClickNum = document3.getElementById("viewclicknum").text();
      System.out.println("点击数量为：" + viewClickNum);
      String collectNum = document3.getElementById("f_num").text();
      System.out.println("收藏的次数为：" + collectNum.substring(1, collectNum.length() - 1));
      System.out.println("作者为：" + author);*/

      // 保存食谱的图片
      /* Random random = new Random();
            int viewClickNum = random.nextInt(1000000000);
            int collectNum = random.nextInt(1000000);
            String fileName = "recipe" + recipeNum + ".png";
            Recipe menu =
                new Recipe(
                    recipeNum,
                    kind,
                    recipeName,
                    savePath + "\\" + fileName,
                    mainIngredient.toString(),
                    accessories.toString(),
                    methods.toString(),
                    // Integer.valueOf(viewClickNum == null ? "0" : viewClickNum),
                    viewClickNum,
                    collectNum,
                    /// Integer.valueOf(collectNum == null ? "0" : collectNum),
                    author);
            recipeDao.save(menu);
      */
      /*   new Thread(
                    new Runnable() {
                      @Override
                      public void run() {
                        try {
                          DownPicUtils.downFile(recipeImage, savePath, fileName);
                        } catch (Exception e) {
                          System.out.println("下载图片出现异常");
                        }
                      }
                    })
                .start();
      */

      // 保存种类和食谱的关系

      // 保存健康饮食与食谱的关系
      /*  int healthDietNumber = healthDietDao.getNumberByName(kind);
            HealthDietRecipes dietRecipes = new HealthDietRecipes(recipeNum, healthDietNumber);
      */
      // functionalbenefit与recipe的关系
      /*  for (FunctionalBenefit benefit : benefits) {
        FunctionalBenefitRecipes benefitRecipes = new FunctionalBenefitRecipes();
        benefitRecipes.setFunBenefitNum(benefit.getBenefitNum());
        benefitRecipes.setRecipeNum(recipeNum);
        benefitRecipesDao.save(benefitRecipes);
      }
      System.out.println();*/
      recipeNum++;
    }
  }

  private String getAjaxRequest(String url) throws Exception {
    WebClient webClient = new WebClient();
    webClient.getOptions().setJavaScriptEnabled(true); // 启用JS解释器，默认为true
    webClient.getOptions().setCssEnabled(true); // 禁用css支持
    webClient.getOptions().setThrowExceptionOnScriptError(false); // js运行错误时，是否抛出异常
    webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
    webClient.getOptions().setTimeout(10 * 1000); // 设置连接超时时间
    webClient.waitForBackgroundJavaScript(10000);

    WebRequest request = new WebRequest(new URL(url));
    request.setAdditionalHeader(
        "User-Agent",
        "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:62.0) Gecko/20100101 Firefox/62.0");
    request.setAdditionalHeader(
        "Cookie",
        "PLAY_LANG=cn; _plh=b9289d0a863a8fc9c79fb938f15372f7731d13fb; PLATFORM_SESSION=39034d07000717c664134556ad39869771aabc04-_ldi=520275&_lsh=8cf91cdbcbbb255adff5cba6061f561b642f5157&csrfToken=209f20c8473bc0518413c226f898ff79cd69c3ff-1539926671235-b853a6a63c77dd8fcc364a58&_lpt=%2Fcn%2Fvehicle_sales%2Fsearch&_lsi=1646321; _ga=GA1.2.2146952143.1539926675; _gid=GA1.2.1032787565.1539926675; _plh_notime=8cf91cdbcbbb255adff5cba6061f561b642f5157");
    String result = "";
    try {
      HtmlPage page = webClient.getPage(request);
      /*for (int i = 0; i < 20; i++) {
        if (page.getByXPath("//div[@class=‘c-gap-top c-recommend‘]").isEmpty()) break;
        synchronized (page) {
          page.wait(500);
        }
      }*/
      //  page.wait(10000);
      Thread.sleep(15000);
      result = page.asXml();

      // Thread.sleep(10000);

      // webClient.waitForBackgroundJavaScript(30 * 1000); // 等待js后台执行30秒

    } catch (Exception e) {
      System.out.println("爬取请求出现异常");
    }
    return result;
  }
}
