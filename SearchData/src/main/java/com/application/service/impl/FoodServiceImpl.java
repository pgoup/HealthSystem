package com.application.service.impl;

import com.application.dao.FoodDao;
import com.application.entity.client.FoodClient;
import com.application.entity.client.FoodItemClient;
import com.application.service.FoodService;
import com.application.util.ConvertUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author PG
 */
@Service
public class FoodServiceImpl implements FoodService {
    @Resource
    private FoodDao foodDao;

    /**
     * 获取所有的一级食物分类
     */
    @Override
    public List<String> getAllFirstKindFood() {
        return foodDao.getAllFirstKindFood();
    }

    /**
     * 根据一级分类获取二级分类
     */
    @Override
    public List<String> getSecondKindByFirstKind(String firstKindFood) {
        return foodDao.getSecondKindByFirstKind(firstKindFood);
    }

    /**
     * 根据二级分类获取相应的食物
     * 只获取食物的名称和图片
     */
    @Override
    public List<FoodItemClient> getFoodsBySecondKind(String secondKind) {
        List<Object[]> objects = foodDao.getFoodsBySecondKind(secondKind);
        List<FoodItemClient> clients = new ArrayList<>();
        for (Object[] o : objects) {
            clients.add(new FoodItemClient(o[0].toString(), ConvertUtils.picConvertToByte(o[1].toString())));
        }
        return clients;
    }

    /**
     * 根据名称获取具体的食物信息
     */
    @Override
    public FoodClient getFoodByName(String name) {
        Object[] o = foodDao.getFoodByName(name).get(0);
        return new FoodClient(o[0].toString(), ConvertUtils.picConvertToByte(o[1].toString()), o[2].toString(), o[3].toString(), o[4].toString(), o[5].toString(), o[6].toString(), o[7].toString());
    }
}
