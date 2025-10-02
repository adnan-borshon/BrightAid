import React from 'react';
import { useNavigate, useParams, useLocation } from 'react-router-dom';
import { Home, Users, Briefcase, FileText, ChevronRight, Search } from 'lucide-react';
import { useDonor } from '../context/DonorContext';

export default function DonorDashSidebar() {
  const { donationsData, projectsData } = useDonor();
  const navigate = useNavigate();
  const { id: userId } = useParams(); // URL param is now userId
  const location = useLocation();
  const navItems = [
    { name: 'Home', icon: Home, badge: null, path: `/donor-dashboard/${userId}` },
    { name: 'Projects', icon: Briefcase, badge: projectsData?.length?.toString() || '0', path: `/donor-projects/${userId}` },
    { name: 'Students', icon: Users, badge: '0', path: `/donor-students/${userId}` },
    { name: 'Reporting', icon: FileText, badge: null, path: `/donor-reporting/${userId}` },
  ];

  const getActiveNav = () => {
    const path = location.pathname;
    if (path.includes('/donor-students/')) return 'Students';
    if (path.includes('/donor-projects/')) return 'Projects';
    if (path.includes('/donor-reporting/')) return 'Reporting';
    return 'Home';
  };

  const activeNav = getActiveNav();

  return (
    <div className="w-64 p-6 flex flex-col" style={{ backgroundColor: '#0E792E' }}>
      <div className="flex items-center gap-2 mb-8">
        <div className="w-6 h-6 bg-white rounded flex items-center justify-center text-green-800 text-xs">★</div>
        <div>
          <span className="font-bold text-white">BrightAid</span>
          <span className="font-light text-green-100"> Donor</span>
        </div>
      </div>

      <div className="flex items-center gap-3 mb-6  bg-opacity-20 rounded-lg p-3">
        <div className="w-10 h-10 bg-white rounded-full"></div>
        <div className="flex-1">
          <div className="text-sm font-semibold text-white">Donor User</div>
          <div className="text-xs text-green-100">Donor</div>
        </div>
        <ChevronRight className="w-4 h-4 text-green-100" />
      </div>

      {/* <div className="mb-6 relative">
        <Search className="w-4 h-4 absolute left-3 top-3 text-gray-400" />
        <input 
          type="text" 
          placeholder="Search" 
          className="w-full pl-10 pr-3 py-2 bg-white bg-opacity-20 rounded-lg text-sm border-none focus:outline-none focus:ring-2 focus:ring-white text-white placeholder-green-100"
        />
      </div> */}

      <nav className="flex-1 space-y-1">
        {navItems.map((item) => {
          const Icon = item.icon;
          const isActive = activeNav === item.name;

          return (
            <button
              key={item.name}
              onClick={() => navigate(item.path)}
              className={`w-full flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm transition-colors ${
                isActive
                  ? 'bg-white text-green-800 font-semibold'
                  : 'text-green-100 hover:bg-white hover:bg-opacity-20 hover:text-white'
              }`}
              style={isActive ? {} : { backgroundColor: 'transparent' }}
            >
              <Icon className="w-4 h-4" />
              <span className="flex-1 text-left">{item.name}</span>
              {item.badge && (
                <span className="text-[10px] bg-white bg-opacity-20 text-green-100 px-2 py-1 rounded-xl">
                  {item.badge}
                </span>
              )}
            </button>
          );
        })}
      </nav>
    </div>
  );
}
