package mx.com.ml.rebell.alliance.message.serive;

import mx.com.ml.rebell.alliance.message.config.properties.AllianceSatellitePositionProperties;
import mx.com.ml.rebell.alliance.message.config.properties.SatelliteCoordinates;
import mx.com.ml.rebell.alliance.message.dto.AllianceSatellitesDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AllianceMessageServiceTest {

  @InjectMocks
  private AllianceMessageService service;

  @MockBean
  private MessageService messageService = new MessageService();

  @MockBean
  private LocationService locationService = new LocationService();

  @Mock
  private RedisService redisService;

  @Mock
  private AllianceSatellitePositionProperties properties;

  private List<AllianceSatellitesDto> satellites;

  @BeforeEach
  void setUp() {
    service = new AllianceMessageService(locationService, messageService, redisService, properties);
    satellites = createSatellites();
  }

  @Test
  void getAlliedShipPosition() {
    Mockito.when(properties.getSatellites()).thenReturn(createProperties());
    var response = service.getAlliedShipPosition(satellites);
    assertTrue(response.isPresent());
  }

  @Test
  void getAlliedShipPositionException() {
    assertThrows(ResponseStatusException.class,() -> service.getAlliedShipPosition(Collections.emptyList()));
  }

  @Test
  void getAlliedShipPositionTopSecretSplitPost() {
    var messageParts = new ArrayList<String>();
    messageParts.add("esto");
    messageParts.add("es");
    messageParts.add("un");
    messageParts.add("mensaje");
    messageParts.add("secreto");
    var kenobi = createSatelliteKenobi();
    kenobi.setMessage(messageParts);
    Mockito.when(properties.getSatellites()).thenReturn(createProperties());
    var response = service.getAlliedShipPosition("kenobi", kenobi);
    assertTrue(response.isPresent());
  }

  @Test
  void getAlliedShipPositionTopSecretSplitPostException() {
    Mockito.when(properties.getSatellites()).thenReturn(createProperties());
    assertThrows(ResponseStatusException.class, () -> service.getAlliedShipPosition(
        "kenobi",
        createSatelliteKenobi()
    ));
  }

  @Test
  void getAlliedShipPositionTopSecretSplitGet() {
    Mockito.when(properties.getSatellites()).thenReturn(createProperties());
    Mockito.when(redisService.getValue("allianceMessage")).thenReturn("esto es un mensaje secreto");
    var response = service.getAlliedShipPosition("kenobi");
    assertTrue(response.isPresent());
  }

  @Test
  void getAlliedShipPositionTopSecretSplitGetException() {
    Mockito.when(redisService.getValue("allianceMessage")).thenReturn(null);
    assertThrows(ResponseStatusException.class, () -> service.getAlliedShipPosition("kenobi"));
  }

  private List<AllianceSatellitesDto>createSatellites() {
    var satellites = new ArrayList<AllianceSatellitesDto>();
    satellites.add(createSatelliteKenobi());
    satellites.add(createSatelliteSkywalker());
    satellites.add(createSatelliteSato());
    return satellites;
  }

  private AllianceSatellitesDto createSatelliteKenobi() {
    var satelliteKenobi = new AllianceSatellitesDto();
    var messageParts = new ArrayList<String>();
    messageParts.add("esto");
    messageParts.add("");
    messageParts.add("");
    messageParts.add("mensaje");
    messageParts.add("");
    satelliteKenobi.setDistance(100);
    satelliteKenobi.setName("kenobi");
    satelliteKenobi.setMessage(messageParts);
    return satelliteKenobi;
  }

  private AllianceSatellitesDto createSatelliteSkywalker() {
    var satelliteSkywalker = new AllianceSatellitesDto();
    var messageParts = new ArrayList<String>();
    messageParts.add("esto");
    messageParts.add("");
    messageParts.add("un");
    messageParts.add("");
    messageParts.add("secreto");
    satelliteSkywalker.setDistance(120);
    satelliteSkywalker.setName("skywalker");
    satelliteSkywalker.setMessage(messageParts);
    return satelliteSkywalker;
  }

  private AllianceSatellitesDto createSatelliteSato() {
    var satelliteSato = new AllianceSatellitesDto();
    var messageParts = new ArrayList<String>();
    messageParts.add("esto");
    messageParts.add("es");
    messageParts.add("");
    messageParts.add("");
    messageParts.add("secreto");
    satelliteSato.setDistance(120);
    satelliteSato.setName("sato");
    satelliteSato.setMessage(messageParts);
    return satelliteSato;
  }

  private Map<String, SatelliteCoordinates>createProperties() {
    var properties = new HashMap<String, SatelliteCoordinates>();
    var kenobiProperties = new SatelliteCoordinates();
    var kenobi = new ArrayList<Double>();
    kenobi.add((double) -500);
    kenobi.add((double) 100);
    kenobiProperties.setCoordinates(kenobi);

    var skywalkerProperties = new SatelliteCoordinates();
    var skywalker = new ArrayList<Double>();
    skywalker.add((double) -500);
    skywalker.add((double) 100);
    skywalkerProperties.setCoordinates(skywalker);

    var satoProperties = new SatelliteCoordinates();
    var sato = new ArrayList<Double>();
    sato.add((double) -500);
    sato.add((double) 100);
    satoProperties.setCoordinates(sato);

    properties.put("kenobi", kenobiProperties);
    properties.put("skywalker", skywalkerProperties);
    properties.put("sato", satoProperties);
    return properties;
  }
}