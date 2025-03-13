package com.javarush.jira.profile.internal.web;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.jira.common.error.ErrorMessageHandler;
import com.javarush.jira.profile.ContactTo;
import com.javarush.jira.profile.ProfileTo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.anyLong;
import java.util.Set;

@WebMvcTest(ProfileRestController.class)  // Тестируем только контроллер
class ProfileRestControllerTest {

    private static final String REST_URL = ProfileRestController.REST_URL;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ErrorMessageHandler errorMessageHandler;

    @MockBean
    private RestTemplate restTemplate;


    @MockBean
    private MessageSource messageSource;

    @MockBean
    private ProfileRestController profileRestController;



    @Test
    @WithMockUser(username = "user", roles = "USER")
    void getProfile_Success() throws Exception {
        // Подготовка данных
        Set<String> mailNotifications = Set.of("email", "sms");
        Set<ContactTo> contacts = Set.of(new ContactTo("John Doe", "john@example.com"));
        ProfileTo profile = new ProfileTo(1L, mailNotifications, contacts);

        // Заглушка для родительского класса
        when(profileRestController.get(anyLong())).thenReturn(profile);

        // Эмуляция запроса с авторизацией
        mockMvc.perform(get(REST_URL))

                // Эмуляция авторизованного пользователя
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.id").value(1L))  // Проверка ID профиля
//                .andExpect(jsonPath("$.mailNotifications").value(hasItems("email", "sms")))  // Проверка содержимого коллекции
//                .andExpect(jsonPath("$.contacts[0].name").value("John Doe"))  // Проверка первого контакта
//                .andExpect(jsonPath("$.contacts[0].email").value("john@example.com"));  // Проверка email контакта
    }

    @Test
    void getProfile_Unauthorized() throws Exception {
        // Без авторизации
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void updateProfile_Success() throws Exception {
        Set<String> mailNotifications = Set.of("email");
        Set<ContactTo> contacts = Set.of(new ContactTo("skype", "userSkype"));
        ProfileTo updateRequest = new ProfileTo(1L, mailNotifications, contacts);

        // Эмуляция успешного обновления профиля
        mockMvc.perform(put(REST_URL).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andDo(print())
                          // Эмуляция авторизованного пользователя
                .andExpect(status().isNoContent());  // Ожидаем код статуса 204 (NO_CONTENT)
    }

    @Test
    @WithAnonymousUser
    void updateProfile_Unauthorized() throws Exception {
        Set<String> mailNotifications = Set.of("email");
        Set<ContactTo> contacts = Set.of(new ContactTo("Updated Name", "updated@example.com"));
        ProfileTo updateRequest = new ProfileTo(1L, mailNotifications, contacts);

        // Без авторизации
        mockMvc.perform(put(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateRequest))
                        .with(csrf()))
                .andExpect(status().isUnauthorized());  // Ожидаем код статуса 401 (Unauthorized)
    }
}




