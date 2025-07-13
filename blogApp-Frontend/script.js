// API Configuration
const API_BASE_URL = 'http://localhost:8080/api/v1';

// Global State
let currentUser = null;
let currentPage = 0;
let totalPages = 0;
let posts = [];

// DOM Elements
const loginBtn = document.getElementById('loginBtn');
const registerBtn = document.getElementById('registerBtn');
const profileBtn = document.getElementById('profileBtn');
const logoutBtn = document.getElementById('logoutBtn');
const createPostBtn = document.getElementById('createPostBtn');
const hamburger = document.querySelector('.hamburger');
const navMenu = document.querySelector('.nav-menu');

// Modal Elements
const loginModal = document.getElementById('loginModal');
const registerModal = document.getElementById('registerModal');
const createPostModal = document.getElementById('createPostModal');
const postDetailModal = document.getElementById('postDetailModal');

// Form Elements
const loginForm = document.getElementById('loginForm');
const registerForm = document.getElementById('registerForm');
const createPostForm = document.getElementById('createPostForm');

// Search Elements
const searchInput = document.getElementById('searchInput');
const searchBtn = document.getElementById('searchBtn');
const searchResults = document.getElementById('searchResults');

// Initialize the application
document.addEventListener('DOMContentLoaded', function() {
    checkBackendAvailability();
    initializeApp();
    setupEventListeners();
    loadPosts();
    loadStats();
});

// Check if backend is available
async function checkBackendAvailability() {
    try {
        const response = await fetch(`${API_BASE_URL}/posts?page=0&size=1`);
        if (!response.ok) {
            showError('Backend server is not available. Please check if the server is running.');
        }
    } catch (error) {
        showError('Cannot connect to backend server. Please ensure the server is running on localhost:8080');
    }
}

// Initialize application
function initializeApp() {
    // Check if user is logged in
    const token = localStorage.getItem('token');
    if (token) {
        try {
            currentUser = JSON.parse(localStorage.getItem('user'));
            updateAuthUI();
        } catch (error) {
            console.error('Error parsing user data:', error);
            localStorage.removeItem('token');
            localStorage.removeItem('user');
        }
    }
}

// Load statistics for home page
async function loadStats() {
    try {
        // For now, we'll use placeholder data since we don't have stats endpoints
        // In a real app, you'd call API endpoints to get actual statistics
        const stats = {
            totalPosts: 150,
            totalUsers: 89,
            totalComments: 342,
            totalLikes: 1250
        };
        
        // Animate the numbers
        animateNumber('totalPosts', stats.totalPosts);
        animateNumber('totalUsers', stats.totalUsers);
        animateNumber('totalComments', stats.totalComments);
        animateNumber('totalLikes', stats.totalLikes);
        
    } catch (error) {
        console.log('Stats loading failed:', error);
    }
}

// Animate number counting
function animateNumber(elementId, targetNumber) {
    const element = document.getElementById(elementId);
    if (!element) return;
    
    let currentNumber = 0;
    const increment = targetNumber / 50; // 50 steps
    const timer = setInterval(() => {
        currentNumber += increment;
        if (currentNumber >= targetNumber) {
            currentNumber = targetNumber;
            clearInterval(timer);
        }
        element.textContent = Math.floor(currentNumber);
    }, 30);
}

// Setup event listeners
function setupEventListeners() {
    // Navigation
    loginBtn.addEventListener('click', () => showModal(loginModal));
    registerBtn.addEventListener('click', () => showModal(registerModal));
    logoutBtn.addEventListener('click', logout);
    createPostBtn.addEventListener('click', () => showModal(createPostModal));
    profileBtn.addEventListener('click', showUserProfile);
    
    // Mobile menu
    hamburger.addEventListener('click', toggleMobileMenu);
    
    // Modal close buttons
    document.querySelectorAll('.close').forEach(closeBtn => {
        closeBtn.addEventListener('click', (e) => {
            const modal = e.target.closest('.modal');
            hideModal(modal);
        });
    });
    
    // Close modal when clicking outside
    window.addEventListener('click', (e) => {
        if (e.target.classList.contains('modal')) {
            hideModal(e.target);
        }
    });
    
    // Form submissions
    loginForm.addEventListener('submit', handleLogin);
    registerForm.addEventListener('submit', handleRegister);
    createPostForm.addEventListener('submit', handleCreatePost);
    
    // Edit form submissions
    const editProfileForm = document.getElementById('editProfileForm');
    const editPostForm = document.getElementById('editPostForm');
    
    if (editProfileForm) {
        editProfileForm.addEventListener('submit', handleEditProfile);
    }
    
    if (editPostForm) {
        editPostForm.addEventListener('submit', handleEditPost);
    }
    
    // Search
    searchBtn.addEventListener('click', handleSearch);
    searchInput.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') {
            handleSearch();
        }
    });
    
    // Navigation links
    document.querySelectorAll('.nav-link').forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            const target = e.target.getAttribute('href').substring(1);
            showSection(target);
        });
    });
}

