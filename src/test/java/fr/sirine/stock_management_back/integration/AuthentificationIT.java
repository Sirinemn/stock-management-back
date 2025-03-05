package fr.sirine.stock_management_back.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.sirine.stock_management_back.StockManagementBackApplication;
import fr.sirine.stock_management_back.auth.AuthenticationService;
import fr.sirine.stock_management_back.payload.request.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = StockManagementBackApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthentificationIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthenticationService authenticationService;

    @Test
    void souldRegisterSuccessfully() throws Exception {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .firstname("firstname")
                .lastname("lastname")
                .email("email@test.fr")
                .dateOfBirth(LocalDateTime.now())
                .password("password")
                .build();
        String registrationRequestJson = objectMapper.writeValueAsString(registerRequest);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registrationRequestJson);
        mockMvc.perform(request)
                .andExpect(status().isAccepted());
    }
}
