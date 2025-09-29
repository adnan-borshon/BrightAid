
import React from "react";
import { Link } from "react-router-dom";

const Navbar = () => {
  return (
    <div className="flex items-center justify-center flex-wrap gap-4 lg:gap-8 text-sm font-semibold sm:p-6 text-[#3F4246]">
      <Link to="/how-it-works" className="hover:underline">
        How it works
      </Link>
      <Link to="/scholarships" className="hover:underline">
        Scholarships
      </Link>
      <Link to="/projects" className="hover:underline">
        Projects
      </Link>
      <Link to="/collaborate" className="hover:underline">
        Collaborate
      </Link>
      <Link to="/about" className="hover:underline">
        About
      </Link>
      <button className="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition-colors">
        Sign Up
      </button>
    </div>
  );
};

export default Navbar;