// Mobile menu toggle
function toggleMobileMenu() {
    navMenu.classList.toggle('active');
    hamburger.classList.toggle('active');
}

// Show/hide modals
function showModal(modal) {
    modal.style.display = 'block';
    // Don't hide body overflow to allow scrolling within modal
}

function hideModal(modal) {
    modal.style.display = 'none';
    document.body.style.overflow = 'auto';
}

// Show section
function showSection(sectionId) {
    document.querySelectorAll('section').forEach(section => {
        section.style.display = 'none';
    });
    
    document.getElementById(sectionId).style.display = 'block';
    
    // Update active nav link
    document.querySelectorAll('.nav-link').forEach(link => {
        link.classList.remove('active');
    });
    document.querySelector(`[href="#${sectionId}"]`).classList.add('active');
}

// Update authentication UI
function updateAuthUI() {
    if (currentUser) {
        loginBtn.style.display = 'none';
        registerBtn.style.display = 'none';
        profileBtn.style.display = 'inline-block';
        logoutBtn.style.display = 'inline-block';
        createPostBtn.style.display = 'inline-block';
        
        // Update like button states when user logs in
        setTimeout(() => {
            initializeLikeButtonStates();
        }, 300);
    } else {
        loginBtn.style.display = 'inline-block';
        registerBtn.style.display = 'inline-block';
        profileBtn.style.display = 'none';
        logoutBtn.style.display = 'none';
        createPostBtn.style.display = 'none';
        
        // Reset like button states when user logs out (show like buttons, hide unlike buttons)
        document.querySelectorAll('.like-btn').forEach(btn => {
            btn.style.display = 'inline-block';
        });
        document.querySelectorAll('.unlike-btn').forEach(btn => {
            btn.style.display = 'none';
        });
    }
}

// API Functions
async function apiRequest(endpoint, options = {}, retryCount = 0, showLoadingSpinner = true) {
    const token = localStorage.getItem('token');
    
    const defaultOptions = {
        headers: {
            'Content-Type': 'application/json',
            ...(token && { 'Authorization': `Bearer ${token}` })
        }
    };
    
    const config = {
        ...defaultOptions,
        ...options,
        headers: {
            ...defaultOptions.headers,
            ...options.headers
        }
    };
    
    try {
        if (showLoadingSpinner) {
            showLoading();
        }
        console.log(`Making API request to: ${API_BASE_URL}${endpoint}`);
        console.log('Request config:', config);
        
        const response = await fetch(`${API_BASE_URL}${endpoint}`, config);
        
        console.log('Response status:', response.status);
        console.log('Response headers:', response.headers);
        
        if (response.status === 401) {
            console.log('Unauthorized - attempting token refresh');
            // Token expired, try to refresh
            const refreshed = await refreshToken();
            if (refreshed) {
                // Retry the original request with new token
                const newToken = localStorage.getItem('token');
                config.headers.Authorization = `Bearer ${newToken}`;
                const retryResponse = await fetch(`${API_BASE_URL}${endpoint}`, config);
                
                if (!retryResponse.ok) {
                    const errorData = await retryResponse.json();
                    throw new Error(errorData.message || 'An error occurred');
                }
                
                const data = await retryResponse.json();
                console.log('API Response after refresh:', data);
                return data.data || data;
            } else {
                // Refresh failed, redirect to login
                logout();
                throw new Error('Session expired. Please login again.');
            }
        }
        
        if (!response.ok) {
            const errorData = await response.json();
            console.error('API Error:', errorData);
            throw new Error(errorData.message || `HTTP ${response.status}: ${response.statusText}`);
        }
        
        // Handle 204 No Content responses (like unlike operations)
        if (response.status === 204) {
            console.log('API Response: 204 No Content');
            return { success: true, message: 'Operation completed successfully' };
        }
        
        const data = await response.json();
        console.log('API Response:', data);
        return data.data || data;
        
    } catch (error) {
        console.error('API Request failed:', error);
        throw error;
    } finally {
        if (showLoadingSpinner) {
            hideLoading();
        }
    }
}

