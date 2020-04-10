package com.application.controller;

import com.application.dao.FoodDao;
import com.application.entity.client.FoodClient;
import com.application.entity.client.FoodItemClient;
import com.application.entity.server.Food;
import com.application.service.FoodService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author PG
 */
@Controller
public class FoodController {

    @Resource
    private FoodService foodService;

    /**
     * 获取所有的一级食物分类
     */
    @PostMapping("/getAllFirstKindFood")
    @ResponseBody
    private List<String> getAllFirstKindFood() {
        return foodService.getAllFirstKindFood();
    }

    /**
     * 根据一级分类获取二级分类
     */
    @PostMapping("/getSecondKindByFirstKind")
    @ResponseBody
    private List<String> getSecondKindByFirstKind(HttpServletRequest request) {
        String firstKindFood = request.getParameter("firstKindFood");
        return foodService.getSecondKindByFirstKind(firstKindFood);
    }

    /**
     * 根据二级分类获取相应的食物
     */
    @PostMapping("/getFoodsBySecondKind")
    @ResponseBody
    private List<FoodItemClient> getFoodsBySecondKind(HttpServletRequest request) {
        String secondKind = request.getParameter("secondKind");
        return foodService.getFoodsBySecondKind(secondKind);
    }

    /**
     * 根据名称获取具体的食物信息
     */
    @PostMapping("/getFoodByName")
    @ResponseBody
    private FoodClient getFoodByName(HttpServletRequest request) {
        String foodName = request.getParameter("foodName");
        return foodService.getFoodByName(foodName);
    }
}
