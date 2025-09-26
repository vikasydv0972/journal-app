package com.myproject.journalApp.scheduler;

import com.myproject.journalApp.cache.AppCache;
import com.myproject.journalApp.entity.JournalEntry;
import com.myproject.journalApp.entity.User;
import com.myproject.journalApp.enums.Sentiment;
import com.myproject.journalApp.repository.UserRepositoryImpl;
import com.myproject.journalApp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {
    @Autowired
    AppCache appCache;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepository;


//use to schedule email use cron expresion
 //   @Scheduled(cron = "0 0 9 * * SUN" )
    @Scheduled(cron = "0 * * ? * *" )

public void fetchUsersAndSendSaMail(){
    List<User> users = userRepository.getUserForSA();
    for(User user : users){
        List<JournalEntry> journalEntries = user.getJournalEntries();
        List<Sentiment> sentiments = journalEntries.stream()
                .filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS)))
                .map(x -> x.getSentiment())
                .collect(Collectors.toList());
        Map<Sentiment, Integer> sentimentCounts=new HashMap();

        for(Sentiment sentiment : sentiments){
            sentimentCounts.put(sentiment,sentimentCounts.getOrDefault(sentiment,0)+1);

        }
        Sentiment mostFrequentSentiment=null;
        int maxCount=0;
        for(Map.Entry<Sentiment, Integer> entry : sentimentCounts.entrySet()){
            if(entry.getValue()>maxCount){
                maxCount=entry.getValue();
                mostFrequentSentiment=entry.getKey();

            }
        }

        if(mostFrequentSentiment!=null){
            emailService.sendEmail(user.getEmail(),"Sentiment for last 7 days", mostFrequentSentiment.toString());
        }

    }
}


//scheduler for clear in memory app cache every 5 min
    @Scheduled(cron = "0 0/10 * ? * *" )
    public void clearAppCache(){
        appCache.init();
    }
}
