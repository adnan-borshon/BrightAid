package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Entity.NgoProjectRequests;
import com.example.Bright_Aid.Dto.NgoProjectRequestsDTO;
import com.example.Bright_Aid.repository.NgoProjectRequestsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NgoProjectRequestsService {

    private final NgoProjectRequestsRepository repository;

    public NgoProjectRequestsService(NgoProjectRequestsRepository repository) {
        this.repository = repository;
    }

    // ===================== CREATE =====================
    public NgoProjectRequestsDTO create(NgoProjectRequestsDTO dto) {
        NgoProjectRequests entity = mapToEntity(dto);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        NgoProjectRequests saved = repository.save(entity);
        return mapToDTO(saved);
    }

    // ===================== UPDATE =====================
    public NgoProjectRequestsDTO update(Integer id, NgoProjectRequestsDTO dto) {
        NgoProjectRequests entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        entity.setNgoProjectId(dto.getNgoProjectId());
        entity.setSchoolId(dto.getSchoolId());
        entity.setRequestType(dto.getRequestType() != null ? parseRequestType(dto.getRequestType()) : null);
        entity.setStatus(dto.getStatus() != null ? parseRequestStatus(dto.getStatus()) : null);
        entity.setRequestMessage(dto.getRequestMessage());
        entity.setRequestedBudget(dto.getRequestedBudget());
        entity.setRequestedAt(dto.getRequestedAt());
        entity.setRequestedByUserId(dto.getRequestedByUserId());
        entity.setResponseMessage(dto.getResponseMessage());
        entity.setRespondedAt(dto.getRespondedAt());
        entity.setRespondedByUserId(dto.getRespondedByUserId());
        entity.setNpsId(dto.getNpsId());
        entity.setUpdatedAt(LocalDateTime.now());

        NgoProjectRequests updated = repository.save(entity);
        return mapToDTO(updated);
    }

    // ===================== GET BY ID =====================
    public NgoProjectRequestsDTO getById(Integer id) {
        NgoProjectRequests entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        return mapToDTO(entity);
    }

    // ===================== GET ALL =====================
    public List<NgoProjectRequestsDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ===================== DELETE =====================
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    // ===================== MAPPER METHODS =====================
    private NgoProjectRequestsDTO mapToDTO(NgoProjectRequests entity) {
        return NgoProjectRequestsDTO.builder()
                .requestId(entity.getRequestId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .ngoProjectId(entity.getNgoProjectId())
                .schoolId(entity.getSchoolId())
                .requestType(entity.getRequestType() != null ? entity.getRequestType().name() : null)
                .status(entity.getStatus() != null ? entity.getStatus().name() : null)
                .requestMessage(entity.getRequestMessage())
                .requestedBudget(entity.getRequestedBudget())
                .requestedAt(entity.getRequestedAt())
                .requestedByUserId(entity.getRequestedByUserId())
                .responseMessage(entity.getResponseMessage())
                .respondedAt(entity.getRespondedAt())
                .respondedByUserId(entity.getRespondedByUserId())
                .npsId(entity.getNpsId())
                .build();
    }

    private NgoProjectRequests mapToEntity(NgoProjectRequestsDTO dto) {
        return NgoProjectRequests.builder()
                .ngoProjectId(dto.getNgoProjectId())
                .schoolId(dto.getSchoolId())
                .requestType(dto.getRequestType() != null ? parseRequestType(dto.getRequestType()) : null)
                .status(dto.getStatus() != null ? parseRequestStatus(dto.getStatus()) : null)
                .requestMessage(dto.getRequestMessage())
                .requestedBudget(dto.getRequestedBudget())
                .requestedAt(dto.getRequestedAt())
                .requestedByUserId(dto.getRequestedByUserId())
                .responseMessage(dto.getResponseMessage())
                .respondedAt(dto.getRespondedAt())
                .respondedByUserId(dto.getRespondedByUserId())
                .npsId(dto.getNpsId())
                .build();
    }
    
    private NgoProjectRequests.RequestType parseRequestType(String type) {
        try {
            return NgoProjectRequests.RequestType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return NgoProjectRequests.RequestType.JOIN_REQUEST; // Default value
        }
    }
    
    private NgoProjectRequests.RequestStatus parseRequestStatus(String status) {
        try {
            return NgoProjectRequests.RequestStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return NgoProjectRequests.RequestStatus.PENDING; // Default value
        }
    }
}
