/*
import com.application.entity.server.FunctionalBenefit;
import com.application.dao.RecipeDao;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

*/
/**
 * @author PG
 *//*

public class SearchData {
    @Resource
    private RecipeDao recipeDao;

    @Test
    public void searchData() throws Exception {

        Document document = Jsoup.connect("https://www.meishij.net/chufang/diy/jiangchangcaipu/").get();
        Elements elements = document.getElementsByClass("listtyle1");
        String kind = "";
        System.out.println(elements.size());
        String savePath = "C:\\Users\\PG\\Desktop\\picture";
        long num = 10000;
        for (Element element : elements) {
            String url = element.getElementsByClass("big").attr("href");
            Document document1 = Jsoup.connect(url).get();
            String fileName = "menu" + num + ".png";
            String imageSrc = document1.getElementsByClass("cp_headerimg_w").select("img").attr("src");
            // System.out.println(document1.getElementsByClass("cp_headerimg_w").size() + " " + imageSrc);
            try {
                //downFile(imageSrc, savePath, fileName);
            } catch (Exception e) {
            }
            String menuName = document1.getElementsByClass("title").text();
            //System.out.println(name);
            String functionBenefits = document1.getElementsByClass("yj_tags clearfix").text();
            List<FunctionalBenefit> benefits = new ArrayList<>();
            for (String value : functionBenefits.split(" ")) {
                //benefits.add(new FunctionalBenefit(value));
            }
            //System.out.println(document1.getElementsByClass("yj_tags clearfix").size());
            //主料
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
            //辅料
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
            System.out.println(accessories.toString());

            StringBuilder methods = new StringBuilder();
            int i = 1;
            for (Element element1 : document1.getElementsByClass("content clearfix")) {
                String method = element1.select("p").text();
                String path = savePath + "\\measure\\" + num;
                String imageResource = element1.select("img").attr("src");
                // System.out.println(method + " " + path + " " + imageResource);
                String picName = String.valueOf(num) + "_" + i + ".png";
                methods.append(method);
                methods.append("/");
                methods.append(picName);
                methods.append("|");
                try {
                    // downFile(imageResource, path, picName);
                } catch (Exception e) {
                }
                i++;
            }
            //Recipe menu = new Recipe(num, menuName, savePath + "\\" + fileName, "家常菜", benefits, null, mainIngredient.toString(), accessories.toString(), methods.toString(), null);
            //menuDao.save(menu);
            num++;
        }
    }


    */
/**
     * 下载图片
     *//*

    private static void downFile(String url, String savePath, String file) throws Exception {
        URL url2 = new URL(url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url2.openConnection();
        InputStream inputStream = httpURLConnection.getInputStream();
        File file1 = new File(savePath);
        if (!file1.exists())
            file1.mkdir();
        String saveFilePath = savePath + File.separator + file;
        FileOutputStream outputStream = new FileOutputStream(saveFilePath);
        int byteRead = -1;
        byte[] buffer = new byte[4096];
        while ((byteRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, byteRead);
        }
        outputStream.close();
        inputStream.close();
    }
}


*/
