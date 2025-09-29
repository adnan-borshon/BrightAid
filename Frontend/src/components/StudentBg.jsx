// src/components/HeroSection.jsx
import React from "react";
import Navbar from "../Fixed components/Navbar";
import student from "/student.png"; 

const StudentBg = () => {
  return (
    <div
      className="relative col-span-12 lg:col-span-6 flex flex-col rounded-4xl min-h-[60vh] lg:min-h-screen overflow-hidden"
      style={{
        background:
          "repeating-conic-gradient(from 0deg, #89F336D1 0deg 20deg, #89F336 20deg 40deg)",
      }}
    >
      {/* Navbar Links */}
      <Navbar />

      {/* Student Image (centered) */}
      <div className="relative flex justify-center items-center flex-1">
        <img
          src={student}
          alt="Student"
          className="relative z-10 rotate-y-180 object-contain"
        />

        {/* Bubbles */}
        <div className="absolute top-12 right sm:top-20 sm:right-24 bg-white px-5 sm:px-6 py-3 rounded-full z-20 shadow">
          <span className="text-sm sm:text-lg font-semibold text-[#09501E]">
            আমি বড় হবো
          </span>
        </div>

        <div className="absolute bottom-6 left-6 sm:bottom-10 sm:left-5 bg-white px-5 sm:px-6 py-3 rounded-full z-20 shadow">
          <span className="text-sm sm:text-lg font-semibold text-[#09501E]">
            স্বপ্ন দেখার শেষ নেই আমার
          </span>
        </div>
      </div>
    </div>
  );
};

export default StudentBg;
