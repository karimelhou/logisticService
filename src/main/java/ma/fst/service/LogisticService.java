package ma.fst.service;

import jakarta.transaction.Transactional;
import ma.fst.dto.LogisticDTO;
import ma.fst.dto.UserDTO;
import ma.fst.entity.LogisticEntity;
import ma.fst.entity.Status;
import ma.fst.repo.LogisticRepo;
import ma.fstt.common.exceptions.RecordNotFoundException;
import ma.fstt.common.messages.BaseResponse;
import ma.fstt.common.messages.CustomMessage;
import ma.fstt.common.utils.Topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LogisticService {

    @Autowired
    private LogisticRepo logisticRepo;


    private final WebClient webClient;

    public LogisticService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8082/api/v1/auth/users").build();
    }

    public List<LogisticDTO> findLogisticList() {
        return logisticRepo.findAll().stream().map(this::copyLogisticEntityToDto).collect(Collectors.toList());
    }

    public List<LogisticDTO> findLogisticByUserId(Long id) {
        return logisticRepo.findByUserId(id).stream().map(this::copyLogisticEntityToDto).collect(Collectors.toList());
    }

    public LogisticDTO findByLogisticId(Long logisticId) {
        LogisticEntity userEntity = logisticRepo.findById(logisticId)
                .orElseThrow(() -> new RecordNotFoundException("Logistic id '" + logisticId + "' does not exist !"));
        return copyLogisticEntityToDto(userEntity);
    }

    public BaseResponse createOrUpdateLogistic(LogisticDTO logisticDTO) {
        // Vérifie d'abord l'existence de l'utilisateur avant de créer une logistic
        Mono<UserDTO> userMono = webClient.get()
                .uri("/{id}", logisticDTO.getUserId())
                .retrieve()
                .bodyToMono(UserDTO.class);

        // Attendez la réponse du service d'authentification
        UserDTO userResponse = userMono.block();

        if (userResponse != null && userResponse.getId() != null) {
            // L'utilisateur existe, continuez avec la création ou la mise à jour de l'logistic
            LogisticEntity logisticEntity = copyLogisticDtoToEntity(logisticDTO);
            logisticRepo.save(logisticEntity);
            return new BaseResponse(Topic.ASSISTANCE.getName() + CustomMessage.SAVE_SUCCESS_MESSAGE, HttpStatus.CREATED.value());
        } else {
            // L'utilisateur n'existe pas, vous pouvez gérer cela en lançant une exception, par exemple
            throw new RecordNotFoundException("L'utilisateur avec l'ID " + logisticDTO.getUserId() + " n'existe pas.");
        }

    }

    public BaseResponse updateLogistic(Long logisticId, LogisticDTO updatedLogisticDTO) {
        // Check if the logistic with the given ID exists in the database
        if (!logisticRepo.existsById(logisticId)) {
            throw new RecordNotFoundException("Logistic id '" + logisticId + "' does not exist!");
        }

        // Find the existing LogisticEntity by ID
        LogisticEntity existingLogisticEntity = logisticRepo.findById(logisticId)
                .orElseThrow(() -> new RecordNotFoundException("Logistic id '" + logisticId + "' does not exist !"));

        // Update the fields of the existing entity with the values from the updated DTO
        existingLogisticEntity.setType(updatedLogisticDTO.getType());
        existingLogisticEntity.setStatus(updatedLogisticDTO.getStatus());


        // Save the updated entity back to the database
        logisticRepo.save(existingLogisticEntity);
        return new BaseResponse(Topic.ASSISTANCE.getName() + CustomMessage.UPDATE_SUCCESS_MESSAGE, HttpStatus.OK.value());
    }

    public BaseResponse deleteLogistic(Long logisticId) {
        if (logisticRepo.existsById(logisticId)) {
            logisticRepo.deleteById(logisticId);
        } else {
            throw new RecordNotFoundException("No record found for given id: " + logisticId);
        }
        return new BaseResponse(Topic.ASSISTANCE.getName() + CustomMessage.DELETE_SUCCESS_MESSAGE, HttpStatus.OK.value());
    }


    private LogisticDTO copyLogisticEntityToDto(LogisticEntity logisticEntity) {
        LogisticDTO logisticDTO = new LogisticDTO();
        logisticDTO.setLogisticId(logisticEntity.getLogisticId());
        logisticDTO.setUserId(logisticEntity.getUserId());
        logisticDTO.setType(logisticEntity.getType());
        logisticDTO.setStatus(logisticEntity.getStatus());
        return logisticDTO;
    }

    private LogisticEntity copyLogisticDtoToEntity(LogisticDTO logisticDTO) {
        LogisticEntity userEntity = new LogisticEntity();
        userEntity.setLogisticId(logisticDTO.getLogisticId());
        userEntity.setStatus(logisticDTO.getStatus());
        userEntity.setUserId(logisticDTO.getUserId());
        userEntity.setType(logisticDTO.getType());
        return userEntity;
    }


    public List<LogisticDTO> findByStatus(Status status) {
        return logisticRepo.findByStatus(status).stream().map(this::copyLogisticEntityToDto).collect(Collectors.toList());

    }
}
