package ma.fst.controller;


import ma.fst.dto.LogisticDTO;
import ma.fst.entity.Status;
import ma.fst.service.LogisticService;
import ma.fstt.common.messages.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@Validated
@RestController
@RequestMapping("/logistics")
public class LogisticController {
    @Autowired
    private LogisticService logisticService;

    @GetMapping
    public ResponseEntity<List<LogisticDTO>> getAllLogistic() {
        List<LogisticDTO> list = logisticService.findLogisticList();
        return new ResponseEntity<List<LogisticDTO>>(list, HttpStatus.OK);
    }

    @PostMapping(value = { "/add" })
    public ResponseEntity<BaseResponse> createLogistic(@RequestBody LogisticDTO userDTO) {
        BaseResponse response = logisticService.createOrUpdateLogistic(userDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/requests/{status}")
    public ResponseEntity<List<LogisticDTO>> getAllLogisticByType(@PathVariable Status status) {
        List<LogisticDTO> list = logisticService.findByStatus(status);
        return new ResponseEntity<List<LogisticDTO>>(list, HttpStatus.OK);
    }

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<List<LogisticDTO>> getAllLogisticByUserId(@PathVariable Long id) {
        List<LogisticDTO> list = logisticService.findLogisticByUserId(id);
        return new ResponseEntity<List<LogisticDTO>>(list, HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<BaseResponse> updateLogistic(
            @PathVariable("id") Long id,
            @RequestBody LogisticDTO updatedLogisticDTO) {

        BaseResponse response = logisticService.updateLogistic(id, updatedLogisticDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<BaseResponse> deleteLogisticById(@PathVariable Long id) {
        BaseResponse response = logisticService.deleteLogistic(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<LogisticDTO> getLogisticById(@PathVariable Long id) {
        LogisticDTO list = logisticService.findByLogisticId(id);
        return new ResponseEntity<LogisticDTO>(list, HttpStatus.OK);
    }

}
