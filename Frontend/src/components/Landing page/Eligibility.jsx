import React from 'react';

const Eligibility = () => {
  const eligibilityTypes = [
    {
      icon: '👨‍👩‍👧‍👦',
      title: 'Disabled Father',
      description: "Student's father is physically unable to work"
    },
    {
      icon: '👩‍👧‍👦',
      title: 'Single Mother',
      description: "Student's father is deceased or has abandoned the family"
    },
    {
      icon: '👫',
      title: 'Orphan',
      description: 'Both parents are deceased or have abandoned the child'
    }
  ];

  return (
    <section className="py-16 bg-gray-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        
        {/* Header */}
        <div className="text-center mb-12">
          <h2 className="text-3xl font-bold text-gray-900 mb-4">Scholarship Eligibility</h2>
          <p className="text-gray-600 max-w-2xl mx-auto">
            Only students who are currently enrolled in Government Primary Schools and fall 
            under any of the following criteria are eligible to apply for scholarships
          </p>
        </div>
        
        {/* Eligibility Cards */}
        <div className="grid md:grid-cols-3 gap-8">
          {eligibilityTypes.map((type, index) => (
            <div key={index} className="bg-white p-8 rounded-xl shadow-sm text-center hover:shadow-md transition-shadow">
              <div className="text-4xl mb-4">{type.icon}</div>
              <h3 className="text-xl font-semibold text-gray-900 mb-3">{type.title}</h3>
              <p className="text-gray-600">{type.description}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
};

export default Eligibility;