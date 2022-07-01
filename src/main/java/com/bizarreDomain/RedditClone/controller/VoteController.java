package com.bizarreDomain.RedditClone.controller;

import com.bizarreDomain.RedditClone.dto.VoteDTO;
import com.bizarreDomain.RedditClone.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vote/")
@AllArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<VoteDTO> vote(@RequestBody VoteDTO voteDTO){
        return ResponseEntity.status(HttpStatus.OK)
                .body(voteService.vote(voteDTO));
    }
}
