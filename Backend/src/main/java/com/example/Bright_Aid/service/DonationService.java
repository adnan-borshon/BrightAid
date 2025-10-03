package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Entity.*;
import com.example.Bright_Aid.Dto.DonationDto;
import com.example.Bright_Aid.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

<<<<<<< HEAD
=======
import java.math.BigDecimal;
>>>>>>> 852bdc8 (schema updated)
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DonationService {

    private final DonationRepository donationRepository;
    private final DonorRepository donorRepository;
    private final SchoolProjectRepository schoolProjectRepository;
    private final StudentRepository studentRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;

    public DonationService(DonationRepository donationRepository,
                           DonorRepository donorRepository,
                           SchoolProjectRepository schoolProjectRepository,
                           StudentRepository studentRepository,
                           PaymentTransactionRepository paymentTransactionRepository) {
        this.donationRepository = donationRepository;
        this.donorRepository = donorRepository;
        this.schoolProjectRepository = schoolProjectRepository;
        this.studentRepository = studentRepository;
        this.paymentTransactionRepository = paymentTransactionRepository;
    }

    // Create or update Donation
    public DonationDto saveDonation(DonationDto donationDto) {
        Donor donor = donorRepository.findById(donationDto.getDonorId())
                .orElseThrow(() -> new RuntimeException("Donor not found"));

        SchoolProject project = null;
        if (donationDto.getProjectId() != null) {
            project = schoolProjectRepository.findById(donationDto.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Project not found"));
        }

        Student student = null;
        if (donationDto.getStudentId() != null) {
            student = studentRepository.findById(donationDto.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found"));
        }

        PaymentTransaction transaction = null;
        if (donationDto.getTransactionId() != null) {
            transaction = paymentTransactionRepository.findById(donationDto.getTransactionId())
                    .orElse(null); // Make transaction optional
        }

        Donation donation;
        if (donationDto.getDonationId() != null && donationDto.getDonationId() > 0) {
            // Try to update existing donation, create new if not found
            donation = donationRepository.findById(donationDto.getDonationId())
                    .orElse(null);
            
            if (donation != null) {
                // Update existing donation
                donation.setDonor(donor);
                donation.setProject(project);
                donation.setStudent(student);
                donation.setAmount(donationDto.getAmount());
                donation.setDonationType(donationDto.getDonationType());
                donation.setTransaction(transaction);
                donation.setPaymentStatus(donationDto.getPaymentStatus() != null ?
                        donationDto.getPaymentStatus() : Donation.PaymentStatus.PENDING);
                donation.setPurpose(donationDto.getPurpose());
                donation.setDonorMessage(donationDto.getDonorMessage());
                donation.setIsAnonymous(donationDto.getIsAnonymous() != null ?
                        donationDto.getIsAnonymous() : false);
                donation.setDonatedAt(donationDto.getDonatedAt() != null ?
                        donationDto.getDonatedAt() : LocalDateTime.now());
                donation.setPaymentCompletedAt(donationDto.getPaymentCompletedAt());
            } else {
                // Create new donation if existing not found
                donation = Donation.builder()
                        .donor(donor)
                        .project(project)
                        .student(student)
                        .amount(donationDto.getAmount())
                        .donationType(donationDto.getDonationType())
                        .transaction(transaction)
                        .paymentStatus(donationDto.getPaymentStatus() != null ?
                                donationDto.getPaymentStatus() : Donation.PaymentStatus.PENDING)
                        .purpose(donationDto.getPurpose())
                        .donorMessage(donationDto.getDonorMessage())
                        .isAnonymous(donationDto.getIsAnonymous() != null ?
                                donationDto.getIsAnonymous() : false)
                        .donatedAt(donationDto.getDonatedAt() != null ?
                                donationDto.getDonatedAt() : LocalDateTime.now())
                        .paymentCompletedAt(donationDto.getPaymentCompletedAt())
                        .build();
            }
        } else {
            // Create new donation
            donation = Donation.builder()
                    .donor(donor)
                    .project(project)
                    .student(student)
                    .amount(donationDto.getAmount())
                    .donationType(donationDto.getDonationType())
                    .transaction(transaction)
                    .paymentStatus(donationDto.getPaymentStatus() != null ?
                            donationDto.getPaymentStatus() : Donation.PaymentStatus.PENDING)
                    .purpose(donationDto.getPurpose())
                    .donorMessage(donationDto.getDonorMessage())
                    .isAnonymous(donationDto.getIsAnonymous() != null ?
                            donationDto.getIsAnonymous() : false)
                    .donatedAt(donationDto.getDonatedAt() != null ?
                            donationDto.getDonatedAt() : LocalDateTime.now())
                    .paymentCompletedAt(donationDto.getPaymentCompletedAt())
                    .build();
        }

        Donation saved = donationRepository.save(donation);
        return mapToDto(saved);
    }

    // Get all donations
    public List<DonationDto> getAllDonations() {
        return donationRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Get donation by ID
    public DonationDto getDonationById(Integer donationId) {
        return donationRepository.findById(donationId)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Donation not found"));
    }

    // Delete donation
    public void deleteDonation(Integer donationId) {
        if (!donationRepository.existsById(donationId)) {
            throw new RuntimeException("Donation not found");
        }
        donationRepository.deleteById(donationId);
    }

    // Update payment status
    public DonationDto updatePaymentStatus(Integer donationId, Donation.PaymentStatus paymentStatus) {
        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new RuntimeException("Donation not found"));

        donation.setPaymentStatus(paymentStatus);
        if (paymentStatus == Donation.PaymentStatus.COMPLETED) {
            donation.setPaymentCompletedAt(LocalDateTime.now());
        }

        Donation saved = donationRepository.save(donation);
        return mapToDto(saved);
    }

    // Complete payment
    public DonationDto completePayment(Integer donationId) {
        return updatePaymentStatus(donationId, Donation.PaymentStatus.COMPLETED);
    }

    // Fail payment
    public DonationDto failPayment(Integer donationId) {
        return updatePaymentStatus(donationId, Donation.PaymentStatus.FAILED);
    }

    // Get donations by donor ID
    public List<DonationDto> getDonationsByDonor(Integer donorId) {
        return donationRepository.findByDonor_DonorId(donorId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Get donations by donor ID with transaction details and proper ordering (recent first)
    public List<DonationDto> getDonationsByDonorWithDetails(Integer donorId) {
        List<Object[]> results = donationRepository.findDonationsByDonorWithDetailsOrderByDateDesc(donorId);
        return results.stream()
                .map(this::mapResultToDto)
                .collect(Collectors.toList());
    }

    // Map Donation entity to DTO
    private DonationDto mapToDto(Donation donation) {
        return DonationDto.builder()
                .donationId(donation.getDonationId())
                .donorId(donation.getDonor().getDonorId())
                .projectId(donation.getProject() != null ? donation.getProject().getProjectId() : null)
                .studentId(donation.getStudent() != null ? donation.getStudent().getStudentId() : null)
                .amount(donation.getAmount())
                .donationType(donation.getDonationType())
                .transactionId(donation.getTransaction() != null ? donation.getTransaction().getTransactionId() : null)
                .paymentStatus(donation.getPaymentStatus())
                .purpose(donation.getPurpose())
                .donorMessage(donation.getDonorMessage())
                .isAnonymous(donation.getIsAnonymous())
                .donatedAt(donation.getDonatedAt())
                .paymentCompletedAt(donation.getPaymentCompletedAt())
                .createdAt(donation.getCreatedAt())
                .updatedAt(donation.getUpdatedAt())
                .build();
    }

    // Map native query result to DTO with transaction and project details
    private DonationDto mapResultToDto(Object[] result) {
        return DonationDto.builder()
                .donationId((Integer) result[0])
                .donorId((Integer) result[1])
                .projectId((Integer) result[2])
                .studentId((Integer) result[3])
<<<<<<< HEAD
                .amount((BigDecimal) result[4])
=======
                .amount(result[4] != null ? new BigDecimal(result[4].toString()) : null)
>>>>>>> 852bdc8 (schema updated)
                .donationType(result[5] != null ? Donation.DonationType.valueOf((String) result[5]) : null)
                .transactionId((Integer) result[6])
                .paymentStatus(result[7] != null ? Donation.PaymentStatus.valueOf((String) result[7]) : Donation.PaymentStatus.PENDING)
                .purpose(result[8] != null ? Donation.DonationPurpose.valueOf((String) result[8]) : null)
                .donorMessage((String) result[9])
                .isAnonymous((Boolean) result[10])
                .donatedAt(result[11] != null ? ((java.sql.Timestamp) result[11]).toLocalDateTime() : null)
                .paymentCompletedAt(result[12] != null ? ((java.sql.Timestamp) result[12]).toLocalDateTime() : null)
                .createdAt(result[13] != null ? ((java.sql.Timestamp) result[13]).toLocalDateTime() : null)
                .updatedAt(result[14] != null ? ((java.sql.Timestamp) result[14]).toLocalDateTime() : null)
                .transactionRef((String) result[15]) // Transaction reference from native query
                .projectName((String) result[16]) // Project name from native query
                .build();
    }
}
