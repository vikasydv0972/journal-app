package com.myproject.journalApp.service;

import com.myproject.journalApp.api.response.WeatherResponse;
import com.myproject.journalApp.cache.AppCache;
import com.myproject.journalApp.constants.Placeholders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Component
public class WeatherService {
    //Use Values Annotations store api key in application.yml files and here pass
    @Value("${weather.api.key}")
    private String apiKey;

   // private static final String API="http://api.weatherstack.com/current?access_key=API_KEY&query=CITY";

    //it is class of spring-boot which is process http request process and give us response
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppCache appCache;

    @Autowired
    private  RedisService redisService;


    public WeatherResponse getWeather(String city){

        WeatherResponse weatherResponse = redisService.get("Weather_Of_" + city, WeatherResponse.class);
        if(weatherResponse!=null){
            return weatherResponse;
        }

        else {

            String finalAPI = appCache.appCache.get(AppCache.keys.WEATHER_API.toString()).replace(Placeholders.CITY, city).replace(Placeholders.API_KEY, apiKey);

            ResponseEntity<WeatherResponse> responce = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
            WeatherResponse body = responce.getBody();
            if(body!=null){
                redisService.set("weather_Of_"+city,body,300L);
            }
            return body;
        }

    }
}
