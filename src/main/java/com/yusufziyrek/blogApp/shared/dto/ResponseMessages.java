package com.yusufziyrek.blogApp.shared.dto;

public final class ResponseMessages {
    
    // Auth ile ilgili mesajlar
    public static final String USER_REGISTERED_SUCCESSFULLY = "UserDomain registered successfully";
    public static final String LOGIN_SUCCESSFUL = "Login successful";
    public static final String ACCOUNT_VERIFICATION_SUCCESSFUL = "Account verification successful";
    public static final String TOKEN_REFRESHED_SUCCESSFULLY = "Token refreshed successfully";
    
    // UserDomain ile ilgili mesajlar
    public static final String USERS_RETRIEVED_SUCCESSFULLY = "Users retrieved successfully";
    public static final String USER_RETRIEVED_SUCCESSFULLY = "UserDomain retrieved successfully";
    public static final String USER_UPDATED_SUCCESSFULLY = "UserDomain updated successfully";
    public static final String USER_DELETED_SUCCESSFULLY = "UserDomain deleted successfully";
    
    // PostDomain ile ilgili mesajlar
    public static final String POSTS_RETRIEVED_SUCCESSFULLY = "Posts retrieved successfully";
    public static final String POST_RETRIEVED_SUCCESSFULLY = "PostDomain retrieved successfully";
    public static final String POST_CREATED_SUCCESSFULLY = "PostDomain created successfully";
    public static final String POST_UPDATED_SUCCESSFULLY = "PostDomain updated successfully";
    public static final String POST_DELETED_SUCCESSFULLY = "PostDomain deleted successfully";
    
    // CommentDomain ile ilgili mesajlar
    public static final String COMMENTS_FOR_POST_RETRIEVED_SUCCESSFULLY = "Comments for post retrieved successfully";
    public static final String COMMENTS_FOR_USER_RETRIEVED_SUCCESSFULLY = "Comments for user retrieved successfully";
    public static final String COMMENT_RETRIEVED_SUCCESSFULLY = "CommentDomain retrieved successfully";
    public static final String COMMENT_ADDED_SUCCESSFULLY = "CommentDomain added successfully";
    public static final String COMMENT_UPDATED_SUCCESSFULLY = "CommentDomain updated successfully";
    public static final String COMMENT_DELETED_SUCCESSFULLY = "CommentDomain deleted successfully";
    
    // LikeDomain ile ilgili mesajlar
    public static final String LIKES_FOR_POST_RETRIEVED_SUCCESSFULLY = "Likes for post retrieved successfully";
    public static final String LIKES_FOR_COMMENT_RETRIEVED_SUCCESSFULLY = "Likes for comment retrieved successfully";
    public static final String LIKE_RETRIEVED_SUCCESSFULLY = "LikeDomain retrieved successfully";
    public static final String LIKE_FOR_POST_ADDED_SUCCESSFULLY = "LikeDomain for post added successfully";
    public static final String LIKE_FOR_COMMENT_ADDED_SUCCESSFULLY = "LikeDomain for comment added successfully";
    public static final String LIKE_REMOVED_FROM_POST_SUCCESSFULLY = "LikeDomain removed from post successfully";
    public static final String LIKE_REMOVED_FROM_COMMENT_SUCCESSFULLY = "LikeDomain removed from comment successfully";
    public static final String LIKE_DELETED_SUCCESSFULLY = "LikeDomain deleted successfully";
    
    // Arama ile ilgili mesajlar
    public static final String SEARCH_COMPLETED_SUCCESSFULLY = "Search completed successfully";
    
    private ResponseMessages() {
    // Yardımcı sınıf - örneklenmesin
    }
} 