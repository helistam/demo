package com.example.demo;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DemoApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHelloControllerAddition() throws Exception {
        mockMvc.perform(get("/hello?first=3&second=4&operator=+"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("3 + 4 = 7")));
    }

    @Test
    public void testHelloControllerSubtraction() throws Exception {
        mockMvc.perform(get("/hello?first=3&second=4&operator=-"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("3 - 4 = -1")));
    }

    @Test
    public void testHelloControllerMultiplication() throws Exception {
        mockMvc.perform(get("/hello?first=3&second=4&operator=*"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("3 * 4 = 12")));
    }

    @Test
    public void testHelloControllerDivision() throws Exception {
        mockMvc.perform(get("/hello?first=12&second=4&operator=/"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("12 / 4 = 3")));
    }

    @Test
    public void testHelloControllerDivisionByZero() throws Exception {
        mockMvc.perform(get("/hello?first=3&second=0&operator=/"))

                .andExpect(content().string(equalTo("Cannot divide by zero")));
    }
}
