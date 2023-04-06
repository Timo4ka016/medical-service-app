package com.medapp.app.dts.medappbackendspring.Service;

import com.medapp.app.dts.medappbackendspring.Dto.CreateAdDto;
import com.medapp.app.dts.medappbackendspring.Dto.DoctorDto;
import com.medapp.app.dts.medappbackendspring.Dto.UpdateDoctorDto;
import com.medapp.app.dts.medappbackendspring.Entity.Ad;
import com.medapp.app.dts.medappbackendspring.Entity.User;
import com.medapp.app.dts.medappbackendspring.Enum.Role;
import com.medapp.app.dts.medappbackendspring.Repository.AdRepository;
import com.medapp.app.dts.medappbackendspring.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdRepository adRepository;
    private final PasswordEncoder passwordEncoder;
    private static final ModelMapper mapper = new ModelMapper();


    public void updateDoctor(User user, UpdateDoctorDto request) {
        User existingUser = userRepository.findById(user.getId()).orElseThrow(null);
        if (request.getFirstname() != null && !request.getFirstname().trim().isEmpty()) {
            existingUser.setFirstname(request.getFirstname().trim());
        }
        if (request.getLastname() != null && !request.getLastname().trim().isEmpty()) {
            existingUser.setLastname(request.getLastname().trim());
        }
        if (request.getDescription() != null && !request.getDescription().trim().isEmpty()) {
            existingUser.setDescription(request.getDescription());
        }
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().trim().isEmpty()) {
            existingUser.setPhoneNumber(request.getPhoneNumber().trim());
        }
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(request.getPassword().trim()));
        }
        userRepository.save(existingUser);
    }

    public List<DoctorDto> getDoctorInfo(Long id, String firstname, String lastname) {
        Specification<User> spec = Specification.where(null);
        if (id != null)
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id));
        if (firstname != null)
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("firstname"), firstname));
        if (lastname != null)
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("lastname"), lastname));

        spec = spec.and((root, query, criteriaBuilder) -> {
            return criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("role"), Role.USER_DOCTOR),
                    criteriaBuilder.notEqual(root.get("role"), Role.USER_CLIENT),
                    criteriaBuilder.notEqual(root.get("role"), Role.USER_ADMIN)
            );
        });

        List<User> users = userRepository.findAll(spec);
        return users.stream().map(user -> mapper.map(user, DoctorDto.class)).collect(Collectors.toList());
    }

}
