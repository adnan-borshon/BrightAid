import React, { createContext, useContext, useState } from 'react';

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

  const fetchDonationsData = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/donations`);
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
        setProjectsData(Array.isArray(data) ? data : []);
        return data;
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
    setLoading(true);
    await Promise.all([
      fetchDonorData(userId),
      fetchDonationsData(),
      fetchProjectsData(),
      fetchSchoolsData()
    ]);
    setLoading(false);
  };

  const value = {
    donorData,
    donationsData,
    projectsData,
    schoolsData,
    loading,
    fetchDonorData,
    fetchDonationsData,
    fetchProjectsData,
    fetchSchoolsData,
    createDonation,
    refreshDonorData,
    API_BASE_URL
  };

  return (
    <DonorContext.Provider value={value}>
      {children}
    </DonorContext.Provider>
  );
};