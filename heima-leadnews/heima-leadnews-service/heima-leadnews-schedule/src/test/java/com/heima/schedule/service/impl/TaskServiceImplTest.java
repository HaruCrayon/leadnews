package com.heima.schedule.service.impl;

import com.heima.model.schedule.dtos.Task;
import com.heima.schedule.ScheduleApplication;
import com.heima.schedule.service.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @author LiJing
 * @version 1.0
 */
@SpringBootTest(classes = ScheduleApplication.class)
@RunWith(SpringRunner.class)
public class TaskServiceImplTest {
    @Autowired
    private TaskService taskService;

    @Test
    public void addTask() {
        for (int i = 1; i <= 5; i++) {
            Task task = new Task();
            task.setTaskType(100 + i);
            task.setPriority(50);
            task.setParameters("task test".getBytes());
            task.setExecuteTime(new Date().getTime() + 5000 * i);

            long taskId = taskService.addTask(task);
//            System.out.println(taskId);
        }

    }

    @Test
    public void cancelTask() {
        boolean result = taskService.cancelTask(1689731483765362689L);
        System.out.println("result=" + result);
    }

    @Test
    public void testPull() {
        Task task = taskService.pull(100, 50);
        System.out.println(task);
    }
}