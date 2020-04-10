package com.application;

import com.application.dao.*;
import com.application.entity.server.*;
import com.application.util.DownPicUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

/** @author PG */
@Controller
public class ClassifyReptile {

  @Resource private FoodDao foodDao;

  @Resource private RecipeDao recipeDao;

  @Resource private RecipeKindDao recipeKindDao;

  @Resource private HealthDietDao healthDietDao;
  @Resource private FunctionalBenefitRecipesDao benefitRecipesDao;
  @Resource private HealthDietRecipesDao dietRecipesDao;

  @Resource private FunctionalBenefitDao functionalBenefitDao;
  @Resource private RecipeKindRecipesDao kindRecipesDao;

  @RequestMapping("searchPeople")
  @ResponseBody
  private String searchPeople() throws Exception {
    List<String> urls = new ArrayList<>();
    urls.add("https://www.meishij.net/yaoshanshiliao/renqunshanshi/");
    //urls.add("https://www.meishij.net/yaoshanshiliao/jibingtiaoli/");
    //urls.add("https://www.meishij.net/yaoshanshiliao/gongnengxing/");
    //urls.add("https://www.meishij.net/yaoshanshiliao/zangfu");
    List<String> firstKinds = new ArrayList<>();
    firstKinds.add("人群膳食");
   // firstKinds.add("疾病调理");
    //firstKinds.add("功能性调理");
    //firstKinds.add("脏腑调理");
    for (int i = 0; i < urls.size(); i++) {
      String URL = urls.get(i);
      String firstKindName = firstKinds.get(i);
      // System.out.println("链接为：" + URL + "   类别为：" + firstKindName);
      getSearch(URL, firstKindName);
    }

    return "测试成功";
  }

  private void getSearch(String URL, String kind) throws Exception {

    // System.out.println("开始进行搜索");
    Document document = Jsoup.connect(URL).get();
    Elements elements = document.getElementsByClass("listnav_dl_style1 w990 clearfix");
    Integer m = healthDietDao.getNum();
    int dietNum = m == null ? 1000 : m + 1;

    for (Element element : elements.select("dd")) {
      String secondKindName = element.text();
      String classUrl = element.select("a").attr("href");
      // System.out.println("类型名称：" + classifyName + "  链接：" + classUrl);
      Document document1 = Jsoup.connect(classUrl).get();
     /* String dietMethod = document1.getElementsByClass("p1").text();
      //   System.out.println(dietMethod);
      String suitableFood = document1.getElementsByClass("p2").get(0).text();
      // System.out.println("适宜的食物" + suitableFood);
      String suitableFoods =
          document1
              .getElementsByClass("slys_con")
              .get(0)
              .getElementsByClass("clearfix")
              .get(0)
              .text();
      // System.out.println("具体食物为：" + suitableFoods);
      String tabooFood = document1.getElementsByClass("p2").get(1).text();
      // System.out.println("禁忌的食物为：" + tabooFood);
      String tabooFoods =
          document1
              .getElementsByClass("slys_con")
              .get(0)
              .getElementsByClass("clearfix")
              .get(1)
              .text();
      //  System.out.println("具体为：" + tabooFoods);
      HealthDiet healthDiet = healthDietDao.getByName(secondKindName);
      *//* if (healthDiet == null) {
        healthDiet =
            new HealthDiet(
                dietNum,
                kind,
                secondKindName,
                dietMethod,
                suitableFood,
                suitableFoods,
                tabooFood,
                tabooFoods);
        healthDietDao.saveAndFlush(healthDiet);
      }*/
      dietNum++;

      // Set<Recipe> recipeList = healthDiet == null ? new HashSet<>() : healthDiet.getRecipes();
      /** 修改食谱的类别 需要修改menu表和menuKind表 */
      Elements elements1 = document1.getElementsByClass("paixu");
      String url = HomePageReptile.DEFAULT_URL + elements1.get(0).select("a").get(1).attr("href");
      Document document2 = Jsoup.connect(url).get();
      Elements elements2 = document2.getElementsByClass("listtyle1_page_w").select("a");
      saveRecipe(secondKindName, url);
      System.out.println("类别为：" + secondKindName);
      if (elements2.size() == 2) {
        String url2 = HomePageReptile.DEFAULT_URL + elements2.get(1).attr("href");
        saveRecipe(secondKindName, url2);
      }
      if (elements2.size() == 3) {
        String url2 = HomePageReptile.DEFAULT_URL + elements2.get(1).attr("href");
        saveRecipe(secondKindName, url2);
        String url3 = HomePageReptile.DEFAULT_URL + elements2.get(2).attr("href");
        saveRecipe(secondKindName, url3);
      }
      if (elements2.size() > 3) {
        String url2 = HomePageReptile.DEFAULT_URL + elements2.get(1).attr("href");
        saveRecipe(secondKindName, url2);
        String url3 = HomePageReptile.DEFAULT_URL + elements2.get(2).attr("href");
        saveRecipe(secondKindName, url3);
        String url4 = HomePageReptile.DEFAULT_URL + elements2.get(3).attr("href");
        saveRecipe(secondKindName, url4);
      }
    }
  }

