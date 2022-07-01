package com.bizarreDomain.RedditClone.service;

import com.bizarreDomain.RedditClone.dto.VoteDTO;
import com.bizarreDomain.RedditClone.exceptions.PostNotFoundException;
import com.bizarreDomain.RedditClone.exceptions.SpringRedditException;
import com.bizarreDomain.RedditClone.mapper.VoteMapper;
import com.bizarreDomain.RedditClone.model.Post;
import com.bizarreDomain.RedditClone.model.User;
import com.bizarreDomain.RedditClone.model.Vote;
import com.bizarreDomain.RedditClone.model.VoteType;
import com.bizarreDomain.RedditClone.repository.PostRepository;
import com.bizarreDomain.RedditClone.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;

    private final AuthService authService;

    private final VoteMapper voteMapper;

    public VoteDTO vote(VoteDTO voteDTO){
        Post post = postRepository.findById(voteDTO.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post does not exist having id: "+voteDTO.getPostId()));
        User user = authService.getCurrentUser();
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, user);
        if(voteByPostAndUser.isPresent() && voteByPostAndUser.get().getVoteType().equals(voteDTO.getVoteType())){
            throw new SpringRedditException("You have already " + voteDTO.getVoteType() + "'d on this post");
        }

        if(VoteType.UPVOTE.equals(voteDTO.getVoteType())){
            post.setVoteCount(post.getVoteCount() + 1);
        }else{
            post.setVoteCount(post.getVoteCount() - 1);
        }
        Vote vote = voteRepository.save(voteMapper.voteDtoToVote(voteDTO, user, post));
        postRepository.save(post);
        return voteMapper.voteToDTO(vote);
    }
}
