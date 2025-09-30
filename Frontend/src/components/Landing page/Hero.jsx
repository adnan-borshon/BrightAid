import React from "react";
import student from "/student.png";
import { Link } from "react-router-dom";
import StudentBg from "../StudentBg.jsx"
const Hero = () => {
  return (
    <section className="relative bg-[#E8FAED]">
      <div className="absolute  mx-20 my-7">
        <img src="logo.svg" alt="" className="h-9"/>
      </div>
      <div className="grid lg:grid-cols-12 ">
        {/* Left Column */}

        <div className="flex flex-col col-span-12 lg:col-span-6 justify-center px-6 sm:px-8 lg:px-16 py-12">
     
          <p className="text-gray-600 mb-4 w-max bg-white rounded-3xl p-3">
            Connecting Donors with Schools to Reduce Dropout in Bangladesh 
          </p>

          <h1 className="text-4xl sm:text-5xl lg:text-6xl font-bold mb-6">
            <span className="text-gray-900">Bridging Gaps,</span>
            <br />
            <span className="text-green-600">Building Futures</span>
          </h1>

          <p className="text-gray-700 text-base sm:text-lg mb-8 max-w-lg">
            Connecting donors with schools to reduce dropout rates in
            Bangladesh. Empowering communities through education.
          </p>

          <div className="flex flex-col sm:flex-row gap-4">
            <Link
            to="/signup1"
            className="bg-green-600 text-white px-6 sm:px-8 py-3 rounded-lg hover:bg-green-700 transition-colors flex items-center">
              <span className="mr-2">💚</span> Donate in Project
            </Link>
            <Link
            to="/signup1"
            className="border-2 border-gray-300 text-gray-700 px-6 sm:px-8 py-3 rounded-lg hover:border-green-600 hover:text-green-600 transition-colors flex items-center">
              <span className="mr-2">👨‍🎓</span> Sponsor a Student
            </Link>
          </div>
        </div>

        {/* Right Column with Green Burst */}
      <StudentBg/>
      </div>
    </section>
  );
};

export default Hero;
