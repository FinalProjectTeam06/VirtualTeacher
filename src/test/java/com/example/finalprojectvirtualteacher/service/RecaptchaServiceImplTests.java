import com.example.finalprojectvirtualteacher.exceptions.InvalidRecaptchaException;
import com.example.finalprojectvirtualteacher.models.RecaptchaResponse;
import com.example.finalprojectvirtualteacher.services.RecaptchaServiceImpl;
import com.example.finalprojectvirtualteacher.services.contacts.RecaptchaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecaptchaServiceImplTests {

    private RecaptchaService recaptchaService;

    @Mock
    private Environment environment;

    @Mock
    private RestTemplate restTemplate;

    private static final String VALID_RECAPTCHA_RESPONSE = "validResponse";
    private static final String INVALID_RECAPTCHA_RESPONSE = "invalidResponse";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(environment.getProperty(RecaptchaServiceImpl.RECAPTCHA_SECRET_ATT)).thenReturn("secretKey");

        recaptchaService = new RecaptchaServiceImpl(environment, restTemplate);
    }

    @Test
    void validateRecaptcha_ValidRecaptcha_ShouldNotThrowException() {
        RecaptchaResponse mockRecaptchaResponse = new RecaptchaResponse();
        mockRecaptchaResponse.setSuccess(true);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), isNull(), eq(RecaptchaResponse.class)))
                .thenReturn(ResponseEntity.ok(mockRecaptchaResponse));

        assertDoesNotThrow(() -> recaptchaService.validateRecaptcha(VALID_RECAPTCHA_RESPONSE));
    }

    @Test
    void validateRecaptcha_InvalidRecaptcha_ShouldThrowException() {
        RecaptchaResponse mockRecaptchaResponse = new RecaptchaResponse();
        mockRecaptchaResponse.setSuccess(false);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), isNull(), eq(RecaptchaResponse.class)))
                .thenReturn(ResponseEntity.ok(mockRecaptchaResponse));

        assertThrows(InvalidRecaptchaException.class, () -> recaptchaService.validateRecaptcha(INVALID_RECAPTCHA_RESPONSE));
    }

    @Test
    void verify_ValidRecaptcha_ShouldReturnTrue() {
        RecaptchaResponse mockRecaptchaResponse = new RecaptchaResponse();
        mockRecaptchaResponse.setSuccess(true);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), isNull(), eq(RecaptchaResponse.class)))
                .thenReturn(ResponseEntity.ok(mockRecaptchaResponse));

        assertTrue(recaptchaService.verify(VALID_RECAPTCHA_RESPONSE));
    }

    @Test
    void verify_InvalidRecaptcha_ShouldReturnFalse() {
        RecaptchaResponse mockRecaptchaResponse = new RecaptchaResponse();
        mockRecaptchaResponse.setSuccess(false);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), isNull(), eq(RecaptchaResponse.class)))
                .thenReturn(ResponseEntity.ok(mockRecaptchaResponse));

        assertFalse(recaptchaService.verify(INVALID_RECAPTCHA_RESPONSE));
    }
}
