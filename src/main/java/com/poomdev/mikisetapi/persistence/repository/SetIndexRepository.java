package com.poomdev.mikisetapi.persistence.repository;

import com.poomdev.mikisetapi.persistence.SetIndex;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface SetIndexRepository extends CrudRepository<SetIndex, String> {
    List<SetIndex> findByName(String name);
    SetIndex findByNameAndDate(String name, Date date);
}
