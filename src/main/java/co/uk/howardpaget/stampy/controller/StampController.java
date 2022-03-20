package co.uk.howardpaget.stampy.controller;

import co.uk.howardpaget.stampy.controller.dto.StampRequest;
import co.uk.howardpaget.stampy.entity.StampEntity;
import co.uk.howardpaget.stampy.repository.StampRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class StampController {

  private final StampRepository stampRepository;

  public StampController(StampRepository stampRepository) {
    this.stampRepository = stampRepository;
  }

  @GetMapping("/stamps")
  public Iterable<StampEntity> getStamps() {
    return stampRepository.findAll();
  }

  @PostMapping("/stamps")
  public StampEntity createStamp(@RequestBody StampRequest request) {
    return stampRepository.save(new StampEntity(request.getName()));
  }

  @DeleteMapping("/stamps/{id}")
  public ResponseEntity<Void> deleteStamp(@PathVariable long id) {
    if (!stampRepository.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    stampRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }
}