// Refresh token function
async function refreshToken() {
    try {
        const refreshToken = localStorage.getItem('refreshToken');
        if (!refreshToken) {
            return false;
        }
        
        const response = await fetch(`${API_BASE_URL}/auth/refresh`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ refreshToken })
        });
        
        if (!response.ok) {
            return false;
        }
        
        const data = await response.json();
        
        // Update tokens
        localStorage.setItem('token', data.data.accessToken);
        localStorage.setItem('refreshToken', data.data.refreshToken);
        
        return true;
    } catch (error) {
        console.error('Token refresh failed:', error);
        return false;
    }
}

// Authentication Functions
async function handleLogin(e) {
    e.preventDefault();
    
    const usernameOrEmail = document.getElementById('loginEmail').value;
    const password = document.getElementById('loginPassword').value;
    
    try {
        const response = await apiRequest('/auth/login', {
            method: 'POST',
            body: JSON.stringify({ usernameOrEmail, password })
        });
        
        // Store tokens and user data
        localStorage.setItem('token', response.accessToken);
        localStorage.setItem('refreshToken', response.refreshToken);
        localStorage.setItem('user', JSON.stringify(response.user));
        
        currentUser = response.user;
        updateAuthUI();
        
        hideModal(loginModal);
        loginForm.reset();
        
        showSuccess('Login successful!');
        
        // Initialize like button states after login
        setTimeout(() => initializeLikeButtonStates(), 500);
        
    } catch (error) {
        showError('Login failed: ' + error.message);
    }
}

