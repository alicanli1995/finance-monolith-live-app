package com.bist.api.rest;


import com.bist.api.repository.BistValueRepository;
import com.bist.api.repository.BistsRepository;
import com.bist.api.repository.UserExtraRepository;
import com.bist.api.service.impl.BistServiceImpl;
import com.bist.api.util.UtilHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    BistsRepository bistsRepository;

    @MockBean
    BistValueRepository bistValueRepository;

    @MockBean
    UserExtraRepository userExtraRepository;

    @MockBean
    BistServiceImpl bistService;

    @Test
    void get_all_bists_test() throws Exception {
        var getAllBist = UtilHelper.getBistList();

        when(bistService.getBists()).thenReturn(getAllBist);

        mockMvc.perform(get("/api/bists"))
                .andExpect(status().isOk())
                .andExpect(result -> result.getResponse().getContentAsString().contains("bist1"))
                .andExpect(result -> result.getResponse().getContentAsString().contains("bist2"))
                .andExpect(jsonPath("$[0].name").value(getAllBist.get(0).getName()))
                .andExpect(jsonPath("$[1].name").value(getAllBist.get(1).getName()));

        verify(bistService).getBists();

        verify(bistsRepository, times(1)).findAll();
    }

}
