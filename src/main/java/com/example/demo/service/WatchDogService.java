package com.example.demo.service;

import com.example.demo.model.WatchDog;
import com.example.demo.repository.WatchdogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.ServiceMode;
import java.util.Optional;

@Service
public class WatchDogService {

    @Autowired
    private WatchdogRepository watchdogRepository;

    public WatchDog createWatchDog(String patch, String date, String time, String filename, String action) {
        WatchDog a = new WatchDog();
        a.setPath(patch);
        a.setDate(date);
        a.setTime(time);
        a.setFileName(filename);
        a.setAction(action);
        return watchdogRepository.save(a);
    }
    public WatchDog getWatchDogById(long id) {
        Optional<WatchDog> watchDog = watchdogRepository.findById(id);
        if (!watchDog.isPresent()) {
            throw new RuntimeException("Cannot find by id:" + id);
        }

        return  watchDog.get();
    }
}
