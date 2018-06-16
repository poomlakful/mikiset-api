package com.poomdev.mikisetapi.controller;

import com.poomdev.mikisetapi.persistence.SetIndex;
import com.poomdev.mikisetapi.persistence.repository.SetIndexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/set/index")
public class SetController {

    @Autowired
    private SetIndexRepository repository;

    @RequestMapping(path = "/{name}", method = RequestMethod.GET)
    public List<SetIndex> getIndexByName(@PathVariable(name = "name") String name) {
        List<SetIndex> response = repository.findByName(name);
        return response;
    }


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> updateIndex(@RequestBody SetIndex requestBody) {
        SetIndex response = repository.findByNameAndDate(requestBody.getName(), requestBody.getDate());
        if(response == null) {
            return new ResponseEntity<>(String.format("Data not found in DB for index: [%s] date: [%s]", requestBody.getName(), requestBody.getDate().toString()), HttpStatus.BAD_REQUEST);
        }
        response.setLast(requestBody.getLast());
        response = repository.save(response);
        return new ResponseEntity<>("Updated", HttpStatus.OK);
    }

}
