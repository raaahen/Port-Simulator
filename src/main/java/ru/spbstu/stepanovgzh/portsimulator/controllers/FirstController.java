package ru.spbstu.stepanovgzh.portsimulator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.spbstu.stepanovgzh.portsimulator.common.Schedule;

@Controller
@RequestMapping("/first")
public class FirstController
{
    private final Schedule randomSchedule;

    @Autowired
    public FirstController(Schedule randomSchedule)
    {
        this.randomSchedule = randomSchedule;
    }

    @GetMapping("/random_schedule")
    @ResponseBody
    public String getRandomSchedule()
    {
        return randomSchedule.toString();
    }
}
