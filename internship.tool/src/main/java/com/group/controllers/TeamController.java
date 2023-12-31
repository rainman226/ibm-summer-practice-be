package com.group.controllers;

import com.group.entities.*;
import com.group.exceptions.TeamNotFoundException;
import com.group.services.EnrollmentService;
import com.group.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/teams")
public class TeamController {
    @Autowired
    private TeamService teamService;
    private final EnrollmentService enrollmentService;

    public TeamController(TeamService teamService, EnrollmentService enrollmentService) {
        this.teamService = teamService;
        this.enrollmentService = enrollmentService;
    }

    @PostMapping(value = "/new")
    public ResponseEntity<Team> addTeam(@RequestBody Team team) {
        Team result = teamService.addTeam(team);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{teamId}/activities")
    public ResponseEntity<List<Activity>> getActivitiesByTeam(@PathVariable Integer teamId) {
        Team team = teamService.getTeamById(teamId);
        if (team == null) {
            return ResponseEntity.notFound().build();
        }

        List<Activity> activities = enrollmentService.getActivityByTeam(team);
        return ResponseEntity.ok(activities);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(value = "/all")
    public List<Team> getAllTeams() {
        return  teamService.getAllTeams();
    }

    @PutMapping("/{teamId}/leader")
    public ResponseEntity<Team> updateTeamLeader(
            @PathVariable int teamId,
            @RequestParam(name = "newLeaderId") int newLeaderId
    ) {
        Team team = teamService.updateTeamLeader(teamId, newLeaderId);
        return ResponseEntity.ok(team);
    }

    @GetMapping
    public ResponseEntity<Team> getTeamById(@RequestParam int teamId) {
            Team team = teamService.getTeamById(teamId);
            return ResponseEntity.ok(team);

    }

 }
