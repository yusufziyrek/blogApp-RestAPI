package com.yusufziyrek.blogApp.shared.dto;

public final class ResponseMessages {
    
    // Auth related messages
    public static final String USER_REGISTERED_SUCCESSFULLY = "User registered successfully";
    public static final String LOGIN_SUCCESSFUL = "Login successful";
    public static final String ACCOUNT_VERIFICATION_SUCCESSFUL = "Account verification successful";
    public static final String TOKEN_REFRESHED_SUCCESSFULLY = "Token refreshed successfully";
    
    // User related messages
    public static final String USERS_RETRIEVED_SUCCESSFULLY = "Users retrieved successfully";
    public static final String USER_RETRIEVED_SUCCESSFULLY = "User retrieved successfully";
    public static final String USER_UPDATED_SUCCESSFULLY = "User updated successfully";
    public static final String USER_DELETED_SUCCESSFULLY = "User deleted successfully";
    
    // Post related messages
    public static final String POSTS_RETRIEVED_SUCCESSFULLY = "Posts retrieved successfully";
    public static final String POST_RETRIEVED_SUCCESSFULLY = "Post retrieved successfully";
    public static final String POST_CREATED_SUCCESSFULLY = "Post created successfully";
    public static final String POST_UPDATED_SUCCESSFULLY = "Post updated successfully";
    public static final String POST_DELETED_SUCCESSFULLY = "Post deleted successfully";
    
    // Comment related messages
    public static final String COMMENTS_FOR_POST_RETRIEVED_SUCCESSFULLY = "Comments for post retrieved successfully";
    public static final String COMMENTS_FOR_USER_RETRIEVED_SUCCESSFULLY = "Comments for user retrieved successfully";
    public static final String COMMENT_RETRIEVED_SUCCESSFULLY = "Comment retrieved successfully";
    public static final String COMMENT_ADDED_SUCCESSFULLY = "Comment added successfully";
    public static final String COMMENT_UPDATED_SUCCESSFULLY = "Comment updated successfully";
    public static final String COMMENT_DELETED_SUCCESSFULLY = "Comment deleted successfully";
    
    // Like related messages
    public static final String LIKES_FOR_POST_RETRIEVED_SUCCESSFULLY = "Likes for post retrieved successfully";
    public static final String LIKES_FOR_COMMENT_RETRIEVED_SUCCESSFULLY = "Likes for comment retrieved successfully";
    public static final String LIKE_RETRIEVED_SUCCESSFULLY = "Like retrieved successfully";
    public static final String LIKE_FOR_POST_ADDED_SUCCESSFULLY = "Like for post added successfully";
    public static final String LIKE_FOR_COMMENT_ADDED_SUCCESSFULLY = "Like for comment added successfully";
    public static final String LIKE_REMOVED_FROM_POST_SUCCESSFULLY = "Like removed from post successfully";
    public static final String LIKE_REMOVED_FROM_COMMENT_SUCCESSFULLY = "Like removed from comment successfully";
    public static final String LIKE_DELETED_SUCCESSFULLY = "Like deleted successfully";
    
    // Search related messages
    public static final String SEARCH_COMPLETED_SUCCESSFULLY = "Search completed successfully";
    
    private ResponseMessages() {
        // Utility class, prevent instantiation
    }
} 