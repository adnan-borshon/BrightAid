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
      const projectsRes = await fetch(`${API_BASE_URL}/school-projects`);
      if (projectsRes.ok) {
        const allProjects = await projectsRes.json();
        const ngoProjects = Array.isArray(allProjects) ? allProjects.filter(p => p.ngoId == ngoId) : [];
        setProjectsData(ngoProjects);
      }
      
      // Fetch NGO donations
      const [studentDonationsRes, projectDonationsRes] = await Promise.all([
        fetch(`${API_BASE_URL}/ngo-student-donations`),
        fetch(`${API_BASE_URL}/ngo-project-donations`)
      ]);
      
      let allDonations = [];
      if (studentDonationsRes.ok) {
        const studentDonations = await studentDonationsRes.json();
        const ngoStudentDonations = Array.isArray(studentDonations) ? studentDonations.filter(d => d.ngoId == ngoId) : [];
        allDonations = [...allDonations, ...ngoStudentDonations];
      }
      
      if (projectDonationsRes.ok) {
        const projectDonations = await projectDonationsRes.json();
        const ngoProjectDonations = Array.isArray(projectDonations) ? projectDonations.filter(d => d.ngoId == ngoId) : [];
        allDonations = [...allDonations, ...ngoProjectDonations];
      }
      
      setDonationsData(allDonations);
      
      // Fetch gamification data
      const gamificationRes = await fetch(`${API_BASE_URL}/ngo-gamification`);
      if (gamificationRes.ok) {
        const allGamificationData = await gamificationRes.json();
        const ngoGamification = Array.isArray(allGamificationData) 
          ? allGamificationData.find(g => g.ngoId == ngoId)
          : null;
        setGamificationData(ngoGamification);
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