package ru.spbstu.stepanovgzh.portsimulator.config;

import org.springframework.context.annotation.*;
import ru.spbstu.stepanovgzh.portsimulator.common.Schedule;

@Configuration
@ComponentScan("ru.spbstu.stepanovgzh.portsimulator")
public class PortSimulatorContext
{
    @Bean
    @Scope("singleton")
    public Schedule schedule()
    {
        return Schedule.generateRandomSchedule();
    }
}
