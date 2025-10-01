import React, { createContext, useContext, useState } from 'react';

const AppContext = createContext();

export const useApp = () => {
  const context = useContext(AppContext);
  if (!context) {
    throw new Error('useApp must be used within AppProvider');
  }
  return context;
};

export const AppProvider = ({ children }) => {
  const [schoolData, setSchoolData] = useState(null);
  const [studentsData, setStudentsData] = useState([]);
  const [projectsData, setProjectsData] = useState([]);
  const [loading, setLoading] = useState(false);

  const API_BASE_URL = 'http://localhost:8081/api';

  const fetchSchoolData = async (schoolId) => {
    try {
      const response = await fetch(`${API_BASE_URL}/schools/${schoolId}`);
      if (response.ok) {
        const data = await response.json();
        setSchoolData(data);
        return data;
      }
    } catch (error) {
      console.error('Error fetching school data:', error);
    }
    return null;
  };

  const fetchStudentsData = async (schoolId) => {
    try {
      const response = await fetch(`${API_BASE_URL}/students`);
      if (response.ok) {
        const data = await response.json();
        const students = Array.isArray(data) ? data : data.data || [];
        const schoolStudents = students.filter(student => 
          (student.schoolId || student.school_id) == schoolId
        );
        setStudentsData(schoolStudents);
        return schoolStudents;
      }
    } catch (error) {
      console.error('Error fetching students data:', error);
    }
    return [];
  };

  const fetchProjectsData = async (schoolId) => {
    try {
      const response = await fetch(`${API_BASE_URL}/school-projects`);
      if (response.ok) {
        const data = await response.json();
        const projects = Array.isArray(data) ? data : data.data || [];
        const schoolProjects = projects.filter(project => 
          (project.schoolId || project.school_id) == schoolId
        );
        setProjectsData(schoolProjects);
        return schoolProjects;
      }
    } catch (error) {
      console.error('Error fetching projects data:', error);
    }
    return [];
  };

  const refreshData = async (schoolId) => {
    setLoading(true);
    await Promise.all([
      fetchSchoolData(schoolId),
      fetchStudentsData(schoolId),
      fetchProjectsData(schoolId)
    ]);
    setLoading(false);
  };

  const value = {
    schoolData,
    studentsData,
    projectsData,
    loading,
    fetchSchoolData,
    fetchStudentsData,
    fetchProjectsData,
    refreshData,
    API_BASE_URL
  };

  return (
    <AppContext.Provider value={value}>
      {children}
    </AppContext.Provider>
  );
};