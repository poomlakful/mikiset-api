package com.poomdev.mikisetapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poomdev.mikisetapi.persistence.SetIndex;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(SetController.class)
public class SetControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private SetController controller;

    @Value("${admin.password}")
    private String password;

    @Test
    public void getIndexByName() throws Exception {
        SetIndex index = new SetIndex("SET", 1000.00, new Date());

        List<SetIndex> indexs = singletonList(index);

        given(controller.getIndexByName(index.getName())).willReturn(indexs);

        mvc.perform(get("/set/index/" + index.getName())
                .with(user("admin").password(password))
                .contentType(APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].name", is(index.getName())));
    }

    @Test
    public void updateIndex() throws Exception {
        SetIndex index = new SetIndex("SET", 1000.00, new Date());

        given(controller.updateIndex(index)).willReturn("Updated");

        mvc.perform(post("/set/index/")
                .with(user("admin").password(password).roles("ADMIN"))
                .characterEncoding("UTF-8")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(index)))
                    .andExpect(status().isForbidden());
    }
}