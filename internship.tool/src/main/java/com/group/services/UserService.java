package com.group.services;

import com.group.entities.Role;
import com.group.entities.Team;
import com.group.entities.User;
import com.group.exceptions.TeamNotFoundException;
import com.group.exceptions.UserAlreadyAssignedToTeamException;
import com.group.exceptions.UserNotFound;
import com.group.repositories.TeamRepository;
import com.group.repositories.UserRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    public UserService(UserRepository userRepository, TeamRepository teamRepository) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
    }

    public List<User> getAllStudents(boolean sorted) {
        List<User> users;
        if(sorted) users = userRepository.findAllByOrderByNameAsc();
        else users = userRepository.findAll();
        return users;
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getUsersByFields(Integer id, String name, String email, Role role, Integer teamId) {
        Specification<User> spec = Specification.where(null);

        if(id != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id));
        }

        if (name != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), name));
        }

        if (email != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("email"), email));
        }

        if (role != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("role"), role));
        }

        if (teamId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id_team").get("id"), teamId));
        }

        List<User> users = userRepository.findAll(spec);

        if (users.isEmpty()) {
            throw new UserNotFound(HttpStatus.NOT_FOUND);
        }

        return userRepository.findAll(spec);
    }

    public User getUserById(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFound(HttpStatus.NOT_FOUND));
    }

    public List<User> getUsersWithoutTeam() {
        List<User> users = userRepository.findUsersWithoutTeamExcludingMentors(Role.MENTOR);

        if (users.isEmpty()) {
            throw new UserNotFound(HttpStatus.NOT_FOUND);
        }

        return users;
    }

    public User putUserTeam(int userId, int teamId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFound(HttpStatus.NOT_FOUND));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(HttpStatus.NOT_FOUND));

        if (user.getId_team() != null) {
            throw new UserAlreadyAssignedToTeamException("User is already assigned to a team.");
        }

        user.setId_team(team);
        return userRepository.save(user);
    }
}
