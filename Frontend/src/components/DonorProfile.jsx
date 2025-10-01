import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

export default function DonorProfile() {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [formData, setFormData] = useState({
    donorName: '',
    taxId: '',
    isAnonymous: false
  });

  useEffect(() => {
    const authData = JSON.parse(localStorage.getItem('authData') || '{}');
    if (authData.user) {
      setFormData(prev => ({
        ...prev,
        donorName: authData.user.username || ''
      }));
    }
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const authData = JSON.parse(localStorage.getItem('authData') || '{}');
      const response = await fetch('http://localhost:8081/api/donors', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          userId: authData.user.userId,
          donorName: formData.donorName,
          taxId: formData.taxId || null,
          isAnonymous: formData.isAnonymous
        })
      });

      if (response.ok) {
        const donorData = await response.json();
        navigate(`/donor-dashboard/${donorData.donorId}`);
      } else {
        alert('Failed to create donor profile');
      }
    } catch (error) {
      console.error('Error creating donor profile:', error);
      alert('Error creating profile');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 flex items-center justify-center">
      <div className="max-w-md w-full bg-white rounded-lg shadow p-6">
        <h1 className="text-2xl font-bold text-gray-800 mb-6">Complete Your Donor Profile</h1>
        
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Donor Name*
            </label>
            <input
              type="text"
              value={formData.donorName}
              onChange={(e) => setFormData(prev => ({ ...prev, donorName: e.target.value }))}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Tax ID (Optional)
            </label>
            <input
              type="text"
              value={formData.taxId}
              onChange={(e) => setFormData(prev => ({ ...prev, taxId: e.target.value }))}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
            />
          </div>

          <div className="flex items-center">
            <input
              type="checkbox"
              id="anonymous"
              checked={formData.isAnonymous}
              onChange={(e) => setFormData(prev => ({ ...prev, isAnonymous: e.target.checked }))}
              className="h-4 w-4 text-green-600 focus:ring-green-500 border-gray-300 rounded"
            />
            <label htmlFor="anonymous" className="ml-2 text-sm text-gray-700">
              Donate anonymously
            </label>
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