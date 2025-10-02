import React, { createContext, useContext, useState, useEffect } from 'react';

const DonorContext = createContext();

export const useDonor = () => {
  const context = useContext(DonorContext);
  if (!context) {
    throw new Error('useDonor must be used within DonorProvider');
  }
  return context;
};

export const DonorProvider = ({ children }) => {

  const [donorData, setDonorData] = useState(null);
  const [donationsData, setDonationsData] = useState([]);
  const [projectsData, setProjectsData] = useState([]);
  const [schoolsData, setSchoolsData] = useState([]);
  const [sponsoredStudentsData, setSponsoredStudentsData] = useState([]);
  const [gamificationData, setGamificationData] = useState(null);
  const [donorStats, setDonorStats] = useState(null);
  const [uniqueSchoolsCount, setUniqueSchoolsCount] = useState(0);
  const [loading, setLoading] = useState(false);

  const API_BASE_URL = 'http://localhost:8081/api';

  const fetchDonorData = async (userId) => {
    try {
      const response = await fetch(`${API_BASE_URL}/donors/user/${userId}`);
      if (response.ok) {
        const data = await response.json();
        setDonorData(data);
        return data;
      }
    } catch (error) {
      console.error('Error fetching donor data:', error);
    }
    return null;
  };

  const fetchDonationsData = async (donorId = null) => {
    try {
      const endpoint = donorId ? `${API_BASE_URL}/donations/donor/${donorId}` : `${API_BASE_URL}/donations`;
      const response = await fetch(endpoint);
      if (response.ok) {
        const data = await response.json();
        setDonationsData(Array.isArray(data) ? data : []);
        return data;
      }
    } catch (error) {
      console.error('Error fetching donations data:', error);
    }
    return [];
  };

  const fetchProjectsData = async () => {
    try {

      const response = await fetch(`${API_BASE_URL}/school-projects`);
     
      if (response.ok) {
        const data = await response.json();
        
        const projects = Array.isArray(data) ? data : [];
        setProjectsData(projects);
     
        return data;
      } else {
        console.error('Projects API error:', response.status, response.statusText);
      }
    } catch (error) {
      console.error('Error fetching projects data:', error);
    }
    return [];
  };

  const fetchSchoolsData = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/schools`);
      if (response.ok) {
        const data = await response.json();
        setSchoolsData(Array.isArray(data) ? data : []);
        return data;
      }
    } catch (error) {
      console.error('Error fetching schools data:', error);
    }
    return [];
  };

  const fetchSponsoredStudentsData = async (donorId) => {
    if (!donorId) {
      console.log('No donorId provided, skipping sponsored students fetch');
      return [];
    }
    
    try {
      const url = `${API_BASE_URL}/students/sponsored/donor/${donorId}`;

      
      const response = await fetch(url);
    
      
      if (response.ok) {
        const data = await response.json();
   
        
        const students = Array.isArray(data) ? data : [];
        setSponsoredStudentsData(students);
       
      
        return data;
      } else {
        const errorText = await response.text();
        console.error('API Error - Status:', response.status);
        console.error('API Error - Text:', errorText);
      }
    } catch (error) {
      console.error('Network/Parse Error:', error);
    }
    return [];
  };
const fetchGamificationData = async (donorId) => {
    if (!donorId) {
      console.log('No donorId provided, skipping gamification fetch');
      return null;
    }
    
    try {
      const url = `${API_BASE_URL}/donor-gamifications/donor/${donorId}`;

      
      const response = await fetch(url);
   
    
      if (response.ok) {
        const data = await response.json();

        setGamificationData(data);
        return data;
      } else {
        console.error('Gamification API error:', response.status, response.statusText);
        const errorText = await response.text();
        console.error('Error response body:', errorText);
      }
    } catch (error) {
      console.error('Error fetching gamification data:', error);
    }
    return null;
  };

  const fetchUniqueSchoolsCount = async (donorId) => {
    if (!donorId) {
      console.log('No donorId provided, skipping unique schools fetch');
      return 0;
    }
    
    try {
      const url = `${API_BASE_URL}/donor-gamifications/donor/${donorId}/unique-schools`;
      const response = await fetch(url);
      
      if (response.ok) {
        const count = await response.json();
        setUniqueSchoolsCount(count);
        return count;
      } else {
        console.error('Unique schools API error:', response.status, response.statusText);
      }
    } catch (error) {
      console.error('Error fetching unique schools count:', error);
    }
    return 0;
  };

  const fetchDonorStats = async (donorId) => {
    if (!donorId) {
      console.log('No donorId provided for stats fetch');
      return null;
    }
    
    try {
      const url = `${API_BASE_URL}/donor-gamifications/donor/${donorId}/stats`;
      console.log('Fetching donor stats from:', url);
      const response = await fetch(url);
      
      if (response.ok) {
        const stats = await response.json();
        console.log('Donor stats received:', stats);
        return stats;
      } else {
        console.error('Donor stats API error:', response.status, response.statusText);
      }
    } catch (error) {
      console.error('Error fetching donor statistics:', error);
    }
    return null;
  };

  const createDonation = async (donationData) => {
    try {
      const response = await fetch(`${API_BASE_URL}/donations`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(donationData),
      });
      if (response.ok) {
        const data = await response.json();
        await fetchDonationsData(); // Refresh donations
        return data;
      }
    } catch (error) {
      console.error('Error creating donation:', error);
    }
    return null;
  };

  const refreshDonorData = async (userId) => {
    console.log('refreshDonorData called with userId:', userId);
    setLoading(true);
    try {
      const donor = await fetchDonorData(userId);
      const donorId = donor?.donorId || donor?.id;
      console.log('Donor found:', donor, 'donorId:', donorId);
      
      // Always fetch all data
      console.log('About to fetch sponsored students with donorId:', donorId);
      
      const [donations, projects, schools, sponsoredStudents, gamification, stats, uniqueSchools] = await Promise.all([
        fetchDonationsData(donorId),
        fetchProjectsData(),
        fetchSchoolsData(),
        fetchSponsoredStudentsData(donorId),
        fetchGamificationData(donorId),
        fetchDonorStats(donorId),
        fetchUniqueSchoolsCount(donorId)
      ]);
      
      setDonorStats(stats);
      
      
      // Wait a bit for state to update, then log
      setTimeout(() => {
        console.log('Final sponsored students in state after update:', sponsoredStudentsData?.length);
      }, 100);
    } catch (error) {
      console.error('Error refreshing donor data:', error);
    }
    setLoading(false);
  };

  // Initialize data on provider mount
  const initializeDonorData = async () => {
    console.log('Initializing donor data...');
    await Promise.all([
      fetchProjectsData(),
      fetchSchoolsData(),
      fetchDonationsData(),
      fetchGamificationData()
    ]);
  };

  // Initialize data when provider mounts
  useEffect(() => {
 
    initializeDonorData();
    
    // Also try to refresh donor data if userId exists
    const userId = localStorage.getItem('userId');
  
    if (userId) {
      console.log('Found userId, calling refreshDonorData...');
      refreshDonorData(userId);
    } else {
      console.log('No userId found in localStorage');
    }
  }, []);

  const value = {
    donorData,
    donationsData,
    projectsData,
    schoolsData,
    sponsoredStudentsData,
    gamificationData,
    donorStats,
    uniqueSchoolsCount,
    loading,
    fetchDonorData,
    fetchDonationsData,
    fetchProjectsData,
    fetchSchoolsData,
    fetchSponsoredStudentsData,
    fetchGamificationData,
    createDonation,
    refreshDonorData,
    initializeDonorData,
    API_BASE_URL,
    // Helper functions for filtered data
    getDonorProjects: () => projectsData,
    getDonorDonations: () => donationsData,
    getDonorStats: () => {
      const totalDonated = donationsData.reduce((sum, d) => sum + (d.amount || 0), 0);
      const uniqueSchools = gamificationData?.schoolsSupported || 0;
      const activeProjects = projectsData.filter(p => p.status === 'ACTIVE').length;
      return { totalDonated, uniqueSchools, activeProjects };
    }
  };

  return (
    <DonorContext.Provider value={value}>
      {children}
    </DonorContext.Provider>
  );
};