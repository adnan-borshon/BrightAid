import React from 'react';
import { useNavigate, useParams, useLocation } from 'react-router-dom';
import { Home, Users, Briefcase, FileText, CreditCard, HelpCircle, Settings, ChevronRight, Search } from 'lucide-react';
import { useApp } from '../context/AppContext';

export default function DashSidebar() {
  const { schoolData, studentsData, projectsData } = useApp();
  const navigate = useNavigate();
  const { schoolId } = useParams();
  const location = useLocation();
  const navItems = [
    { name: 'Dashboard', icon: Home, badge: null, path: `/dashboard/${schoolId}` },
    { name: 'Students', icon: Users, badge: studentsData?.length?.toString() || '0', path: `/students/${schoolId}` },
    { name: 'Projects', icon: Briefcase, badge: projectsData?.length?.toString() || '0', path: `/projects/${schoolId}` },
    { name: 'Reporting', icon: FileText, badge: 'Pending', badgeColor: 'text-red-600', path: `/reporting/${schoolId}` },
    { name: 'Account', icon: CreditCard, badge: null, path: `/account/${schoolId}` },
    { name: 'Support', icon: HelpCircle, badge: null, path: `/support/${schoolId}` }, 
    { name: 'Settings', icon: Settings, badge: null, path: `/settings/${schoolId}` },
  ];

  const getActiveNav = () => {
    const path = location.pathname;
    if (path.includes('/students/')) return 'Students';
    if (path.includes('/projects/')) return 'Projects';
    if (path.includes('/reporting/')) return 'Reporting';
    if (path.includes('/account/')) return 'Account';
    if (path.includes('/support/')) return 'Support';
    if (path.includes('/settings/')) return 'Settings';
    return 'Dashboard';
  };

  const activeNav = getActiveNav();

  return (
    <div className="w-64 bg-gradient-to-b from-green-50 to-green-100 p-6 flex flex-col">
      <div className="flex items-center gap-2 mb-8">
        <div className="w-6 h-6 bg-green-700 rounded flex items-center justify-center text-white text-xs">★</div>
        <div>
          <span className="font-bold text-gray-800">BrightAid</span>
          <span className="font-light text-gray-800"> Institute</span>
        </div>
      </div>

      <div className="flex items-center gap-3 mb-6 bg-white rounded-lg p-3">
        <div className="w-10 h-10 bg-orange-200 rounded-full"></div>
        <div className="flex-1">
          <div className="text-sm font-semibold text-gray-800">Fatichula Reza</div>
          <div className="text-xs text-gray-500">Admin</div>
        </div>
        <ChevronRight className="w-4 h-4 text-gray-400" />
      </div>

      <div className="mb-6 relative">
        <Search className="w-4 h-4 absolute left-3 top-3 text-gray-400" />
        <input 
          type="text" 
          placeholder="Search" 
          className="w-full pl-10 pr-3 py-2 bg-white rounded-lg text-sm border-none focus:outline-none focus:ring-2 focus:ring-green-300"
        />
      </div>

      <nav className="flex-1 space-y-1">
        {navItems.map((item) => {
          const Icon = item.icon;
          const isActive = activeNav === item.name;

        
          if (item.name === "Support") {
            return (
              <div key={item.name} className="border-t border-gray-300 pt-6 mt-6 space-y-1">
                <button
                  onClick={() => navigate(item.path)}
                  className={` w-full flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm transition-colors ${
                    isActive
                      ? 'secondary !border-0 hover:!bg-gray-50 hover:!text-[#0E792E]'
                      : 'secondary !bg-transparent !border-0 hover:!bg-gray-50 hover:!text-[#0E792E]'
                  }`}
                >
                  <Icon className="w-4 h-4" />
                  <span className="flex-1 text-left">{item.name}</span>
                </button>
              </div>
            );
          }

          return (
            <button
              key={item.name}
              onClick={() => navigate(item.path)}
              className={`secondary w-full flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm transition-colors ${
                isActive
                  ? 'secondary !border-0 hover:!bg-gray-50 hover:!text-[#0E792E]'
                  : 'secondary !bg-transparent !border-0 hover:!bg-gray-50 hover:!text-[#0E792E]'
              }`}
            >
              <Icon className="w-4 h-4" />
              <span className="flex-1 text-left">{item.name}</span>
              {item.badge && (
                <span
                  className={`text-[10px] ${
                    item.badgeColor
                      ? `${item.badgeColor} ml-10 px-2 py-1 bg-red-50 rounded-xl`
                      : 'text-gray-500'
                  }`}
                >
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
