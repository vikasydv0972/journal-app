package com.myproject.journalApp.cache;

import com.myproject.journalApp.entity.ConfigJournalAppEntity;
import com.myproject.journalApp.repository.ConfigJournalAppRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {

    public enum keys{
        WEATHER_API;
    }

    @Autowired
    private ConfigJournalAppRepository configJournalAppRepository;

//we store api key in appCache it will work as in memory catch
    public Map<String, String> appCache;

    @PostConstruct
    public void init(){
        appCache=new HashMap<>();
        List<ConfigJournalAppEntity> all=configJournalAppRepository.findAll();
        for(ConfigJournalAppEntity configJournalAppEntity : all){
            appCache.put(configJournalAppEntity.getKey(),configJournalAppEntity.getValue());

        }

    }

}