  public void saveRecipe(String kind, String URL) throws Exception {

    Document document = Jsoup.connect(URL).get();
    Elements elements = document.getElementsByClass("listtyle1");
    // System.out.println("kind:" + kind + "   食谱个数为：" + elements.size());
    String savePath = "C:\\Users\\PG\\Desktop\\HealthRecipe\\recipe";
    // Long n = recipeDao.getMaxRecipeNum();
    // long recipeNum = n == null ? 10000 : n + 1;
    for (int j = 0; j < elements.size() - 4; j++) {
      Element element = elements.get(j);
      String url = element.getElementsByClass("big").attr("href");
      Document document1 = Jsoup.connect(url).get();
      String recipeImage = document1.getElementsByClass("cp_headerimg_w").select("img").attr("src");
      // System.out.println(document1.getElementsByClass("cp_headerimg_w").size() + " " + imageSrc);
      String recipeName = document1.getElementsByClass("title").text();
      System.out.println("菜单名为：" + recipeName);
      Set<String> kinds = new HashSet<>();
      Long recipeNum = recipeDao.getNumByName(recipeName);
      if (recipeNum == null) continue;
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
        System.out.println(kindName);
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
      /* String functionBenefits = document1.getElementsByClass("yj_tags clearfix").text();
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
            methods.append("无图片||");
            i++;
          }
          status = 0;
          pic = 1;
          String method = element1.text();
          */
      /*if (method.charAt(0) - '0' >= 0 || method.charAt(0) - '0' <= 9) {
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
      /*  Random random = new Random();
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
            try {
              DownPicUtils.downFile(recipeImage, savePath, fileName);
            } catch (Exception e) {
            }
      */
      // 保存种类和食谱的关系
      /* int recipeKindNum;
      if (recipeKindDao.getNumByName(kind) == null) {
        Integer kindNum = recipeKindDao.getNum();
        RecipeKind recipeKind = new RecipeKind(kindNum == null ? 1000 : kindNum + 1, kind);
        recipeKindDao.save(recipeKind);
        recipeKindNum = recipeKindDao.getNum() + 1;
      } else {
        recipeKindNum = recipeKindDao.getNumByName(kind);
      }
      RecipeKindRecipes kindRecipes = new RecipeKindRecipes(recipeKindNum, recipeNum);
      kindRecipesDao.save(kindRecipes);*/

      // 保存健康饮食与食谱的关系
      /* int healthDietNumber = healthDietDao.getNumberByName(kind);
      HealthDietRecipes dietRecipes = new HealthDietRecipes(recipeNum, healthDietNumber);
      dietRecipesDao.save(dietRecipes);

      // functionalbenefit与recipe的关系
      for (FunctionalBenefit benefit : benefits) {
        FunctionalBenefitRecipes benefitRecipes = new FunctionalBenefitRecipes();
        benefitRecipes.setFunBenefitNum(benefit.getBenefitNum());
        benefitRecipes.setRecipeNum(recipeNum);
        benefitRecipesDao.save(benefitRecipes);
      }
      System.out.println();*/
      recipeNum++;
    }
  }

