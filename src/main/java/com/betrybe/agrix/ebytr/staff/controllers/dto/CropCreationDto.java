package com.betrybe.agrix.ebytr.staff.controllers.dto;

import com.betrybe.agrix.ebytr.staff.entity.Crop;

/**
 * CropCreationDto.
 */
public record CropCreationDto(
    Long id,
    String name,
    Double plantedArea,
    Long farmId
) {
  /**
   * Método fromCrop.
   */
  public CropCreationDto fromCrop(Crop crop) {
    return new CropCreationDto(
        crop.getId(),
        crop.getName(),
        crop.getPlantedArea(),
        crop.getFarm().getId()
    );
  }

  /**
   * Método toCrop.
   */
  public Crop toCrop() {
    Crop crop = new Crop();
    crop.setName(name);
    crop.setPlantedArea(plantedArea);
    return crop;
  }
}
