package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Entity.*;
import com.example.Bright_Aid.Dto.DonationDto;
import com.example.Bright_Aid.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                    .orElseThrow(() -> new RuntimeException("Transaction not found"));
        }

        Donation donation = Donation.builder()
                .donationId(donationDto.getDonationId())
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
                .fundUtilizationId(donation.getFundUtilization() != null ? donation.getFundUtilization().getUtilizationId() : null)
                .createdAt(donation.getCreatedAt())
                .updatedAt(donation.getUpdatedAt())
                .build();
    }
}
