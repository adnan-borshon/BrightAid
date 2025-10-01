import * as React from "react"

const Progress = React.forwardRef(({ className = "", value = 0, ...props }, ref) => {
  const percentage = Math.min(Math.max(value, 0), 100);
  
  return (
    <div
      ref={ref}
      className={`relative h-4 w-full overflow-hidden rounded-full bg-gray-200 ${className}`}
      {...props}
    >
      <div
        className="h-full bg-green-600 transition-all duration-300 ease-in-out"
        style={{ width: `${percentage}%` }}
      />
    </div>
  );
});

Progress.displayName = "Progress";

export { Progress };