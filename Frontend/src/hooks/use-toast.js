import { useState, useEffect } from 'react';

let toastState = {
  toasts: [],
  listeners: []
};

const addToast = (toast) => {
  const id = Date.now();
  const newToast = { id, ...toast };
  toastState.toasts.push(newToast);
  
  toastState.listeners.forEach(listener => listener(toastState.toasts));
  
  setTimeout(() => {
    toastState.toasts = toastState.toasts.filter(t => t.id !== id);
    toastState.listeners.forEach(listener => listener(toastState.toasts));
  }, 5000);
};

export const useToast = () => {
  const [toasts, setToasts] = useState(toastState.toasts);
  
  useEffect(() => {
    const listener = (newToasts) => setToasts([...newToasts]);
    toastState.listeners.push(listener);
    
    return () => {
      toastState.listeners = toastState.listeners.filter(l => l !== listener);
    };
  }, []);
  
  const toast = (options) => {
    console.log('Toast:', options.title, options.description);
    addToast(options);
  };
  
  return { toast, toasts };
};