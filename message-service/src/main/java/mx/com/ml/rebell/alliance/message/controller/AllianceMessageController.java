package mx.com.ml.rebell.alliance.message.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.com.ml.rebell.alliance.message.dto.AllianceSatellitesDto;
import mx.com.ml.rebell.alliance.message.dto.AllianceSatellitesMessagesDto;
import mx.com.ml.rebell.alliance.message.dto.ShipPositionDto;
import mx.com.ml.rebell.alliance.message.serive.AllianceMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(
    path = "/api/v1/alliance/message",
    consumes = {APPLICATION_JSON_VALUE},
    produces = {APPLICATION_JSON_VALUE}
)
@Tag(name = "alliance-message")
public class AllianceMessageController {

  private final AllianceMessageService service;

  @Autowired
  public AllianceMessageController(final AllianceMessageService service) {
    this.service = service;
  }

  @ApiOperation(value = "topSecret", notes = "Obtiene la posición en base a la información de los satélites aliados.")
  @PostMapping(path = {"/topsecret"})
  public ResponseEntity<ShipPositionDto>topSecret(@RequestBody final AllianceSatellitesMessagesDto request) {
      return service.getAlliedShipPosition(request.getSatellites())
          .map(ResponseEntity::ok)
          .orElseGet(() -> ResponseEntity.badRequest().build());
  }

  @ApiOperation(value = "topSecretSplit", notes = "Obtiene la posición en base a la información del satélite aliado.")
  @PostMapping(path = {"/topsecret_split/{satellite_name}"})
  public ResponseEntity<ShipPositionDto>topSecretSplit(@PathVariable(name = "satellite_name") final String satelliteName,
                                                       @RequestBody final AllianceSatellitesDto request) {
    return service.getAlliedShipPosition(satelliteName, request)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.badRequest().build());
  }

  @ApiOperation(value = "topSecretSplit", notes = "Obtiene la posición y el mensaje de un satélite aliado.")
  @GetMapping(path = {"/topsecret_split/{satellite_name}"})
  public ResponseEntity<ShipPositionDto>topSecretSplit(@PathVariable(name = "satellite_name") final String satelliteName) {
    return service.getAlliedShipPosition(satelliteName)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.badRequest().build());
  }
}
