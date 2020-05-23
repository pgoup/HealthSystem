package com.application.schedule;

import com.application.service.RecipeClientService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Timer;
import java.util.TimerTask;

/** @author PG */
@Service
public class UpdateDataScheduleImpl implements UpdateDataSchedule {
  @Resource private RecipeClientService recipeClientService;

  private static final long PERIOD = 10 * 60 * 1000;
  @Override
  public void updateData() {
    Timer timer = new Timer(true);
    timer.schedule(
        new TimerTask() {
          @Override
          public void run() {
            recipeClientService.updateData();
          }
        },
        0,
        PERIOD);
  }
}
