package com.medapp.app.dts.medappbackendspring.Repository;

import com.medapp.app.dts.medappbackendspring.Entity.Appointment;
import com.medapp.app.dts.medappbackendspring.Entity.User;
import com.medapp.app.dts.medappbackendspring.Enum.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByClient(User client);

    List<Appointment> findByClientAndStatus(User client, AppointmentStatus status);

    List<Appointment> findByDoctor(User doctor);

    List<Appointment> findByDoctorAndStatus(User client, AppointmentStatus status);
}
