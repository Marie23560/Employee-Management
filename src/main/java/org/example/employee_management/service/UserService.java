package org.example.employee_management.service;

import jakarta.persistence.Cacheable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.example.employee_management.Dtos.UserDto.*;
import org.example.employee_management.GlobalHandler.ResourceNotFoundException;
import org.example.employee_management.Models.User;
import org.example.employee_management.Repository.UserRepository;
import org.example.employee_management.Security.JwtUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Data
@Transactional
@Cacheable
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    public User getActor(HttpServletRequest request) {
        String email = jwtUtil.getEmailFromToken(request);

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Logged-in user not found"));
    }

    private void grantAccessByRole(Long id, HttpServletRequest request){

        User user = getActor(request);

        if(user.getRole().equals("ROLE_USER")) {
            grantUserAccess(user, id);
        }
    }

    private void grantUserAccess(User user, Long id){
        if(!user.getId().equals(id)) {
            throw new AccessDeniedException("You are not allowed to access this resource");
        }
    }

    public String register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            return "Email already exists!";
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);

        return "User registered successfully!";
    }

    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        String token = jwtUtil.generateToken(user);

        return new LoginResponse(token, user.getId());
    }

    public UserResponseDTO getUserById(Long id, HttpServletRequest request){
        grantAccessByRole(id, request); //check role to grant access accordingly

        return userRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("User with id '" + id + "' was not found!"));
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO, HttpServletRequest request){
        //Logged-in user
        User actor = getActor(request);

        return userRepository.findById(id)
                .map(target -> {

                    if(actor.getRole().equals("ROLE_USER")) { //if the Jwt role is USER

                        if(!actor.getId().equals(id)) {
                            throw new AccessDeniedException("You cannot update another user's account");
                        }


                        target.setUsername(userRequestDTO.getUsername());
                        target.setEmail(userRequestDTO.getEmail());
                        target.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));

                    } else if (actor.getRole().equals("ROLE_ADMIN")) {

                        if(actor.getId().equals(id)) {


                            target.setUsername(userRequestDTO.getUsername());
                            target.setEmail(userRequestDTO.getEmail());
                            target.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
                        } else {


                            target.setRole(userRequestDTO.getRole());
                        }
                    }

                    userRepository.save(target);

                    return toDto(target);
                }).orElseThrow(() -> new ResourceNotFoundException("User with id '" + id + "' was not found!"));
    }

    public Page<UserResponseDTO> getAllUsers( HttpServletRequest request, int page, int size, String sortBy, String direction){
        if (getActor(request).getRole().equals("ROLE_ADMIN")) {
            Sort sort = direction.equalsIgnoreCase("desc")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();

            Pageable pageable = PageRequest.of(page, size, sort);

            Page<User> postsPage = userRepository.findAll(pageable);

            return postsPage.map(this::toDto);
        }

        throw  new AccessDeniedException("You are not allowed to access a list of all users.");
    }

    public String delete(Long id, HttpServletRequest request){
        grantAccessByRole(id, request);

        if(!userRepository.existsById(id)){
            throw new ResourceNotFoundException("User with id '" + id + "' was not found");
        }
        userRepository.deleteById(id);
        return "User with id: " + id + " was deleted Successfully";
    }

    private UserResponseDTO toDto(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        return dto;
    }
}
