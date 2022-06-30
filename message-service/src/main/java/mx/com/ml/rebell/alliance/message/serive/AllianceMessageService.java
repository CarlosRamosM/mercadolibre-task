package mx.com.ml.rebell.alliance.message.serive;

import lombok.extern.slf4j.Slf4j;
import mx.com.ml.rebell.alliance.message.config.properties.AllianceSatellitePositionProperties;
import mx.com.ml.rebell.alliance.message.dto.AllianceSatellitesDto;
import mx.com.ml.rebell.alliance.message.dto.PositionDto;
import mx.com.ml.rebell.alliance.message.dto.ShipPositionDto;
import mx.com.ml.rebell.alliance.message.exception.MessageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AllianceMessageService {

  private final LocationService locationService;

  private final MessageService messageService;

  private final RedisService redisService;

  private final AllianceSatellitePositionProperties positionProperties;

  private static final String ALLIANCE_MESSAGE_KEY = "allianceMessage";

  @Autowired
  public AllianceMessageService(final LocationService locationService,
                                final MessageService messageService,
                                final RedisService redisService,
                                final AllianceSatellitePositionProperties positionProperties) {
    this.locationService = locationService;
    this.messageService = messageService;
    this.redisService = redisService;
    this.positionProperties = positionProperties;
  }

  public Optional<ShipPositionDto>getAlliedShipPosition(final List<AllianceSatellitesDto> satellites) {
    satellites.forEach(allianceSatellites -> {
      allianceSatellites.setX(
          positionProperties
              .getSatellites()
              .get(allianceSatellites.getName())
              .getCoordinates()
              .get(0)
      );
      allianceSatellites.setY(
          positionProperties
              .getSatellites()
              .get(allianceSatellites.getName())
              .getCoordinates()
              .get(1)
      );
    });
    var messages = getMessages(satellites);
    var points = getPositions(satellites);
    var distances = getDistances(satellites);
    return getShipPositionDto(messages, points, distances);
  }

  public Optional<ShipPositionDto>getAlliedShipPosition(final String satelliteName,
                                                        final AllianceSatellitesDto satellite) {
    var messages = new ArrayList<List<String>>();
    messages.add(satellite.getMessage());
    double [][] positions = new double[3][];
    positions[0] = new double[] {
        positionProperties.getSatellites().get(satelliteName).getCoordinates().get(0),
        positionProperties.getSatellites().get(satelliteName).getCoordinates().get(1)
    };
    positions[1] = new double[] {0.0, 0.0};
    positions[2] = new double[] {0.0, 0.0};
    double[] distance = {satellite.getDistance(), 0.0, 0.0};
    return getShipPositionDto(messages, positions, distance);
  }

  public Optional<ShipPositionDto>getAlliedShipPosition(final String satelliteName) {
    var position = new PositionDto();
    if (redisService.getValue(ALLIANCE_MESSAGE_KEY) == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe informacion suficiente");
    }
    position.setX(positionProperties.getSatellites().get(satelliteName).getCoordinates().get(0));
    position.setY(positionProperties.getSatellites().get(satelliteName).getCoordinates().get(1));
    var response = new ShipPositionDto();
    response.setMessage((String) redisService.getValue(ALLIANCE_MESSAGE_KEY));
    response.setPosition(position);
    return Optional.of(response);
  }

  /**
   * Obtiene la posici&oacute;n con base en las coordenadas de los sat&eacute;lites y distancia.
   * @param messages Fragmentos del mensaje
   * @param points Puntos cardinales
   * @param distances Distancia de los sat&eacute;lites
   * @return Informaci&oacute;n de la posici&oacte;n
   */
  private Optional<ShipPositionDto>getShipPositionDto(final List<List<String>> messages,
                                                      final double[][] points,
                                                      final double[] distances) {
    var message = getSecretMessage(messages);
    var location = locationService.getLocation(points, distances);
    var position = new PositionDto();
    position.setX(location[0]);
    position.setY(location[1]);
    var response = new ShipPositionDto();
    response.setMessage(message);
    response.setPosition(position);
    redisService.setValue(ALLIANCE_MESSAGE_KEY, message);
    return Optional.of(response);
  }

  /**
   * Obtiene el mensaje a partir de los fragmentos enviados
   * @param source Fragmentos del mensaje
   * @return Mensaje completo
   */
  private String getSecretMessage(final List<List<String>>source) {
    try {
      return messageService.getMessage(source);
    } catch (MessageException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
  }

  /**
   * Forma una lista con todos los fragmentos de mensaje recibidos por los aliados.
   * @param satellites Información de los sat&eacute;lites.
   * @return Lista con todos los fragmentos de mensaje.
   */
  private List<List<String>>getMessages(final List<AllianceSatellitesDto> satellites) {
    var messages = new ArrayList<List<String>>();
    satellites.forEach(allianceSatellites -> messages.add(allianceSatellites.getMessage()));
    return messages;
  }

  /**
   * Arma una matriz con las posiciones de los sat&eacute;lites.
   * @param satellites Informaci&oacute;n de los sat&eacute;lites.
   * @return Matriz de las coordenadas de los sat&eacute;lites.
   */
  private double[][] getPositions(final List<AllianceSatellitesDto> satellites) {
    double [][] positions = new double[satellites.size()][];
    for(int i = 0; i < satellites.size(); i ++) {
      double [] points = {satellites.get(i).getX(), satellites.get(i).getY()};
        positions[i] = points;
    }
    return positions;
  }

  /**
   * Conjunto de distancias de los satélites.
   * @param satellites Información de los satélites.
   * @return Conjunto de las diferentes distancias de cada satélite.
   */
  private double[] getDistances(final List<AllianceSatellitesDto> satellites) {
    return satellites
        .stream()
        .mapToDouble(AllianceSatellitesDto::getDistance)
        .toArray();
  }
}
