package com.poomdev.mikisetapi.controller;

import com.poomdev.mikisetapi.persistence.SetIndex;
import com.poomdev.mikisetapi.persistence.repository.SetIndexRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public SetIndex getUpdateIndex(@RequestBody SetIndex requestBody) {
        SetIndex response = repository.findByNameAndDate(requestBody.getName(), requestBody.getDate());
        if(response == null) {
            return null;
        }
        response.setLast(requestBody.getLast());
        response = repository.save(response);
        return response;
    }

}
