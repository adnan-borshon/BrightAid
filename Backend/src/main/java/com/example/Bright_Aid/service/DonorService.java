package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Entity.Donor;
import com.example.Bright_Aid.Entity.User;
import com.example.Bright_Aid.Dto.DonorDto;
import com.example.Bright_Aid.repository.DonorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DonorService {

    private final DonorRepository donorRepository;

    private DonorDto toDTO(Donor donor) {
        DonorDto dto = new DonorDto();
        dto.setDonorId(donor.getDonorId());
        dto.setUserId(donor.getUser().getUserId());
        dto.setDonorName(donor.getDonorName());
        dto.setTaxId(donor.getTaxId());
        dto.setIsAnonymous(donor.getIsAnonymous());
        dto.setTotalDonated(donor.getTotalDonated());
        dto.setTotalSchoolsSupported(donor.getTotalSchoolsSupported());
        dto.setTotalStudentsSponsored(donor.getTotalStudentsSponsored());
        return dto;
    }

    private Donor toEntity(DonorDto dto) {
        Donor donor = new Donor();
        donor.setDonorId(dto.getDonorId());
        donor.setDonorName(dto.getDonorName());
        donor.setTaxId(dto.getTaxId());
        donor.setIsAnonymous(dto.getIsAnonymous() != null ? dto.getIsAnonymous() : false);
        donor.setTotalDonated(dto.getTotalDonated() != null ? dto.getTotalDonated() : BigDecimal.ZERO);
        donor.setTotalSchoolsSupported(dto.getTotalSchoolsSupported() != null ? dto.getTotalSchoolsSupported() : 0);
        donor.setTotalStudentsSponsored(dto.getTotalStudentsSponsored() != null ? dto.getTotalStudentsSponsored() : 0);

        // Link user only by ID
        if (dto.getUserId() != null) {
            User user = new User();
            user.setUserId(dto.getUserId());
            donor.setUser(user);
        }

        return donor;
    }

    public DonorDto createDonor(DonorDto donorDto) {
        Donor donor = toEntity(donorDto);
        Donor saved = donorRepository.save(donor);
        return toDTO(saved);
    }

    public Optional<DonorDto> getByUserId(Integer userId) {
        return donorRepository.findByUserId(userId).map(this::toDTO);
    }

    public List<DonorDto> getAnonymousDonors() {
        return donorRepository.findAllAnonymousDonors()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<DonorDto> getDonorsByMinDonation(BigDecimal amount) {
        return donorRepository.findByTotalDonatedGreaterThan(amount)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<DonorDto> getTopDonors(int limit) {
        return donorRepository.findTopDonors(PageRequest.of(0, limit))
                .stream().map(this::toDTO).collect(Collectors.toList());
    }
}
