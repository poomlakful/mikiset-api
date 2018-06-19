package com.poomdev.mikisetapi.persistence.repository;

import com.poomdev.mikisetapi.persistence.SetIndex;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SetIndexRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SetIndexRepository repository;

    @Test
    public void findByName() {
        SetIndex index1 = new SetIndex("SET", 1000.00, new Date());
        entityManager.persist(index1);
        entityManager.flush();
        SetIndex index2 = new SetIndex("SET", 1000.00, new Date());
        entityManager.persist(index2);
        entityManager.flush();

        List<SetIndex> indexList = repository.findByName(index1.getName());

        assertThat(indexList.size()).isEqualTo(2);
        assertThat(indexList.get(0)).isEqualTo(index1);
        assertThat(indexList.get(1)).isEqualTo(index2);
    }

    @Test
    public void findByNameAndDate() {
        Date date = new Date();

        SetIndex indexIn = new SetIndex("SET", 1000.00, date);
        entityManager.persist(indexIn);
        entityManager.flush();

        SetIndex indexOut = repository.findByNameAndDate(indexIn.getName(), indexIn.getDate());

        assertThat(indexOut).isEqualTo(indexIn);
    }
}