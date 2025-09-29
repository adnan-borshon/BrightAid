import React from "react";
import { Check } from "lucide-react";

const Sidebar = ({ currentPath }) => {
  const steps = [
    {
      id: "registration",
      path: "/",
      title: "Registration",
      description:
        "Create your BrightAid account to start connecting with donors and NGOs",
      number: 1,
    },
    {
      id: "school-profile",
      path: "/school-profile",
      title: "School Profile Setup",
      description:
        "Share your school's basic information for greater visibility and funding opportunities",
      number: 2,
    },
    {
      id: "document-verification",
      path: "/document-verification",
      title: "Document Verification",
      description:
        "Submit official documents to verify your school and ensure platform transparency",
      number: 3,
    },
    {
      id: "approval",
      path: "/approval",
      title: "Approval & Activation",
      description:
        "Your profile is under review, school will go live once verified",
      number: 4,
    },
  ];

  const currentStepIndex = steps.findIndex(
    (step) => step.path === currentPath
  );

  return (
    <div className="w-80 bg-[#0E792E] text-white p-8 min-h-screen">
      <div className="flex items-center mb-12 ">
   
        <img src="Logo_white.svg" alt="" className="h-10"/>
      </div>

      <div className="space-y-6">
        {steps.map((step, index) => {
          const isCompleted = index < currentStepIndex;
          const isActive = index === currentStepIndex;
          const isUpcoming = index > currentStepIndex;

          return (
            <div key={step.id} className="flex items-start">
              <div className="flex flex-col items-center mr-4">
                <div
                  className={`w-8 h-8 rounded-full flex items-center justify-center ${
                    isCompleted
                      ? "bg-white text-green-600"
                      : isActive
                      ? "bg-white text-green-600"
                      : "bg-green-600 text-white"
                  }`}
                >
                  {isCompleted ? (
                    <Check className="w-4 h-4" />
                  ) : (
                    <span className="text-sm font-bold">{step.number}</span>
                  )}
                </div>
                {index < steps.length - 1 && (
                  <div
                    className={`w-0.5 h-12 mt-2 bg-white
                    }`}
                  ></div>
                )}
              </div>

              <div className={`flex-1 ${isUpcoming ? "opacity-60" : ""}`}>
                <h3 className="font-semibold text-sm mb-1">{step.title}</h3>
                <p className="text-xs text-green-100 leading-relaxed">
                  {step.description}
                </p>
              </div>
            </div>
          );
        })}
      </div>

      <div className="mt-16 pt-8 border-t border-[#0E792E]">
        <p className="text-xs text-green-200">© BrightAid 2025</p>
        <p className="text-xs text-green-200 mt-1">help@BrightAid.com</p>
      </div>
    </div>
  );
};

export default Sidebar;
