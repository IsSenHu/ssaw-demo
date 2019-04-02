package com.ssaw.smscenter;

import com.ssaw.rocketmq.annotation.EnableRocketMq;
import com.ssaw.rocketmq.annotation.RocketMqProducerComponentScan;
import com.ssaw.smscenter.producer.ProducerBasicPackagesClass;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author hszyp
 */
@EnableRocketMq
@EnableScheduling
@SpringBootApplication
@RocketMqProducerComponentScan(basicPackagesClass = ProducerBasicPackagesClass.class)
public class SmsCenterApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SmsCenterApplication.class, args);
    }
}

