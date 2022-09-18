package ru.practicum.shareit;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import ru.practicum.shareit.requests.ItemRequestController;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
public class RequestControllerTest {
}
