import * as React from "react"

const Separator = React.forwardRef(
  ({ className = "", orientation = "horizontal", ...props }, ref) => {
    const baseClasses = "shrink-0 bg-gray-200";
    const orientationClasses = orientation === "horizontal" ? "h-[1px] w-full" : "h-full w-[1px]";
    
    return (
      <div
        ref={ref}
        className={`${baseClasses} ${orientationClasses} ${className}`}
        {...props}
      />
    );
  }
);

Separator.displayName = "Separator";

export { Separator };