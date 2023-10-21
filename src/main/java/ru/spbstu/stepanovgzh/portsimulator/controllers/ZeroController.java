package ru.spbstu.stepanovgzh.portsimulator.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ZeroController
{
    @GetMapping("/")
    public String mainView()
    {
        return "main_view";
    }
}
