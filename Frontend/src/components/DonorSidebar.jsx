import { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { Home, FolderOpen, Users, BarChart3 } from 'lucide-react';

export default function DonorSidebar({ donorId }) {
  const navigate = useNavigate();
  const location = useLocation();
  
  const menuItems = [
    { id: 'home', label: 'Home', icon: Home, path: `/donor-dashboard/${donorId}` },
    { id: 'projects', label: 'Projects', icon: FolderOpen, path: `/donor-projects/${donorId}` },
    { id: 'students', label: 'Students', icon: Users, path: `/donor-students/${donorId}` },
    { id: 'reporting', label: 'Reporting', icon: BarChart3, path: `/donor-reporting/${donorId}` },
  ];

  const isActive = (path) => location.pathname === path;

  return (
    <div className="w-64 h-screen flex flex-col" style={{ backgroundColor: '#0E792E' }}>
      <div className="p-6 border-b border-green-600">
        <h2 className="text-xl font-bold text-white">BrightAid</h2>
        <p className="text-green-100 text-sm">Donor Portal</p>
      </div>
      
      <nav className="flex-1 p-4">
        <ul className="space-y-2">
          {menuItems.map((item) => {
            const Icon = item.icon;
            const active = isActive(item.path);
            
            return (
              <li key={item.id}>
                <button
                  onClick={() => navigate(item.path)}
                  className={`w-full flex items-center gap-3 px-4 py-3 rounded-lg text-left transition-all duration-200 ${
                    active 
                      ? 'bg-white text-green-800 font-semibold' 
                      : 'text-green-100 hover:bg-green-700 hover:text-white'
                  }`}
                  style={active ? {} : { backgroundColor: 'transparent' }}
                >
                  <Icon size={20} />
                  <span>{item.label}</span>
                </button>
              </li>
            );
          })}
        </ul>
      </nav>
      
      <div className="p-4 border-t border-green-600">
        <button
          onClick={() => {
            localStorage.removeItem('token');
            localStorage.removeItem('userId');
            navigate('/login');
          }}
          className="w-full px-4 py-2 text-green-100 hover:bg-green-700 hover:text-white rounded-lg transition-all duration-200"
          style={{ backgroundColor: 'transparent' }}
        >
          Logout
        </button>
      </div>
    </div>
  );
}