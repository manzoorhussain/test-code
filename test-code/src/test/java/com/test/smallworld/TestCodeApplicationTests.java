package com.test.smallworld;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.smallworld.controller.TransactionController;
import com.test.smallworld.dto.TransactionDto;
import com.test.smallworld.service.TransactionDataFetcher;
import com.test.smallworld.util.CommonUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TestCodeApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    TransactionController transactionController;

    @Autowired
    private CommonUtil commonUtil;

    private final String baseURL = "/transactions/v1/api/";
    @Autowired
    private TransactionDataFetcher transactionDataFetcher;

    @Test
    void getTotalTransactionAmount() throws Exception {

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(baseURL + "total"));
        response.andExpect(status().isOk());


    }

    @Test
    void getTotalTransactionAmountByClient() throws Exception {

        String knowClientName = "Tom Shelby";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(baseURL + "transaction/" + knowClientName)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String result = mvcResult.getResponse().getContentAsString();
        assertTrue(result.equals("828.26"));


        String unKnowClientName = "UnKnow";
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(baseURL + "transaction/" + unKnowClientName)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        result = mvcResult.getResponse().getContentAsString();
        assertTrue(result.contains("Invalid sender name"));


    }

    @Test
    void getMaxTransactionAmount() throws Exception {

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(baseURL + "max"));
        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$", is(985.0)));


    }


    @Test
    void countUniqueClients() throws Exception {

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(baseURL + "unique"));
        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$", is(10)));


    }

    @Test
    void hasOpenComplianceIssues() throws Exception {

        String knowName = "Tom Shelby";
        String unKnowName = "UnKnowName";

        ResultActions response1 = mockMvc.perform(MockMvcRequestBuilders.get(baseURL + "compliance/" + knowName));
        response1.andExpect(status().isOk());
        response1.andExpect(jsonPath("$", is(true)));

        ResultActions response2 = mockMvc.perform(MockMvcRequestBuilders.get(baseURL + "compliance/" + unKnowName));
        response2.andExpect(status().isOk());
        response2.andExpect(jsonPath("$", is(false)));


    }

    @Test
    void unResolvedIssues() throws Exception {
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(baseURL + "unresolved-issues"));
        response.andExpect(status().isOk());
    }


    @Test
    void getTransactionsByBeneficiaryName() throws Exception {
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(baseURL + "beneficiaries"));
        response.andExpect(status().isOk());


    }

    @Test
    void allSolvedIssueMessages() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseURL + "solved-messages"))
                .andExpect(status().isOk());


    }

    @Test
    void topThree() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(baseURL + "top-three")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        TransactionDto[] transactionDtos = commonUtil.mapFromJson(content, TransactionDto[].class);
        assertTrue(transactionDtos.length <= 3);


    }

    @Test
    void getTopSender() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseURL + "top-sender"))
                .andExpect(status().isOk());


    }

}
