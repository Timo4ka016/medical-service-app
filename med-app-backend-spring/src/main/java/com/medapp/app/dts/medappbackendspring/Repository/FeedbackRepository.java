package com.medapp.app.dts.medappbackendspring.Repository;

import com.medapp.app.dts.medappbackendspring.Entity.Feedback;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByDoctorId(Long doctor_id);

    List<Feedback> findAll(Specification<Feedback> spec);
}
