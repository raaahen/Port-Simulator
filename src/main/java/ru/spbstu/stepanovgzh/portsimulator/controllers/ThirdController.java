package ru.spbstu.stepanovgzh.portsimulator.controllers;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.spbstu.stepanovgzh.portsimulator.common.*;

@Controller
@RequestMapping("/third")
public class ThirdController
{
    private final SecondController sC;

    @Autowired
    public ThirdController(SecondController sC)
    {
        this.sC = sC;
    }

    @GetMapping("/port_simulation")
    @ResponseBody
    public String simulatePort()
    {
        String jsonSchedule = sC.getJson();
        var gson = new Gson();
        Schedule schedule = gson.fromJson(jsonSchedule, Schedule.class);
        Port port = Port.simulateWorkingAndGetData(schedule, new CranePool());
        Statistic statistic = new Statistic(port);

        return sC.postStatistic(schedule, statistic);
    }
}
