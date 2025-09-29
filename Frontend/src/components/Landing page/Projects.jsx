import React from 'react';

const Projects = () => {
  const projects = [
    {
      id: 1,
      title: 'Help children for orphanage scholarship in Bowalmari',
      location: 'Bowalmari | Government Primary School',
      raised: '$2,400',
      daysLeft: 22,
      status: 'Raised',
      verified: true
    },
    {
      id: 2,
      title: 'Help children for orphanage scholarship in Bowalmari',
      location: 'Bowalmari | Government Primary School',
      raised: '$2,400',
      daysLeft: 22,
      status: 'Raised',
      verified: true
    },
    {
      id: 3,
      title: 'Help children for orphanage scholarship in Bowalmari',
      location: 'Bowalmari | Government Primary School',
      raised: '$2,400',
      daysLeft: 22,
      status: 'Raised',
      verified: true
    },
    {
      id: 4,
      title: 'Help children for orphanage scholarship in Bowalmari',
      location: 'Bowalmari | Government Primary School',
      raised: '$2,400',
      daysLeft: 22,
      status: 'Raised',
      verified: true
    },
    {
      id: 5,
      title: 'Help children for orphanage scholarship in Bowalmari',
      location: 'Bowalmari | Government Primary School',
      raised: '$2,400',
      daysLeft: 22,
      status: 'Raised',
      verified: true
    },
    {
      id: 6,
      title: 'Help children for orphanage scholarship in Bowalmari',
      location: 'Bowalmari LG',
      raised: '$8,383.28',
      daysLeft: 22,
      status: 'Donations',
      verified: true,
      featured: true
    }
  ];

  return (
    <section className="py-16 bg-white">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        
        {/* Header */}
        <div className="text-center mb-12">
          <h2 className="text-3xl font-bold text-gray-900 mb-4">Support Projects</h2>
          <p className="text-gray-600">Help make our community a better place for all</p>
        </div>
        
        {/* Projects Grid */}
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6 mb-8">
          {projects.map((project) => (
            <div 
              key={project.id} 
              className={`bg-white rounded-xl overflow-hidden shadow-sm border hover:shadow-md transition-shadow ${
                project.featured ? 'ring-2 ring-green-200' : ''
              }`}
            >
              {/* Project Image Placeholder */}
              <div className="h-48 bg-gradient-to-br from-green-100 to-green-200 flex items-center justify-center">
                <div className="text-center">
                  <div className="w-16 h-16 bg-green-600 rounded-full mx-auto mb-2 flex items-center justify-center">
                    <span className="text-white text-2xl">🏫</span>
                  </div>
                  <p className="text-green-700 text-sm">School Building</p>
                </div>
              </div>
              
              {/* Project Content */}
              <div className="p-6">
                {/* Location with verification */}
                <div className="flex items-center justify-between mb-2">
                  <span className="text-sm text-gray-600">{project.location}</span>
                  {project.verified && (
                    <div className="w-5 h-5 bg-blue-500 rounded-full flex items-center justify-center">
                      <span className="text-white text-xs">✓</span>
                    </div>
                  )}
                </div>
                
                {/* Title */}
                <h3 className="font-semibold text-gray-900 mb-4 line-clamp-2">
                  {project.title}
                </h3>
                
                {/* Stats */}
                <div className="flex justify-between items-center">
                  <div>
                    <div className="text-sm text-gray-500">{project.status}</div>
                    <div className="font-bold text-gray-900">{project.raised}</div>
                  </div>
                  <div className="text-right">
                    <div className="text-sm text-gray-500">Days left</div>
                    <div className="font-bold text-gray-900">{project.daysLeft}</div>
                  </div>
                </div>
                
                {project.featured && (
                  <div className="mt-4 text-center">
                    <span className="bg-green-600 text-white px-4 py-2 rounded-lg text-sm font-medium">
                      Featured Project
                    </span>
                  </div>
                )}
              </div>
            </div>
          ))}
        </div>
        
        {/* See All Button */}
        <div className="text-center">
          <button className="text-green-600 font-semibold hover:text-green-700 transition-colors">
            See all
          </button>
        </div>
      </div>
    </section>
  );
};

export default Projects;