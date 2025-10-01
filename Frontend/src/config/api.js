// API Configuration
const getApiBaseUrl = () => {
  // Use environment variable if available (Vite)
  if (import.meta.env.VITE_API_URL) {
    return import.meta.env.VITE_API_URL;
  }
  
  // Check if we're in development (localhost)
  if (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1') {
    return 'http://localhost:8081/api';
  }
  
  // In production/Replit, use relative path or same origin
  // Backend should be on the same host
  return '/api';
};

export const API_BASE_URL = getApiBaseUrl();