  public Recipe saveMenu(String URL, String menuKind) throws Exception {
    String savePath = "C:\\Users\\PG\\Desktop\\picture";
    Long n = recipeDao.getMaxRecipeNum();
    long num = n == null ? 10000 : n + 1;
    Document document1 = Jsoup.connect(URL).get();
    String imageSrc = document1.getElementsByClass("cp_headerimg_w").select("img").attr("src");
    // System.out.println(document1.getElementsByClass("cp_headerimg_w").size() + " " + imageSrc);

    String menuName = document1.getElementsByClass("title").text();
    // System.out.println(name);
    String functionBenefits = document1.getElementsByClass("yj_tags clearfix").text();
    List<FunctionalBenefit> benefits = new ArrayList<>();
    for (String value : functionBenefits.split(" ")) {
      FunctionalBenefit benefit = new FunctionalBenefit();
      if (!value.isEmpty() && functionalBenefitDao.getByName(value) == null)
        functionalBenefitDao.save(benefit);
      else benefit = functionalBenefitDao.getByName(value);
      benefits.add(benefit);
    }
    // System.out.println(document1.getElementsByClass("yj_tags clearfix").size());
    // 主料
    StringBuilder mainIngredient = new StringBuilder();
    for (Element e : document1.getElementsByClass("yl zl clearfix")) {
      for (Element element1 : e.getElementsByClass("clearfix").select("li")) {
        mainIngredient.append(element1.select("a").text());
        mainIngredient.append("/");
        mainIngredient.append(element1.select("span").text());
        mainIngredient.append("|");
        // System.out.println(element1.select("a").text() + "含量：" + element1.select("span").text());
      }
    }
    // 辅料
    StringBuilder accessories = new StringBuilder();
    for (Element element1 : document1.getElementsByClass("yl fuliao clearfix")) {
      for (Element e : element1.getElementsByClass("clearfix").select("li")) {
        //  accessories.put(e.select("a").text(), e.select("span").text());
        accessories.append(e.select("a").text());
        accessories.append("/");
        accessories.append(e.select("span").text());
        accessories.append("|");
      }
    }
    //  System.out.println(accessories.toString());

    StringBuilder methods = new StringBuilder();
    int i = 1;
    String className1 = "editnew edit";
    String className2 = "edit edit_class_0 edit_class_13";
    String className3 = "edit edit_class_158 edit_class_197";
    String className4 = "edit edit_class_0 edit_class_2";
    Elements elements = document1.getElementsByClass("measure");
    if (document1.getElementsByClass(className1).size() != 0) {
      elements = document1.getElementsByClass(className1);
    } else if (document1.getElementsByClass(className2).size() != 0) {
      elements = document1.getElementsByClass(className2);
    } else if (document1.getElementsByClass(className3).size() != 0) {
      elements = document1.getElementsByClass(className3);
    } else if (document1.getElementsByClass(className4).size() != 0) {
      elements = document1.getElementsByClass(className4);
    }

    if (elements.size() == 0) System.out.println("元素个数为0的页面为：" + URL);
    // System.out.println("元素个数为：" + elements.select("p").size());
    for (Element element1 : elements.select("p")) {
      if (!element1.text().isEmpty()) {
        String method = element1.text();
        methods.append(method);
        methods.append("/");
        // System.out.println(method);
      } else {
        String path = savePath + "\\measure\\" + num;
        String imageResource = element1.select("img").attr("src");
        // System.out.println(method + " " + path + " " + imageResource);
        String picName = String.valueOf(num) + "_" + i + ".png";
        methods.append(path + "\\" + picName);
        methods.append("|");
        // System.out.println("图片 path " + picName);
        try {
          DownPicUtils.downFile(imageResource, path, picName);
        } catch (Exception e) {
        }
      }
      i++;
    }
    System.out.println("制作方法为：" + methods.toString());
    System.out.println("食谱为：" + menuName);
    String fileName = "recipe" + num + ".png";
    Recipe recipe = recipeDao.getByNum(Long.valueOf(menuName));
    if (recipe == null) {
      //   recipe = new Recipe(num, menuKind, menuName, savePath + "\\" + fileName, benefits, "",
      // mainIngredient.toString(), accessories.toString(), "", methods.toString());
      recipeDao.saveAndFlush(recipe);
    } else {
      fileName = "recipe" + recipe.getRecipeNum() + ".png";
    }
    try {
      DownPicUtils.downFile(imageSrc, savePath, fileName);
    } catch (Exception e) {
    }
    return recipe;

    // num++;
  }
}

           /* for (Element menuElement : document1.getElementsByClass("listtyle1_list clearfix").select("a")) {
           String menuName = menuElement.attr("title");
           Recipe recipe = recipeDao.getByName(menuName);
           String menuSrc = menuElement.attr("href");
           Document document2 = Jsoup.connect(menuSrc).get();
           Elements menuNames = document2.getElementsByClass("pathstlye1").select("li");
           String menuKinds = menuNames.get(menuNames.size() - 1).text();
           //    System.out.println("菜谱的种类为：" + menuKinds.split("#").length);
           StringBuilder menuKind = new StringBuilder();
           String[] values = menuKinds.split("#");
           for (int i = 1; i < values.length; i++) {
               if (!values[i].isEmpty()) {
                   menuKind.append(values[i]);
               } else {
                   menuKind.append(",");
               }
           }

           if (recipe == null) {
               recipe = saveMenu(menuSrc, menuKinds);
               //recipe = recipeDao.getByName(menuName);
               if (recipe == null) {
                   System.out.println("食谱为空，食谱名为：" + menuName + " 链接为：" + menuSrc);
               }
               continue;
           }
           System.out.println("菜单名为：" + recipe.getRecipeName());
           //    System.out.println("食谱名称为：" + menuName + ",类型为：" + menuKinds);
           recipe.setKind(menuKinds);
           if (!recipe.getKind().equals(menuKinds)) {
               recipe.setKind(menuKinds);
               recipeDao.saveAndFlush(recipe);
           }
           if (!recipeList.contains(recipe))
               recipeList.add(recipe);
           Long n = recipeKindDao.getNumByName();
           long number = n == null ? 1000 : n + 1;
           for (int i = 1; i < values.length; i++) {
               if (!values[i].equals(" ")) {
                   //  System.out.println(values[i]);
                   RecipeKind kind1 = recipeKindDao.(values[i]);
                   if (kind1 == null) {
                       // System.out.println("kind1 weikong");
                       Set<Recipe> recipes = new HashSet<>();
                       recipes.add(recipe);
                       recipeKindDao.save(new RecipeKind(number, values[i], recipes));
                       number++;
                   } else {
                       Set<Recipe> recipes = kind1.getRecipes();

                       if (!recipes.contains(recipe)) {
                           recipes.add(recipe);
                           kind1.setRecipes(recipes);
                           System.out.println("菜单类型" + kind1.getKindName() + "新加入的菜单id为：" + recipe.getId() + "   更新，原先菜单类型的菜单个数为" + (recipes.size() - 1));
                           Iterator iterator = recipes.iterator();
                           while (iterator.hasNext()) {
                               System.out.print(iterator.next() + "  ");
                           }
                           recipeKindDao.saveAndFlush(kind1);
                       }
                   }*/

            /*@RequestMapping("/searchFood")
            @ResponseBody*/
  /* public String searchFood(@PathParam("URL") String URL, @PathParam("kind") String kind) throws Exception {

          String firstKind = kind;
          Document document = Jsoup.connect(URL).get();
          Elements elements = document.getElementsByClass("category_sub clear");
          long number = foodDao.getLastFood().getNumber() + 1;
          for (Element element : elements) {
              String secondKind = element.select("h3").text();
              System.out.println("secondKind:" + element.select("h3").text() + "  " + element.select("li").size());
              for (Element e : element.select("li")) {
                  String foodName = e.text();
                  if (foodDao.getNameByName(foodName) == null) {
                      String foodSrc = e.select("a").attr("href");
                      String foodPicSrc = "";
                      //System.out.println("name:" + name + " src:" + foodSrc);
                      try {
                          Document document1 = Jsoup.connect(foodSrc).get();
                          String picSrc = document1.getElementsByClass("collect_dp").select("img").attr("data-src");
                          // System.out.println("picSrc" + picSrc);
                          String health = document1.getElementsByClass("collect_dp").select("h1").text();
                          Elements elements1 = document1.getElementsByClass("ui_title_wrap clear ").select("h2");
                          String healthSrc = elements1.get(elements1.size() - 1).select("a").attr("href");
                          // System.out.println("name:" + health + " src:" + healthSrc);
                          Document document2 = Jsoup.connect(healthSrc).get();
                          Elements elements2 = document2.getElementsByClass("category_usebox mt10 clear").select("p");
                          String introduction = elements2.get(1).text();
                          String healthWorth = elements2.get(3).text();
                          String benefit = elements2.get(5).text();
                          String suitablePeople = elements2.get(7).text();
                          String tabooPeople = elements2.get(9).text();
                          //System.out.println(elements2.size());
                          StringBuilder nutritionalIngredient = new StringBuilder();
                          //    System.out.println(document2.getElementsByClass("category_use_table mt10 clear"));
                          for (Element element3 : document2.getElementsByClass("category_use_table mt10 clear").select("li")) {
                              // System.out.println(element3.text());
                              nutritionalIngredient.append(element3.text() + "/");
                          }
                          //  System.out.println(nutritionalIngredient.toString());
                          String savePath = "C:\\Users\\PG\\Desktop\\picture\\food";
                          String fileName = "" + number + ".png";
                          DownPicUtils.downFile(picSrc, savePath, fileName);
                          String picPath = savePath + "\\" + fileName;
                          Food food = new Food(number, foodName, picPath, firstKind, secondKind, healthWorth, introduction, benefit, suitablePeople, tabooPeople, nutritionalIngredient.toString());
                          foodDao.save(food);
                          number++;
                      } catch (Exception e1) {
                          System.out.println("链接出现异常");
                      }
                  }
              }
          }
          System.out.println(elements.size());
          return "获取成功";
      }
  */
