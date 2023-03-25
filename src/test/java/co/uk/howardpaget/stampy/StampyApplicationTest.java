package co.uk.howardpaget.stampy;

import co.uk.howardpaget.stampy.repository.StampRepository;
import co.uk.howardpaget.stampy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class StampyApplicationTest {

	@MockBean
	StampRepository stampRepository;

	@MockBean
	UserRepository userRepository;

	@Test
	void contextLoads() {
	}

}
