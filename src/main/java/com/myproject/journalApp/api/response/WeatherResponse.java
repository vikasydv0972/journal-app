package com.myproject.journalApp.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class WeatherResponse {

    private Current current;

    @Getter
    @Setter
    public class Current {
        @JsonProperty("observation_time")
        public String observationTime;
        public int temperature;
        @JsonProperty("weather_descriptions")
        private List<String> weatherDescription=new ArrayList<>();

        @JsonProperty("feelslike")
        private int feelLike;


    }
}
