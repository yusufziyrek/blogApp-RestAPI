package com.yusufziyrek.blogApp.shared.exception;

public final class ErrorMessages {
    
    // User related messages
    public static final String USER_NOT_FOUND_BY_ID = "User not found. ID: %d";
    public static final String USER_NOT_FOUND_BY_USERNAME = "User not found. Username: %s";
    public static final String USERNAME_ALREADY_EXISTS = "Username already exists: %s";
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists: %s";
    public static final String USER_ACCOUNT_NOT_VERIFIED = "Your account is not verified yet. Please check your email for the activation link.";
    
    // Post related messages
    public static final String POST_NOT_FOUND_BY_ID = "Post not found. ID: %d";
    public static final String POST_ACCESS_DENIED_UPDATE = "You are not allowed to update this post.";
    public static final String POST_ACCESS_DENIED_DELETE = "You are not allowed to delete this post.";
    
    // Comment related messages
    public static final String COMMENT_NOT_FOUND_BY_ID = "Comment not found. ID: %d";
    public static final String COMMENT_ACCESS_DENIED_UPDATE = "You are not allowed to update this comment.";
    public static final String COMMENT_ACCESS_DENIED_DELETE = "You are not allowed to delete this comment.";
    
    // Like related messages
    public static final String LIKE_NOT_FOUND_BY_ID = "Like not found. ID: %d";
    public static final String LIKE_ALREADY_EXISTS_FOR_POST = "You have already liked this post.";
    public static final String LIKE_ALREADY_EXISTS_FOR_COMMENT = "You have already liked this comment.";
    public static final String LIKE_ACCESS_DENIED_DELETE = "You are not allowed to remove this like.";
    
    // Authentication related messages
    public static final String INVALID_CREDENTIALS = "Invalid username or password.";
    public static final String USER_NOT_FOUND_BY_EMAIL = "User not found with email: %s";
    public static final String REFRESH_TOKEN_NOT_FOUND = "Refresh token not found.";
    public static final String REFRESH_TOKEN_EXPIRED = "Refresh token expired. Please login again.";
    public static final String TOKEN_EXPIRED = "Token expired.";
    public static final String INVALID_TOKEN = "Invalid token.";
    
    // Validation messages
    public static final String VALIDATION_FAILED = "Validation failed";
    public static final String FIELD_CANNOT_BE_EMPTY = "%s field cannot be empty";
    public static final String FIELD_LENGTH_INVALID = "%s field must be between %d-%d characters";
    public static final String INVALID_EMAIL_FORMAT = "Invalid email format";
    public static final String AGE_RANGE_INVALID = "Age must be between 18-100";
    
    // General messages
    public static final String INTERNAL_SERVER_ERROR = "Internal server error occurred. Please try again later.";
    public static final String ACCESS_DENIED = "You don't have permission for this operation.";
    public static final String RESOURCE_NOT_FOUND = "Requested resource not found.";
    
    private ErrorMessages() {
        // Utility class, prevent instantiation
    }
} 