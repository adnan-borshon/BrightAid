package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Dto.FundTransparencyDto;
import com.example.Bright_Aid.Entity.FundTransparency;
import com.example.Bright_Aid.Entity.FundUtilization;
import com.example.Bright_Aid.Entity.Admin;
import com.example.Bright_Aid.repository.FundTransparencyRepository;
import com.example.Bright_Aid.repository.FundUtilizationRepository;
import com.example.Bright_Aid.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FundTransparencyService {

    private final FundTransparencyRepository fundTransparencyRepository;
    private final FundUtilizationRepository fundUtilizationRepository;
    private final AdminRepository adminRepository;

    @Transactional
    public FundTransparencyDto createFundTransparency(FundTransparencyDto fundTransparencyDto) {
        log.info("Creating fund transparency for utilization ID: {}", fundTransparencyDto.getUtilizationId());

        FundUtilization utilization = fundUtilizationRepository.findById(fundTransparencyDto.getUtilizationId())
                .orElseThrow(() -> new RuntimeException("Fund utilization not found with ID: " + fundTransparencyDto.getUtilizationId()));

        Admin verifiedBy = adminRepository.findById(fundTransparencyDto.getVerifiedBy())
                .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + fundTransparencyDto.getVerifiedBy()));

        FundTransparency transparency = FundTransparency.builder()
                .utilization(utilization)
                .beforePhotos(fundTransparencyDto.getBeforePhotos())
                .afterPhotos(fundTransparencyDto.getAfterPhotos())
                .beneficiaryFeedback(fundTransparencyDto.getBeneficiaryFeedback())
                .quantityPurchased(fundTransparencyDto.getQuantityPurchased())
                .unitMeasurement(fundTransparencyDto.getUnitMeasurement())
                .unitCost(fundTransparencyDto.getUnitCost())
                .additionalNotes(fundTransparencyDto.getAdditionalNotes())
                .verifiedBy(verifiedBy)
                .isPublic(fundTransparencyDto.getIsPublic())
                .build();

        FundTransparency savedTransparency = fundTransparencyRepository.save(transparency);
        log.info("Successfully created fund transparency with ID: {}", savedTransparency.getTransparencyId());

        return convertToDto(savedTransparency);
    }

    public FundTransparencyDto getFundTransparencyById(Integer transparencyId) {
        log.info("Fetching fund transparency with ID: {}", transparencyId);

        FundTransparency transparency = fundTransparencyRepository.findById(transparencyId)
                .orElseThrow(() -> new RuntimeException("Fund transparency not found with ID: " + transparencyId));

        return convertToDto(transparency);
    }

    public List<FundTransparencyDto> getAllFundTransparencies() {
        log.info("Fetching all fund transparencies");

        List<FundTransparency> transparencies = fundTransparencyRepository.findAll();
        return transparencies.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<FundTransparencyDto> getAllFundTransparencies(Pageable pageable) {
        log.info("Fetching fund transparencies with pagination: {}", pageable);

        Page<FundTransparency> transparencies = fundTransparencyRepository.findAll(pageable);
        return transparencies.map(this::convertToDto);
    }

    @Transactional
    public FundTransparencyDto updateFundTransparency(Integer transparencyId, FundTransparencyDto fundTransparencyDto) {
        log.info("Updating fund transparency with ID: {}", transparencyId);

        FundTransparency existingTransparency = fundTransparencyRepository.findById(transparencyId)
                .orElseThrow(() -> new RuntimeException("Fund transparency not found with ID: " + transparencyId));

        if (fundTransparencyDto.getUtilizationId() != null &&
                !fundTransparencyDto.getUtilizationId().equals(existingTransparency.getUtilization().getUtilizationId())) {
            FundUtilization utilization = fundUtilizationRepository.findById(fundTransparencyDto.getUtilizationId())
                    .orElseThrow(() -> new RuntimeException("Fund utilization not found with ID: " + fundTransparencyDto.getUtilizationId()));
            existingTransparency.setUtilization(utilization);
        }

        if (fundTransparencyDto.getVerifiedBy() != null &&
                !fundTransparencyDto.getVerifiedBy().equals(existingTransparency.getVerifiedBy().getAdminId())) {
            Admin verifiedBy = adminRepository.findById(fundTransparencyDto.getVerifiedBy())
                    .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + fundTransparencyDto.getVerifiedBy()));
            existingTransparency.setVerifiedBy(verifiedBy);
        }

        if (fundTransparencyDto.getBeforePhotos() != null) {
            existingTransparency.setBeforePhotos(fundTransparencyDto.getBeforePhotos());
        }
        if (fundTransparencyDto.getAfterPhotos() != null) {
            existingTransparency.setAfterPhotos(fundTransparencyDto.getAfterPhotos());
        }
        if (fundTransparencyDto.getBeneficiaryFeedback() != null) {
            existingTransparency.setBeneficiaryFeedback(fundTransparencyDto.getBeneficiaryFeedback());
        }
        if (fundTransparencyDto.getQuantityPurchased() != null) {
            existingTransparency.setQuantityPurchased(fundTransparencyDto.getQuantityPurchased());
        }
        if (fundTransparencyDto.getUnitMeasurement() != null) {
            existingTransparency.setUnitMeasurement(fundTransparencyDto.getUnitMeasurement());
        }
        if (fundTransparencyDto.getUnitCost() != null) {
            existingTransparency.setUnitCost(fundTransparencyDto.getUnitCost());
        }
        if (fundTransparencyDto.getAdditionalNotes() != null) {
            existingTransparency.setAdditionalNotes(fundTransparencyDto.getAdditionalNotes());
        }
        if (fundTransparencyDto.getIsPublic() != null) {
            existingTransparency.setIsPublic(fundTransparencyDto.getIsPublic());
        }

        FundTransparency updatedTransparency = fundTransparencyRepository.save(existingTransparency);
        log.info("Successfully updated fund transparency with ID: {}", transparencyId);

        return convertToDto(updatedTransparency);
    }

    @Transactional
    public void deleteFundTransparency(Integer transparencyId) {
        log.info("Deleting fund transparency with ID: {}", transparencyId);

        if (!fundTransparencyRepository.existsById(transparencyId)) {
            throw new RuntimeException("Fund transparency not found with ID: " + transparencyId);
        }

        fundTransparencyRepository.deleteById(transparencyId);
        log.info("Successfully deleted fund transparency with ID: {}", transparencyId);
    }

    public List<FundTransparencyDto> getPublicFundTransparencies() {
        log.info("Fetching all public fund transparencies");

        List<FundTransparency> transparencies = fundTransparencyRepository.findAll();
        return transparencies.stream()
                .filter(transparency -> transparency.getIsPublic())
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<FundTransparencyDto> getFundTransparenciesByUtilization(Integer utilizationId) {
        log.info("Fetching fund transparencies for utilization ID: {}", utilizationId);

        List<FundTransparency> transparencies = fundTransparencyRepository.findAll();
        return transparencies.stream()
                .filter(transparency -> transparency.getUtilization().getUtilizationId().equals(utilizationId))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<FundTransparencyDto> getFundTransparenciesByVerifiedBy(Integer adminId) {
        log.info("Fetching fund transparencies verified by admin ID: {}", adminId);

        List<FundTransparency> transparencies = fundTransparencyRepository.findAll();
        return transparencies.stream()
                .filter(transparency -> transparency.getVerifiedBy().getAdminId().equals(adminId))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private FundTransparencyDto convertToDto(FundTransparency transparency) {
        return FundTransparencyDto.builder()
                .transparencyId(transparency.getTransparencyId())
                .utilizationId(transparency.getUtilization().getUtilizationId())
                .beforePhotos(transparency.getBeforePhotos())
                .afterPhotos(transparency.getAfterPhotos())
                .beneficiaryFeedback(transparency.getBeneficiaryFeedback())
                .quantityPurchased(transparency.getQuantityPurchased())
                .unitMeasurement(transparency.getUnitMeasurement())
                .unitCost(transparency.getUnitCost())
                .additionalNotes(transparency.getAdditionalNotes())
                .verifiedBy(transparency.getVerifiedBy().getAdminId())
                .isPublic(transparency.getIsPublic())
                .createdAt(transparency.getCreatedAt())
                .updatedAt(transparency.getUpdatedAt())
                .build();
    }
}