import React from 'react';

const Stats = () => {
  const stats = [
    {
      number: '7000+',
      label: 'STUDENTS IMPACTED'
    },
    {
      number: '250+',
      label: 'PROJECTS COMPLETED'
    },
    {
      number: '32+',
      label: 'NGOs CONNECTED'
    },
    {
      number: '2+',
      label: 'YEARS OF OPERATION'
    }
  ];

  return (
    <section className="bg-green-700 py-16">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="grid grid-cols-2 md:grid-cols-4 gap-8 text-center">
          {stats.map((stat, index) => (
            <div key={index} className="text-white">
              <div className="text-4xl lg:text-5xl font-bold mb-2">{stat.number}</div>
              <div className="text-sm lg:text-base opacity-90 font-medium">{stat.label}</div>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
};

export default Stats;