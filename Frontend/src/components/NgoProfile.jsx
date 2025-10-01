import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

export default function NgoProfile() {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [formData, setFormData] = useState({
    ngoName: '',
    registrationNumber: '',
    contactPerson: '',
    phoneNumber: '',
    address: '',
    focusAreas: ''
  });

  useEffect(() => {
    const authData = JSON.parse(localStorage.getItem('authData') || '{}');
    if (authData.user) {
      setFormData(prev => ({
        ...prev,
        ngoName: authData.user.username || ''
      }));
    }
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const authData = JSON.parse(localStorage.getItem('authData') || '{}');
      const response = await fetch('http://localhost:8081/api/ngos', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          userId: authData.user.userId,
          ngoName: formData.ngoName,
          registrationNumber: formData.registrationNumber,
          contactPerson: formData.contactPerson,
          phoneNumber: formData.phoneNumber,
          address: formData.address,
          focusAreas: formData.focusAreas
        })
      });

      if (response.ok) {
        const ngoData = await response.json();
        navigate(`/ngo-dashboard/${ngoData.ngoId}`);
      } else {
        alert('Failed to create NGO profile');
      }
    } catch (error) {
      console.error('Error creating NGO profile:', error);
      alert('Error creating profile');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 flex items-center justify-center">
      <div className="max-w-md w-full bg-white rounded-lg shadow p-6">
        <h1 className="text-2xl font-bold text-gray-800 mb-6">Complete Your NGO Profile</h1>
        
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              NGO Name*
            </label>
            <input
              type="text"
              value={formData.ngoName}
              onChange={(e) => setFormData(prev => ({ ...prev, ngoName: e.target.value }))}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Registration Number*
            </label>
            <input
              type="text"
              value={formData.registrationNumber}
              onChange={(e) => setFormData(prev => ({ ...prev, registrationNumber: e.target.value }))}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Contact Person*
            </label>
            <input
              type="text"
              value={formData.contactPerson}
              onChange={(e) => setFormData(prev => ({ ...prev, contactPerson: e.target.value }))}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Phone Number*
            </label>
            <input
              type="tel"
              value={formData.phoneNumber}
              onChange={(e) => setFormData(prev => ({ ...prev, phoneNumber: e.target.value }))}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Address*
            </label>
            <textarea
              value={formData.address}
              onChange={(e) => setFormData(prev => ({ ...prev, address: e.target.value }))}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
              rows="3"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Focus Areas
            </label>
            <input
              type="text"
              value={formData.focusAreas}
              onChange={(e) => setFormData(prev => ({ ...prev, focusAreas: e.target.value }))}
              placeholder="e.g., Education, Health, Environment"
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
            />
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-green-600 hover:bg-green-700 disabled:bg-gray-400 text-white font-medium py-2 px-4 rounded-md transition duration-200"
          >
            {loading ? 'Creating Profile...' : 'Complete Profile'}
          </button>
        </form>
      </div>
    </div>
  );
}