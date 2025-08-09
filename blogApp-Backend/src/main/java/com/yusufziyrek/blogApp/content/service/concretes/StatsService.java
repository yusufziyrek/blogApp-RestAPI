package com.yusufziyrek.blogApp.content.service.concretes;

import com.yusufziyrek.blogApp.content.repo.IPostRepository;
import com.yusufziyrek.blogApp.content.repo.ICommentRepository;
import com.yusufziyrek.blogApp.content.repo.ILikeRepository;
import com.yusufziyrek.blogApp.identity.repo.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final IPostRepository postRepository;
    private final IUserRepository userRepository;
    private final ICommentRepository commentRepository;
    private final ILikeRepository likeRepository;

    public Map<String, Long> getStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalPosts", postRepository.count());
        stats.put("totalUsers", userRepository.count());
        stats.put("totalComments", commentRepository.count());
        stats.put("totalLikes", likeRepository.count());
        return stats;
    }
} 