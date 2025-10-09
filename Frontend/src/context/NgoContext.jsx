import React, { createContext, useContext, useState, useEffect } from 'react';

const NgoContext = createContext();

export const useNgo = () => {
  const context = useContext(NgoContext);
  if (!context) {
    throw new Error('useNgo must be used within an NgoProvider');
  }
  return context;
};

export const NgoProvider = ({ children }) => {
  const [ngoData, setNgoData] = useState(null);
  const [projectsData, setProjectsData] = useState([]);
  const [donationsData, setDonationsData] = useState([]);
  const [gamificationData, setGamificationData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchNgoData = async (ngoId) => {
    if (!ngoId) return;
    
    setLoading(true);
    setError(null);
    
    try {
      const API_BASE_URL = 'http://localhost:8081/api';
      
      // Fetch NGO basic data
      const ngoRes = await fetch(`${API_BASE_URL}/ngos/${ngoId}`);
      if (ngoRes.ok) {
        const ngo = await ngoRes.json();
        setNgoData(ngo);
      }
      
      // Fetch NGO projects
      const projectsRes = await fetch(`${API_BASE_URL}/ngo-projects`);
      if (projectsRes.ok) {
        const allProjects = await projectsRes.json();
        const ngoProjects = Array.isArray(allProjects) ? allProjects.filter(p => p.ngoId == ngoId) : [];
        setProjectsData(ngoProjects);
      }
      
      // Fetch NGO donations using specific NGO endpoints
      const [studentDonationsRes, projectDonationsRes] = await Promise.all([
        fetch(`${API_BASE_URL}/ngo-student-donations/ngo/${ngoId}`),
        fetch(`${API_BASE_URL}/ngo-project-donations/ngo/${ngoId}`)
      ]);
      
      let allDonations = [];
      if (studentDonationsRes.ok) {
        const studentDonations = await studentDonationsRes.json();
        allDonations = [...allDonations, ...(Array.isArray(studentDonations) ? studentDonations : [])];
      }
      
      if (projectDonationsRes.ok) {
        const projectDonations = await projectDonationsRes.json();
        allDonations = [...allDonations, ...(Array.isArray(projectDonations) ? projectDonations : [])];
      }
      
      console.log('Fetched donations data:', allDonations);
      setDonationsData(allDonations);
      
      // Fetch gamification data for specific NGO using dedicated endpoint
      try {
        const gamificationRes = await fetch(`${API_BASE_URL}/ngo-gamification/ngo/${ngoId}`);
        if (gamificationRes.ok) {
          const ngoGamification = await gamificationRes.json();
          
          // Calculate additional fields for frontend
          const totalPoints = ngoGamification.totalPoints || 0;
          const pointsToNextLevel = calculatePointsToNextLevel(totalPoints);
          const progressPercentage = calculateProgressPercentage(totalPoints);
          
          setGamificationData({
            ...ngoGamification,
            pointsToNextLevel,
            progressPercentage
          });
        } else {
          // Fallback to default data
          setGamificationData({
            ngoId: parseInt(ngoId),
            totalPoints: 0,
            badgesEarned: '["New NGO"]',
            pointsToNextLevel: 100,
            progressPercentage: 0
          });
        }
      } catch (gamificationError) {
        console.warn('Failed to fetch gamification data:', gamificationError);
        // Set default gamification data
        setGamificationData({
          ngoId: parseInt(ngoId),
          totalPoints: 0,
          badgesEarned: '["New NGO"]',
          pointsToNextLevel: 100,
          progressPercentage: 0
        });
      }
      
    } catch (err) {
      console.error('Error fetching NGO data:', err);
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const refreshData = (ngoId) => {
    fetchNgoData(ngoId);
  };

  const updateProjectsData = (newProjects) => {
    setProjectsData(newProjects);
  };

  const updateDonationsData = (newDonations) => {
    setDonationsData(newDonations);
  };

  // Helper functions for gamification calculations
  const calculatePointsToNextLevel = (currentPoints) => {
    const levels = [100, 200, 500, 1000, 2000];
    for (let level of levels) {
      if (currentPoints < level) {
        return level - currentPoints;
      }
    }
    return 0; // Max level reached
  };

  const calculateProgressPercentage = (currentPoints) => {
    const levels = [0, 100, 200, 500, 1000, 2000];
    for (let i = 1; i < levels.length; i++) {
      if (currentPoints < levels[i]) {
        const prevLevel = levels[i - 1];
        const nextLevel = levels[i];
        const progress = ((currentPoints - prevLevel) / (nextLevel - prevLevel)) * 100;
        return Math.min(Math.max(progress, 0), 100);
      }
    }
    return 100; // Max level reached
  };

  const value = {
    ngoData,
    projectsData,
    donationsData,
    gamificationData,
    loading,
    error,
    fetchNgoData,
    refreshData,
    updateProjectsData,
    updateDonationsData,
    setNgoData,
    setProjectsData,
    setDonationsData,
    setGamificationData
  };

  return (
    <NgoContext.Provider value={value}>
      {children}
    </NgoContext.Provider>
  );
};