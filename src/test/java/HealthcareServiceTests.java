import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.alert.SendAlertServiceImpl;
import ru.netology.patient.service.medical.MedicalService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

public class HealthcareServiceTests {
    @BeforeAll
    public static void initSuite() {
        System.out.println("Running Healthcare-Service Tests");
    }

    @AfterAll
    public static void completeSuite() {
        System.out.println("Healthcare-Service Tests completed");
    }

    @BeforeEach
    public void init() {
        System.out.println("Test started");
    }

    @AfterEach
    public void finished() {
        System.out.println("Test finished");
    }

    @Test
    public void test_message_bloodPressure() {
        String id = "";
        BloodPressure bloodPressure = new BloodPressure(200, 78);

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoRepository.getById(Mockito.anyString()))
                .thenReturn(new PatientInfo("Семен", "Михайлов", LocalDate.of(1982, 1, 16),
                        new HealthInfo(new BigDecimal("36.6"), new BloodPressure(125, 78))));

        SendAlertService sendAlertService = Mockito.mock(SendAlertServiceImpl.class);

        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkBloodPressure(id, bloodPressure);

        Mockito.verify(sendAlertService, Mockito.only()).send(Mockito.anyString());
    }

    @Test
    public void test_message_temperature() {
        String id = "";
        BigDecimal temperature = new BigDecimal("35.0");

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoRepository.getById(Mockito.anyString()))
                .thenReturn(new PatientInfo("Семен", "Михайлов", LocalDate.of(1982, 1, 16),
                        new HealthInfo(new BigDecimal("36.6"), new BloodPressure(125, 78))));

        SendAlertService sendAlertService = Mockito.mock(SendAlertServiceImpl.class);

        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkTemperature(id, temperature);

        Mockito.verify(sendAlertService, Mockito.only()).send(Mockito.anyString());
    }

    @Test
    public void test_message_health_normal() {
        String id = "";
        BloodPressure bloodPressure = new BloodPressure(125, 78);
        BigDecimal temperature = new BigDecimal("36.4");

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoRepository.getById(Mockito.anyString()))
                .thenReturn(new PatientInfo("Семен", "Михайлов", LocalDate.of(1982, 1, 16),
                        new HealthInfo(new BigDecimal("36.6"), new BloodPressure(125, 78))));

        SendAlertService sendAlertService = Mockito.mock(SendAlertServiceImpl.class);

        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkBloodPressure(id, bloodPressure);
        medicalService.checkTemperature(id, temperature);

        Mockito.verify(sendAlertService, Mockito.never()).send(Mockito.anyString());
    }

    @Test
    public void test_method_call() {
        SendAlertService sendAlertService = Mockito.spy(SendAlertServiceImpl.class);
        sendAlertService.send("Test message");

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.verify(sendAlertService).send(argumentCaptor.capture());
        Assertions.assertEquals("Test message", argumentCaptor.getValue());
    }
}