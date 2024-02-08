package com.rabobank.loans.infrastructure.repositories.entities;

import com.rabobank.loans.domain.enums.CalculationType;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity(name = "loan_estimate")
public class LoanEstimate {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Double value;

    @Column(nullable = false)
    private CalculationType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private User user;

    @CreatedDate
    private LocalDateTime createdAt;

    // Due to time constraints, a builder was not created, instead the static method below is being used
    public static LoanEstimate create(Double value, User user, CalculationType type) {
        var newCalculation = new LoanEstimate();
        newCalculation.setUser(user);
        newCalculation.setType(type);
        newCalculation.setValue(value);
        return newCalculation;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public CalculationType getType() {
        return type;
    }

    public void setType(CalculationType type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