async function handleRegister(e) {
    e.preventDefault();
    
    const formData = {
        firstname: document.getElementById('registerFirstname').value,
        lastname: document.getElementById('registerLastname').value,
        username: document.getElementById('registerUsername').value,
        email: document.getElementById('registerEmail').value,
        password: document.getElementById('registerPassword').value,
        department: document.getElementById('registerDepartment').value,
        age: parseInt(document.getElementById('registerAge').value)
    };
    
    try {
        const response = await fetch(`${API_BASE_URL}/auth/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        });
        
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Registration failed');
        }
        
        const data = await response.json();
        
        hideModal(registerModal);
        registerForm.reset();
        
        showSuccess('Registration successful! Please login.');
        
    } catch (error) {
        showError('Registration failed: ' + error.message);
    }
}

function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('user');
    currentUser = null;
    updateAuthUI();
    showSuccess('Logged out successfully!');
    loadPosts(); // Refresh posts to show public content
}

async function showUserProfile() {
    try {
        console.log('Fetching user profile...');
        const userProfile = await apiRequest('/users/me');
        console.log('User profile received:', userProfile);
        
        const profileContent = document.getElementById('profileContent');
        profileContent.innerHTML = `
            <div class="profile-content">
                <div class="profile-header">
                    <div class="profile-avatar">
                        <i class="fas fa-user-circle"></i>
                    </div>
                    <h2>${userProfile.firstname} ${userProfile.lastname}</h2>
                    <p class="username">@${userProfile.username}</p>
                </div>
                <div class="profile-details">
                    <div class="detail-item">
                        <i class="fas fa-envelope"></i>
                        <span>${userProfile.email}</span>
                    </div>
                    <div class="detail-item">
                        <i class="fas fa-building"></i>
                        <span>${userProfile.department}</span>
                    </div>
                    <div class="detail-item">
                        <i class="fas fa-birthday-cake"></i>
                        <span>${userProfile.age} years old</span>
                    </div>
                </div>
                <div class="profile-actions">
                    <button class="btn btn-outline" onclick="showMyPosts()">My Posts</button>
                    <button class="btn btn-primary" onclick="editProfile()">Edit Profile</button>
                </div>
            </div>
        `;
        
        showModal(document.getElementById('profileModal'));
        
    } catch (error) {
        console.error('Profile loading error:', error);
        showError('Failed to load profile: ' + error.message);
    }
}

async function showMyPosts() {
    try {
        console.log('Fetching user posts...');
        const response = await apiRequest('/posts/me?page=0&size=10');
        console.log('User posts response:', response);
        
        const myPosts = response.items || [];
        
        // Create a new modal for my posts
        const postsModal = document.createElement('div');
        postsModal.className = 'modal';
        postsModal.innerHTML = `
            <div class="modal-content large">
                <span class="close">&times;</span>
                <h2>My Posts</h2>
                <div class="my-posts-list">
                    ${myPosts.length === 0 ? '<p>You haven\'t created any posts yet.</p>' : 
                    myPosts.map(post => `
                        <div class="my-post-item">
                            <h3>${escapeHtml(post.title)}</h3>
                            <p>${escapeHtml(post.text.substring(0, 100))}${post.text.length > 100 ? '...' : ''}</p>
                            <div class="post-stats">
                                <span><i class="fas fa-heart"></i> ${post.likeCount || 0}</span>
                                <span><i class="fas fa-comment"></i> ${post.commentCount || 0}</span>
                                <span><i class="fas fa-calendar"></i> ${formatDate(post.createdDate)}</span>
                            </div>
                            <div class="post-actions">
                                <button class="btn btn-small" onclick="editPost(${post.id})">Edit</button>
                                <button class="btn btn-small btn-danger" onclick="deletePost(${post.id})">Delete</button>
                            </div>
                        </div>
                    `).join('')}
                </div>
            </div>
        `;
        
        document.body.appendChild(postsModal);
        showModal(postsModal);
        
        // Add close functionality
        postsModal.querySelector('.close').addEventListener('click', () => {
            hideModal(postsModal);
            document.body.removeChild(postsModal);
        });
        
        // Close when clicking outside
        postsModal.addEventListener('click', (e) => {
            if (e.target === postsModal) {
                hideModal(postsModal);
                document.body.removeChild(postsModal);
            }
        });
        
    } catch (error) {
        console.error('My posts loading error:', error);
        showError('Failed to load your posts: ' + error.message);
    }
}

async function editProfile() {
    try {
        // Get current user profile
        const userProfile = await apiRequest('/users/me');
        
        // Fill the form with current data
        document.getElementById('editFirstname').value = userProfile.firstname;
        document.getElementById('editLastname').value = userProfile.lastname;
        document.getElementById('editUsername').value = userProfile.username;
        document.getElementById('editPassword').value = ''; // Don't show current password
        document.getElementById('editDepartment').value = userProfile.department;
        document.getElementById('editAge').value = userProfile.age;
        
        // Show the edit modal
        showModal(document.getElementById('editProfileModal'));
        
    } catch (error) {
        console.error('Error loading profile for edit:', error);
        showError('Failed to load profile data: ' + error.message);
    }
}

async function editPost(postId) {
    try {
        // Get post details
        const post = await apiRequest(`/posts/${postId}`);
        
        // Fill the form with current data
        document.getElementById('editPostId').value = postId;
        document.getElementById('editPostTitle').value = post.title;
        document.getElementById('editPostText').value = post.text;
        
        // Show the edit modal
        showModal(document.getElementById('editPostModal'));
        
    } catch (error) {
        console.error('Error loading post for edit:', error);
        showError('Failed to load post data: ' + error.message);
    }
}

function deletePost(postId) {
    if (confirm('Are you sure you want to delete this post?')) {
        apiRequest(`/posts/${postId}`, {
            method: 'DELETE'
        })
        .then(() => {
            showSuccess('Post deleted successfully!');
            // Close the modal and refresh posts
            const modal = document.querySelector('.modal');
            if (modal) {
                hideModal(modal);
                document.body.removeChild(modal);
            }
            loadPosts(currentPage);
        })
        .catch(error => {
            showError('Failed to delete post: ' + error.message);
        });
    }
}

// Posts Functions
async function loadPosts(page = 0) {
    try {
        const response = await apiRequest(`/posts?page=${page}&size=6`);
        posts = response.items; // Backend uses 'items' not 'content'
        currentPage = response.page; // Backend uses 'page' not 'number'
        totalPages = response.totalPages;
        
        displayPosts();
        displayPagination();
        
        // Update like button states after posts are displayed
        if (currentUser) {
            setTimeout(() => initializeLikeButtonStates(), 100);
        }
        
    } catch (error) {
        showError('Failed to load posts: ' + error.message);
    }
}

function displayPosts() {
    const postsGrid = document.getElementById('postsGrid');
    
    if (posts.length === 0) {
        postsGrid.innerHTML = '<p class="no-posts">No posts found.</p>';
        return;
    }
    
    postsGrid.innerHTML = posts.map(post => {
        return `
        <div class="post-card" onclick="showPostDetail(${post.id})">
            <div class="post-header">
                <h3 class="post-title">${escapeHtml(post.title)}</h3>
                <div class="post-meta">
                    <div class="post-author">
                        <i class="fas fa-user"></i>
                        <span onclick="event.stopPropagation(); ${post.authorId ? `showUserProfileById(${post.authorId})` : 'showError(\'Author information not available\')'}" style="cursor: pointer; color: #667eea; text-decoration: underline;">${escapeHtml(post.authorUser)}</span>
                    </div>
                    <div class="post-date">
                        <i class="fas fa-calendar"></i>
                        <span>${formatDate(post.createdDate)}</span>
                    </div>
                </div>
            </div>
            <div class="post-content">
                <p class="post-text">${escapeHtml(post.text.substring(0, 150))}${post.text.length > 150 ? '...' : ''}</p>
            </div>
            <div class="post-actions">
                <div class="action-buttons">
                    <button class="action-btn like-btn" onclick="event.stopPropagation(); event.preventDefault(); likePost(${post.id})" data-post-id="${post.id}" title="Like">
                        <i class="fas fa-heart"></i>
                        <span class="like-count">${post.likeCount || 0}</span>
                    </button>
                    <button class="action-btn" onclick="event.stopPropagation(); event.preventDefault(); showComments(${post.id})">
                        <i class="fas fa-comment"></i>
                        <span>${post.commentCount || 0}</span>
                    </button>
                </div>
            </div>
        </div>
    `;
    }).join('');
    
    // After displaying posts, update like button states if user is logged in
    if (currentUser) {
        setTimeout(() => {
            initializeLikeButtonStates();
        }, 100);
    }
}

function displayPagination() {
    const pagination = document.getElementById('pagination');
    
    if (totalPages <= 1) {
        pagination.innerHTML = '';
        return;
    }
    
    let paginationHTML = '';
    
    // Previous button
    if (currentPage > 0) {
        paginationHTML += `<button onclick="loadPosts(${currentPage - 1})">Previous</button>`;
    }
    
    // Page numbers
    for (let i = 0; i < totalPages; i++) {
        if (i === currentPage) {
            paginationHTML += `<button class="active">${i + 1}</button>`;
        } else {
            paginationHTML += `<button onclick="loadPosts(${i})">${i + 1}</button>`;
        }
    }
    
    // Next button
    if (currentPage < totalPages - 1) {
        paginationHTML += `<button onclick="loadPosts(${currentPage + 1})">Next</button>`;
    }
    
    pagination.innerHTML = paginationHTML;
}

async function showPostDetail(postId) {
    try {
        const post = await apiRequest(`/posts/${postId}`);
        
        const postDetailContent = document.getElementById('postDetailContent');
        postDetailContent.innerHTML = `
            <div class="post-detail">
                <div class="post-detail-header">
                    <h1 class="post-detail-title">${escapeHtml(post.title)}</h1>
                    <div class="post-detail-meta">
                        <span><i class="fas fa-user"></i> ${escapeHtml(post.authorUser)}</span>
                        <span><i class="fas fa-calendar"></i> ${formatDate(post.createdDate)}</span>
                        <span><i class="fas fa-heart"></i> ${post.likeCount || 0} likes</span>
                        <span><i class="fas fa-comment"></i> ${post.commentCount || 0} comments</span>
                    </div>
                </div>
                <div class="post-detail-content">
                    ${escapeHtml(post.text).replace(/\n/g, '<br>')}
                </div>
                <div class="comments-section">
                    <h3>Comments</h3>
                    <div id="commentsList">
                        <!-- Comments will be loaded here -->
                    </div>
                    ${currentUser ? `
                        <form id="commentForm" onsubmit="handleAddComment(event, ${postId})">
                            <div class="form-group">
                                <textarea placeholder="Write a comment..." required></textarea>
                            </div>
                            <button type="submit" class="btn btn-primary">Add Comment</button>
                        </form>
                    ` : '<p>Please login to add comments.</p>'}
                </div>
            </div>
        `;
        
        showModal(postDetailModal);
        
        // Load comments
        loadComments(postId);
        
    } catch (error) {
        showError('Failed to load post details: ' + error.message);
    }
}

async function handleCreatePost(e) {
    e.preventDefault();
    
    const title = document.getElementById('postTitle').value;
    const text = document.getElementById('postText').value;
    
    try {
        await apiRequest('/posts', {
            method: 'POST',
            body: JSON.stringify({ title, text })
        });
        
        hideModal(createPostModal);
        createPostForm.reset();
        
        showSuccess('Post created successfully!');
        loadPosts(currentPage);
        
    } catch (error) {
        showError('Failed to create post: ' + error.message);
    }
}

async function handleEditProfile(e) {
    e.preventDefault();
    
    const firstname = document.getElementById('editFirstname').value;
    const lastname = document.getElementById('editLastname').value;
    const username = document.getElementById('editUsername').value;
    const password = document.getElementById('editPassword').value;
    const department = document.getElementById('editDepartment').value;
    const age = parseInt(document.getElementById('editAge').value);
    
    try {
        await apiRequest(`/users/${currentUser.id}`, {
            method: 'PUT',
            body: JSON.stringify({
                firstname,
                lastname,
                username,
                password,
                department,
                age
            })
        });
        
        hideModal(document.getElementById('editProfileModal'));
        
        // Update current user data
        currentUser = { ...currentUser, firstname, lastname, username, department, age };
        localStorage.setItem('user', JSON.stringify(currentUser));
        
        showSuccess('Profile updated successfully!');
        
        // Refresh profile if it's currently open
        const profileModal = document.querySelector('.modal');
        if (profileModal && profileModal.id === 'profileModal') {
            showUserProfile();
        }
        
    } catch (error) {
        showError('Failed to update profile: ' + error.message);
    }
}

async function handleEditPost(e) {
    e.preventDefault();
    
    const postId = document.getElementById('editPostId').value;
    const title = document.getElementById('editPostTitle').value;
    const text = document.getElementById('editPostText').value;
    
    try {
        await apiRequest(`/posts/${postId}`, {
            method: 'PUT',
            body: JSON.stringify({ title, text })
        });
        
        hideModal(document.getElementById('editPostModal'));
        
        showSuccess('Post updated successfully!');
        
        // Refresh posts
        loadPosts(currentPage);
        
        // Close any open modals and refresh my posts if open
        const myPostsModal = document.querySelector('.modal');
        if (myPostsModal) {
            hideModal(myPostsModal);
            document.body.removeChild(myPostsModal);
        }
        
    } catch (error) {
        showError('Failed to update post: ' + error.message);
    }
}

async function likePost(postId) {
    if (!currentUser) {
        showError('Please login to like posts.');
        showModal(loginModal);
        return;
    }

    try {
        const likeBtn = document.querySelector(`[data-post-id="${postId}"].like-btn`);
        
        if (!likeBtn) {
            console.error('Like button not found for post:', postId);
            return;
        }
        
        // Like the post
        await apiRequest(`/likes/post/${postId}`, {
            method: 'POST',
            body: JSON.stringify({})
        }, 0, false);
        
        // Add animation
        likeBtn.classList.add('like-anim');
        setTimeout(() => likeBtn.classList.remove('like-anim'), 600);

        // Update like count from backend
        await refreshLikeCount(postId);
        
        // Update button state
        await updateLikeButtonState(postId);

    } catch (error) {
        console.error('Like error:', error);
        
        if (error.message.includes('already liked')) {
            showError('You have already liked this post');
        } else {
            showError('Failed to like post: ' + error.message);
        }
    }
}

// Unlike function removed - only like functionality is available

async function handleAddComment(e, postId) {
    e.preventDefault();
    
    const form = e.target;
    const text = form.querySelector('textarea').value;
    
    try {
        await apiRequest(`/comments/post/${postId}`, {
            method: 'POST',
            body: JSON.stringify({ text })
        });
        
        form.reset();
        loadComments(postId);
        showSuccess('Comment added successfully!');
        
    } catch (error) {
        showError('Failed to add comment: ' + error.message);
    }
}

async function handleSearch() {
    const query = searchInput.value.trim();
    if (!query) {
        showError('Please enter a search term.');
        return;
    }
    
    try {
        const response = await apiRequest(`/search?query=${encodeURIComponent(query)}&type=all&page=0&size=10`);
        // SearchResultResponse contains blogPosts and users, we'll use blogPosts for now
        displaySearchResults(response.blogPosts || []);
    } catch (error) {
        showError('Search failed: ' + error.message);
    }
}

function displaySearchResults(results) {
    const searchResultsDiv = document.getElementById('searchResults');
    
    if (!results || results.length === 0) {
        searchResultsDiv.innerHTML = '<p>No results found.</p>';
        return;
    }
    
    searchResultsDiv.innerHTML = results.map(result => `
        <div class="post-card">
            <h3>${escapeHtml(result.title)}</h3>
            <p>${escapeHtml(result.text.substring(0, 200))}...</p>
            <div class="post-meta">
                <span><i class="fas fa-user"></i> ${escapeHtml(result.authorUser)}</span>
                <span><i class="fas fa-calendar"></i> ${formatDate(result.createdDate)}</span>
            </div>
        </div>
    `).join('');
}

// Initialize like button states for all posts
async function initializeLikeButtonStates() {
    if (!currentUser) {
        return;
    }
    
    try {
        // Get all posts and their like states
        for (const post of posts) {
            await updateLikeButtonState(post.id);
        }
    } catch (error) {
        console.error('Failed to initialize like button states:', error);
    }
}

// Update like button state for a specific post
async function updateLikeButtonState(postId) {
    try {
        const likeBtn = document.querySelector(`[data-post-id="${postId}"].like-btn`);

        if (!likeBtn) {
            return;
        }
        
        if (!currentUser) {
            // If user is not logged in, show like button
            likeBtn.style.display = 'inline-block';
            return;
        }
        
        // Get all likes for this post to check if user liked it
        const likes = await apiRequest(`/likes/post/${postId}`, {}, 0, false);
        
        // Check if current user has liked this post
        const userLiked = likes.some(like => like.userId === currentUser.id);
        
        if (userLiked) {
            // User has liked the post - disable like button
            likeBtn.disabled = true;
            likeBtn.style.opacity = '0.5';
            likeBtn.title = 'Already liked';
        } else {
            // User has not liked the post - enable like button
            likeBtn.disabled = false;
            likeBtn.style.opacity = '1';
            likeBtn.title = 'Like';
        }
    } catch (error) {
        console.error('Failed to update like button state:', error);
        // If there's an error, enable like button (default state)
        const likeBtn = document.querySelector(`[data-post-id="${postId}"].like-btn`);
        if (likeBtn) {
            likeBtn.disabled = false;
            likeBtn.style.opacity = '1';
            likeBtn.title = 'Like';
        }
    }
}

// Update all like button states
async function updateAllLikeButtonStates() {
    if (!currentUser) return;
    
    for (const post of posts) {
        await updateLikeButtonState(post.id);
    }
}

// Refresh like count for a specific post
async function refreshLikeCount(postId) {
    try {
        // Get post details which includes the updated likeCount
        const post = await apiRequest(`/posts/${postId}`, {}, 0, false);
        const likeCount = post.likeCount || 0;
        
        const likeBtn = document.querySelector(`[data-post-id="${postId}"].like-btn`);
        
        if (likeBtn) {
            const countElement = likeBtn.querySelector('.like-count');
            if (countElement) countElement.textContent = likeCount;
        }
    } catch (error) {
        console.error('Failed to refresh like count:', error);
    }
}

// Update like count immediately without API call
function updateLikeCount(postId, change) {
    const likeBtn = document.querySelector(`[data-post-id="${postId}"].like-btn`);
    const unlikeBtn = document.querySelector(`[data-post-id="${postId}"].unlike-btn`);
    
    if (likeBtn) {
        const likeCount = likeBtn.querySelector('.like-count');
        if (likeCount) {
            const currentCount = parseInt(likeCount.textContent) || 0;
            likeCount.textContent = Math.max(0, currentCount + change);
        }
    }
    
    if (unlikeBtn) {
        const likeCount = unlikeBtn.querySelector('.like-count');
        if (likeCount) {
            const currentCount = parseInt(likeCount.textContent) || 0;
            likeCount.textContent = Math.max(0, currentCount + change);
        }
    }
}

// Refresh like count after like/unlike operation
async function refreshLikeCountAfterOperation(postId) {
    try {
        await refreshLikeCount(postId);
    } catch (error) {
        console.error('Failed to refresh like count after operation:', error);
    }
}

// Utility Functions
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
    });
}

function showLoading() {
    const spinner = document.getElementById('loadingSpinner');
    if (spinner) {
        spinner.style.display = 'flex';
    }
}

function hideLoading() {
    const spinner = document.getElementById('loadingSpinner');
    if (spinner) {
        spinner.style.display = 'none';
    }
}

function showSuccess(message) {
    showNotification(message, 'success');
}

function showError(message) {
    showNotification(message, 'error');
}

function showNotification(message, type) {
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.textContent = message;
    
    document.body.appendChild(notification);
    
    setTimeout(() => {
        notification.remove();
    }, 5000);
}

// Load comments for a post
async function loadComments(postId) {
    try {
        const comments = await apiRequest(`/comments/post/${postId}`);
        const commentsList = document.getElementById('commentsList');
        
        if (!comments || comments.length === 0) {
            commentsList.innerHTML = '<p>No comments yet. Be the first to comment!</p>';
            return;
        }
        
        commentsList.innerHTML = comments.map(comment => `
            <div class="comment">
                <div class="comment-header">
                    <span class="comment-author">${escapeHtml(comment.authorUser)}</span>
                    <span class="comment-date">${formatDate(comment.createdDate)}</span>
                </div>
                <div class="comment-text">${escapeHtml(comment.text)}</div>
            </div>
        `).join('');
        
    } catch (error) {
        console.error('Failed to load comments:', error);
        const commentsList = document.getElementById('commentsList');
        commentsList.innerHTML = '<p>Failed to load comments.</p>';
    }
}

// Global functions for onclick handlers
window.showPosts = () => showSection('posts');
window.showRegisterModal = () => showModal(registerModal);
window.showPostDetail = showPostDetail;
window.likePost = likePost;
window.showComments = (postId) => showPostDetail(postId);
window.handleAddComment = handleAddComment;
window.showUserProfile = showUserProfile;
window.showUserProfileById = showUserProfileById;
window.showMyPosts = showMyPosts;
window.showUserPostsById = showUserPostsById;
window.editProfile = editProfile;
window.editPost = editPost;
window.deletePost = deletePost;

async function showUserProfileById(userId) {
    try {
        console.log('Fetching user profile for ID:', userId);
        const userProfile = await apiRequest(`/users/${userId}`);
        console.log('User profile received:', userProfile);
        
        const profileContent = document.getElementById('profileContent');
        profileContent.innerHTML = `
            <div class="profile-content">
                <div class="profile-header">
                    <div class="profile-avatar">
                        <i class="fas fa-user-circle"></i>
                    </div>
                    <h2>${userProfile.firstname} ${userProfile.lastname}</h2>
                    <p class="username">@${userProfile.username}</p>
                </div>
                <div class="profile-details">
                    <div class="detail-item">
                        <i class="fas fa-envelope"></i>
                        <span>${userProfile.email}</span>
                    </div>
                    <div class="detail-item">
                        <i class="fas fa-building"></i>
                        <span>${userProfile.department}</span>
                    </div>
                    <div class="detail-item">
                        <i class="fas fa-birthday-cake"></i>
                        <span>${userProfile.age} years old</span>
                    </div>
                </div>
                <div class="profile-actions">
                    <button class="btn btn-outline" onclick="showUserPostsById(${userId})">View Posts</button>
                    ${currentUser && currentUser.id === userId ? `
                        <button class="btn btn-primary" onclick="editProfile()">Edit Profile</button>
                    ` : ''}
                </div>
            </div>
        `;
        
        showModal(document.getElementById('profileModal'));
        
    } catch (error) {
        console.error('Profile loading error:', error);
        showError('Failed to load profile: ' + error.message);
    }
}

async function showUserPostsById(userId) {
    try {
        console.log('Fetching posts for user ID:', userId);
        const response = await apiRequest(`/posts/user/${userId}?page=0&size=10`);
        console.log('User posts response:', response);
        
        const userPosts = response.items || [];
        
        // Get user profile to show name in modal
        const userProfile = await apiRequest(`/users/${userId}`);
        
        // Create a new modal for user posts
        const postsModal = document.createElement('div');
        postsModal.className = 'modal';
        postsModal.innerHTML = `
            <div class="modal-content large">
                <span class="close">&times;</span>
                <h2>${userProfile.firstname} ${userProfile.lastname}'s Posts</h2>
                <div class="my-posts-list">
                    ${userPosts.length === 0 ? '<p>This user hasn\'t created any posts yet.</p>' : 
                    userPosts.map(post => `
                        <div class="my-post-item">
                            <h3>${escapeHtml(post.title)}</h3>
                            <p>${escapeHtml(post.text.substring(0, 100))}${post.text.length > 100 ? '...' : ''}</p>
                            <div class="post-stats">
                                <span><i class="fas fa-heart"></i> ${post.likeCount || 0}</span>
                                <span><i class="fas fa-comment"></i> ${post.commentCount || 0}</span>
                                <span><i class="fas fa-calendar"></i> ${formatDate(post.createdDate)}</span>
                            </div>
                            ${currentUser && currentUser.id === userId ? `
                                <div class="post-actions">
                                    <button class="btn btn-small" onclick="editPost(${post.id})">Edit</button>
                                    <button class="btn btn-small btn-danger" onclick="deletePost(${post.id})">Delete</button>
                                </div>
                            ` : ''}
                        </div>
                    `).join('')}
                </div>
            </div>
        `;
        
        document.body.appendChild(postsModal);
        showModal(postsModal);
        
        // Add close functionality
        postsModal.querySelector('.close').addEventListener('click', () => {
            hideModal(postsModal);
            document.body.removeChild(postsModal);
        });
        
        // Close when clicking outside
        postsModal.addEventListener('click', (e) => {
            if (e.target === postsModal) {
                hideModal(postsModal);
                document.body.removeChild(postsModal);
            }
        });
        
    } catch (error) {
        console.error('User posts loading error:', error);
        showError('Failed to load user posts: ' + error.message);
    }
}