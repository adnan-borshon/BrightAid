import * as React from "react"
import { ChevronDown } from "lucide-react"

const SelectContext = React.createContext();

const Select = ({ children, value, onValueChange, ...props }) => {
  const [isOpen, setIsOpen] = React.useState(false);
  const [selectedValue, setSelectedValue] = React.useState(value || "");

  const handleValueChange = (newValue) => {
    setSelectedValue(newValue);
    onValueChange?.(newValue);
    setIsOpen(false);
  };

  return (
    <SelectContext.Provider value={{ 
      isOpen, 
      setIsOpen, 
      selectedValue, 
      handleValueChange 
    }}>
      <div className="relative" {...props}>
        {children}
      </div>
    </SelectContext.Provider>
  );
};

const SelectTrigger = React.forwardRef(({ className = "", children, ...props }, ref) => {
  const { isOpen, setIsOpen, selectedValue } = React.useContext(SelectContext);
  
  return (
    <button
      ref={ref}
      type="button"
      className={`flex h-10 w-full items-center justify-between rounded-md border border-gray-300 bg-white px-3 py-2 text-sm ring-offset-background placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50 ${className}`}
      onClick={() => setIsOpen(!isOpen)}
      {...props}
    >
      {children}
      <ChevronDown className="h-4 w-4 opacity-50" />
    </button>
  );
});

const SelectValue = ({ placeholder }) => {
  const { selectedValue } = React.useContext(SelectContext);
  return (
    <span className={selectedValue ? "" : "text-gray-500"}>
      {selectedValue || placeholder}
    </span>
  );
};

const SelectContent = ({ className = "", children, ...props }) => {
  const { isOpen } = React.useContext(SelectContext);
  
  if (!isOpen) return null;
  
  return (
    <div
      className={`absolute top-full left-0 z-50 w-full mt-1 bg-white border border-gray-300 rounded-md shadow-lg max-h-60 overflow-auto ${className}`}
      {...props}
    >
      {children}
    </div>
  );
};

const SelectItem = ({ className = "", value, children, ...props }) => {
  const { handleValueChange, selectedValue } = React.useContext(SelectContext);
  
  return (
    <button
      type="button"
      className={`w-full text-left px-3 py-2 text-sm hover:bg-gray-100 focus:bg-gray-100 focus:outline-none ${
        selectedValue === value ? "bg-gray-100" : ""
      } ${className}`}
      onClick={() => handleValueChange(value)}
      {...props}
    >
      {children}
    </button>
  );
};

Select.displayName = "Select";
SelectTrigger.displayName = "SelectTrigger";
SelectValue.displayName = "SelectValue";
SelectContent.displayName = "SelectContent";
SelectItem.displayName = "SelectItem";

export { Select, SelectTrigger, SelectValue, SelectContent, SelectItem };