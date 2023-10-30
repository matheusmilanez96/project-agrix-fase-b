package com.betrybe.agrix.ebytr.staff.controllers.dto;

import com.betrybe.agrix.ebytr.staff.entity.Crop;

/**
 * CropDto.
 */
public record CropDto(Long id, String name, Double plantedArea) {
  public Crop toCrop() {
    return new Crop(id, name, plantedArea);
  }
}
