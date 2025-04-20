package com.yusufziyrek.blogApp.content.service.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.yusufziyrek.blogApp.content.dto.responses.GetAllPostsResponse;
import com.yusufziyrek.blogApp.content.dto.responses.SearchResultResponse;
import com.yusufziyrek.blogApp.content.repo.IPostRepository;
import com.yusufziyrek.blogApp.content.service.abstracts.ISearchService;
import com.yusufziyrek.blogApp.identity.dto.responses.GetByIdUserResponse;
import com.yusufziyrek.blogApp.identity.repo.IUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements ISearchService {

    private final IPostRepository postRepository;
    private final IUserRepository userRepository;

    public SearchResultResponse search(String query, String type, Pageable pageable) {
        SearchResultResponse result = new SearchResultResponse();
        
        if (type.equalsIgnoreCase("blog") || type.equalsIgnoreCase("all")) {
            List<GetAllPostsResponse> postDTOs = postRepository
                    .findByTitleContainingIgnoreCaseOrTextContainingIgnoreCase(query, query, pageable)
                    .getContent().stream()
                    .map(post -> new GetAllPostsResponse(
                        post.getId(),
                        post.getUser().getUsername(),  
                        post.getTitle(),
                        post.getCommentCount(),
                        post.getLikeCount(),
                        post.getCreatedDate()))
                    .collect(Collectors.toList());
            result.setBlogPosts(postDTOs);
        }
        
        if (type.equalsIgnoreCase("user") || type.equalsIgnoreCase("all")) {
            List<GetByIdUserResponse> userDTOs = userRepository
                    .findByUsernameContainingIgnoreCaseOrFirstnameContainingIgnoreCaseOrLastnameContainingIgnoreCase(
                        query, query, query, pageable)
                    .getContent().stream()
                    .map(user -> new GetByIdUserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getFirstname(),
                        user.getLastname(),
                        user.getDepartment(),
                        user.getAge(),
                        null))
                    .collect(Collectors.toList());
            result.setUsers(userDTOs);
        }
        
        return result;
    }
}
