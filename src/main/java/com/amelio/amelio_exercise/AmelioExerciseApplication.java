package com.amelio.amelio_exercise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AmelioExerciseApplication {

   public static void main(String[] args) {
      SpringApplication.run(AmelioExerciseApplication.class, args);
   }

}
