package ru.spbstu.stepanovgzh.portsimulator.controllers;

import com.google.gson.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.spbstu.stepanovgzh.portsimulator.common.*;

import java.io.*;
import java.nio.file.*;

@Controller
@RequestMapping("/second")
public class SecondController
{
    public final Path DIR_PATH_FOR_STATISTIC = Paths.get("M:\\Java\\PortSimulator\\JSONs\\statistic");
    public final Path DIR_PATH_FOR_SCHEDULE = Paths.get("M:\\Java\\PortSimulator\\JSONs");
    private final Schedule randomSchedule;

    @Autowired
    public SecondController(Schedule randomSchedule)
    {
        this.randomSchedule = randomSchedule;
    }

    @GetMapping("/create_json")
    @ResponseBody
    public String getJson()
    {
        try (OutputStream out = Files.newOutputStream(Paths.get(
                DIR_PATH_FOR_SCHEDULE + "\\" + RandomStringUtils.random(10, true, true) + ".json")))
        {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonRandomSchedule = gson.toJson(randomSchedule);
            out.write(jsonRandomSchedule.getBytes());
            return jsonRandomSchedule;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return "Something went wrong";
        }
    }

    @GetMapping("/get_schedule")
    @ResponseBody
    public String getSchedule(String jsonName)
    {
        try
        {
            var gson = new Gson();
            FileReader fileReader = new FileReader(DIR_PATH_FOR_SCHEDULE + "\\" + jsonName);
            Schedule schedule = gson.fromJson(fileReader, Schedule.class);
            return schedule.toString();
        }
        catch (IOException e)
        {
            return "There is no such file";
        }
    }

    @PostMapping()
    @ResponseBody
    public String postStatistic(Schedule schedule, Statistic statistic)
    {
        String scheduleJsonPath =
                DIR_PATH_FOR_STATISTIC + "\\" + RandomStringUtils.random(10, true, true) + ".json";
        String statisticJsonPath =
                DIR_PATH_FOR_STATISTIC + "\\" + RandomStringUtils.random(10, true, true) + ".json";

        try (OutputStream scheduleOut = Files.newOutputStream(Paths.get(scheduleJsonPath));
        OutputStream statisticOut = Files.newOutputStream(Paths.get(statisticJsonPath)))
        {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(schedule);
            scheduleOut.write(json.getBytes());
            json = gson.toJson(statistic);
            statisticOut.write(json.getBytes());
            return "The results of the third service have been successfully saved";
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return "Something went wrong!";
        }
    }
}
