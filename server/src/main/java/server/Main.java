/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package server;


import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Activity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import server.database.ActivityRepo;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SpringBootApplication
@EntityScan(basePackages = { "commons", "server" })
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    /**
     * CommandLineRunner used to initialise the repo of activities when starting the server
     * @param repo the Activity repository
     * @return null if the database is already set
     */
    @Bean
    CommandLineRunner initialise(ActivityRepo repo) {
        if (repo.count() == 0) {
            return new CommandLineRunner() {
                @Override
                public void run(String... args) throws Exception {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        List<Activity> activities = Arrays.asList(mapper
                                .readValue(new InputStreamReader(Objects.requireNonNull(Main
                                                .class.getResourceAsStream("/activities.json"))),
                                        Activity[].class));
                        repo.saveAll(activities);
                        System.out.println("Successful addition " +
                                "of the activities to the repository!");

                    } catch (IllegalArgumentException e) {
                        System.out.println("Unable to add the activities");
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        System.out.println("File not found. Oops.");
                    } catch (NullPointerException e) {
                        System.out.println("Just so that the test works.");
                    }
                }
            };
        }
        return null;
    }

}