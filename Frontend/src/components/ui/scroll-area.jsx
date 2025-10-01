import * as React from "react"

const ScrollArea = React.forwardRef(({ className = "", children, ...props }, ref) => {
  return (
    <div
      ref={ref}
      className={`relative overflow-auto ${className}`}
      {...props}
    >
      {children}
    </div>
  );
});

ScrollArea.displayName = "ScrollArea";

const ScrollBar = React.forwardRef(({ className = "", ...props }, ref) => {
  return <div ref={ref} className={className} {...props} />;
});

ScrollBar.displayName = "ScrollBar";

export { ScrollArea, ScrollBar };