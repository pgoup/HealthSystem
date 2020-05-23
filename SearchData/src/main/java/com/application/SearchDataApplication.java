package com.application;

import com.application.schedule.UpdateDataSchedule;
import com.application.schedule.UpdateDataScheduleImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/** @author PG */
@SpringBootApplication
@Controller
public class SearchDataApplication {
  @Autowired private UpdateDataSchedule updateDataSchedule;
  private static UpdateDataSchedule updateData;
  @PostConstruct
  public void init() {
    updateData = this.updateDataSchedule;
  }
  public static void main(String[] args) {
    SpringApplication.run(SearchDataApplication.class, args);
    updateData.updateData();
  }
}
