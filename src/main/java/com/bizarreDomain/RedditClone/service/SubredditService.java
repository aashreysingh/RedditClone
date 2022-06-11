package com.bizarreDomain.RedditClone.service;

import com.bizarreDomain.RedditClone.dto.SubredditDTO;
import com.bizarreDomain.RedditClone.mapper.SubredditMapper;
import com.bizarreDomain.RedditClone.model.Subreddit;
import com.bizarreDomain.RedditClone.model.User;
import com.bizarreDomain.RedditClone.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;
    private final AuthService authService;

    @Transactional
    public SubredditDTO save(SubredditDTO subredditDTO){
        User user = authService.getCurrentUser();
        Subreddit subreddit = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDTO, user)); ;
        subredditDTO.setSubredditId(subreddit.getSubredditId());
        return subredditDTO;
    }

    public List<SubredditDTO> getAllSubreddit(){
        return subredditRepository.findAll()
                .stream()
                .map(subredditMapper::mapSubredditToDto)
                .collect(Collectors.toList());
    }

    public SubredditDTO getSubreddit(Long id){
        return subredditMapper.mapSubredditToDto(subredditRepository.getById(id));
    }
}
