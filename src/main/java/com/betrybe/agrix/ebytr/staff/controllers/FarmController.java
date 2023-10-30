package com.betrybe.agrix.ebytr.staff.controllers;

import com.betrybe.agrix.ebytr.staff.controllers.dto.CropCreationDto;
import com.betrybe.agrix.ebytr.staff.controllers.dto.FarmDto;
import com.betrybe.agrix.ebytr.staff.entity.Crop;
import com.betrybe.agrix.ebytr.staff.entity.Farm;
import com.betrybe.agrix.ebytr.staff.service.FarmService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * FarmController.
 */
@RestController
@RequestMapping(value = "")
public class FarmController {

  private final FarmService farmService;

  @Autowired
  public FarmController(FarmService farmService) {
    this.farmService = farmService;
  }

  @PostMapping("/farms")
  public ResponseEntity<Farm> createFarm(@RequestBody FarmDto farmDto) {
    Farm newFarm = farmService.insertFarm(farmDto.toFarm());
    return ResponseEntity.status(HttpStatus.CREATED).body(newFarm);
  }

  /**
   * Método insertCrop.
   */
  @PostMapping("/farms/{farmId}/crops")
  public ResponseEntity<?> insertCrop(@RequestBody CropCreationDto cropDto,
      @PathVariable Long farmId) {
    Optional<Farm> optionalFarm = farmService.getFarmById(farmId);

    if (optionalFarm.isEmpty()) {
      String message = "Fazenda não encontrada!";
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    Farm farm = optionalFarm.get();
    Crop crop = cropDto.toCrop();
    crop.setFarm(farm);
    Crop newCrop = farmService.insertCrop(crop);
    Map<String, Object> finalMap = new HashMap<>();
    finalMap.put("id", newCrop.getId());
    finalMap.put("name", newCrop.getName());
    finalMap.put("plantedArea", newCrop.getPlantedArea());
    finalMap.put("farmId", newCrop.getFarm().getId());


    return ResponseEntity.status(HttpStatus.CREATED).body(finalMap);
  }

  /**
   * Método getFarmById.
   */
  @GetMapping("/farms/{farmId}")
  public ResponseEntity<?> getFarmById(@PathVariable Long farmId) {
    Optional<Farm> optionalFarm = farmService.getFarmById(farmId);

    if (optionalFarm.isEmpty()) {
      String message = "Fazenda não encontrada!";
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    return ResponseEntity.ok(optionalFarm.get());
  }

  /**
   * Método getCropById.
   */
  @GetMapping("/crops/{id}")
  public ResponseEntity<?> getCropById(@PathVariable Long id) {
    Optional<Crop> optionalCrop = farmService.getCropById(id);

    if (optionalCrop.isEmpty()) {
      String message = "Plantação não encontrada!";
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }
    Crop crop = optionalCrop.get();

    CropCreationDto responseCrop = new CropCreationDto(crop.getId(), crop.getName(),
        crop.getPlantedArea(), crop.getFarm().getId());

    return ResponseEntity.ok(responseCrop);
  }

  /**
   * Método getCropById.
   */
  @GetMapping("/farms/{farmId}/crops")
  public ResponseEntity<?> getCropsById(@PathVariable Long farmId) {
    Optional<Farm> optionalFarm = farmService.getFarmById(farmId);

    if (optionalFarm.isEmpty()) {
      String message = "Fazenda não encontrada!";
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    List<Crop> crops = farmService.getCropsById(optionalFarm.get());
    crops.forEach(crop -> crop.setFarm(optionalFarm.get()));

    List<CropCreationDto> allCrops = crops.stream()
        .map((crop) -> new CropCreationDto(crop.getId(), crop.getName(), crop.getPlantedArea(),
            crop.getFarm().getId()))
        .toList();

    return ResponseEntity.ok(allCrops);
  }

  /**
   * Método getAllCrops.
   */
  @GetMapping("/crops")
  public List<?> getAllCrops() {
    List<Crop> allCrops = farmService.getAllCrops();
    return allCrops.stream()
        .map((crop) -> new CropCreationDto(crop.getId(), crop.getName(), crop.getPlantedArea(),
            crop.getFarm().getId()))
        .collect(Collectors.toList());
  }

  /**
   * Método getAllFarms.
   */
  @GetMapping("/farms")
  public List<FarmDto> getAllFarms() {
    List<Farm> allFarms = farmService.getAllFarms();
    return allFarms.stream()
        .map((farm) -> new FarmDto(farm.getId(), farm.getName(), farm.getSize()))
        .collect(Collectors.toList());
  }
}
