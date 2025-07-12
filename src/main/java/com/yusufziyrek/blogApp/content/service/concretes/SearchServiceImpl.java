package com.yusufziyrek.blogApp.content.service.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.yusufziyrek.blogApp.content.dto.responses.GetAllPostsResponse;
import com.yusufziyrek.blogApp.content.dto.responses.SearchResultResponse;
import com.yusufziyrek.blogApp.content.repo.IPostRepository;
import com.yusufziyrek.blogApp.content.service.abstracts.ISearchService;
import com.yusufziyrek.blogApp.identity.dto.responses.GetByIdUserResponse;
import com.yusufziyrek.blogApp.identity.repo.IUserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchServiceImpl implements ISearchService {

    private final IPostRepository postRepository;
    private final IUserRepository userRepository;

    @Override
    @Cacheable(value = "searchResults", key = "#query + '-' + #type + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public SearchResultResponse search(String query, String type, Pageable pageable) {
        log.debug("Searching for query: '{}' with type: '{}' and pagination: {}", query, type, pageable);
        
        SearchResultResponse result = new SearchResultResponse();

        if (type.equalsIgnoreCase("blog") || type.equalsIgnoreCase("all")) {
            List<GetAllPostsResponse> postDTOs = postRepository
                .searchPosts(query, pageable)
                .getContent().stream()
                .map(this::mapToGetAllPostsResponse)
                .collect(Collectors.toList());
            result.setBlogPosts(postDTOs);
            log.debug("Found {} blog posts for query: '{}'", postDTOs.size(), query);
        }

        if (type.equalsIgnoreCase("user") || type.equalsIgnoreCase("all")) {
            List<GetByIdUserResponse> userDTOs = userRepository
                .findByUsernameContainingIgnoreCaseOrFirstnameContainingIgnoreCaseOrLastnameContainingIgnoreCase(
                    query, query, query, pageable)
                .getContent().stream()
                .map(this::mapToGetByIdUserResponse)
                .collect(Collectors.toList());
            result.setUsers(userDTOs);
            log.debug("Found {} users for query: '{}'", userDTOs.size(), query);
        }

        return result;
    }

    // Helper methods for mapping
    private GetAllPostsResponse mapToGetAllPostsResponse(com.yusufziyrek.blogApp.content.domain.models.Post post) {
        return GetAllPostsResponse.builder()
            .id(post.getId())
            .authorUser(post.getUser().getUsername())
            .title(post.getTitle())
            .commentCount(post.getCommentCount())
            .likeCount(post.getLikeCount())
            .createdDate(post.getCreatedDate())
            .build();
    }

    private GetByIdUserResponse mapToGetByIdUserResponse(com.yusufziyrek.blogApp.identity.domain.models.User user) {
        return GetByIdUserResponse.builder()
            .id(user.getId())
            .username(user.getUsername())
            .firstname(user.getFirstname())
            .lastname(user.getLastname())
            .department(user.getDepartment())
            .age(user.getAge())
            .titles(null)
            .build();
    }
}