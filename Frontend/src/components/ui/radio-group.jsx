import * as React from "react"

const RadioGroup = React.forwardRef(({ className = "", value, onValueChange, children, ...props }, ref) => {
  return (
    <div
      ref={ref}
      className={`grid gap-2 ${className}`}
      role="radiogroup"
      {...props}
    >
      {React.Children.map(children, (child) => {
        if (React.isValidElement(child)) {
          return React.cloneElement(child, {
            checked: child.props.value === value,
            onCheckedChange: () => onValueChange?.(child.props.value),
          });
        }
        return child;
      })}
    </div>
  );
});

const RadioGroupItem = React.forwardRef(({ className = "", value, id, checked, onCheckedChange, ...props }, ref) => {
  return (
    <button
      ref={ref}
      type="button"
      role="radio"
      aria-checked={checked}
      className={`aspect-square h-4 w-4 rounded-full border border-gray-300 text-primary ring-offset-background focus:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50 ${
        checked ? "bg-green-600 border-green-600" : ""
      } ${className}`}
      onClick={onCheckedChange}
      {...props}
    >
      {checked && (
        <div className="flex items-center justify-center">
          <div className="h-2 w-2 rounded-full bg-white" />
        </div>
      )}
    </button>
  );
});

RadioGroup.displayName = "RadioGroup";
RadioGroupItem.displayName = "RadioGroupItem";

export { RadioGroup, RadioGroupItem };