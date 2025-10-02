import { useEffect, useState } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import { CheckCircle, XCircle, AlertCircle } from 'lucide-react';

export default function PaymentSuccess() {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const [status, setStatus] = useState('loading');
  const [message, setMessage] = useState('Processing payment...');

  useEffect(() => {
    const paymentStatus = searchParams.get('status');
    const transactionId = searchParams.get('tran_id');
    
    if (paymentStatus === 'VALID') {
      setStatus('success');
      setMessage('Payment completed successfully!');
    } else if (paymentStatus === 'FAILED') {
      setStatus('failed');
      setMessage('Payment failed. Please try again.');
    } else if (paymentStatus === 'CANCELLED') {
      setStatus('cancelled');
      setMessage('Payment was cancelled.');
    } else {
      setStatus('unknown');
      setMessage('Payment status unknown.');
    }

    // Auto redirect after 5 seconds
    const timer = setTimeout(() => {
      navigate('/donor-dashboard');
    }, 5000);

    return () => clearTimeout(timer);
  }, [searchParams, navigate]);

  const getIcon = () => {
    switch (status) {
      case 'success':
        return <CheckCircle className="w-16 h-16 text-green-500" />;
      case 'failed':
        return <XCircle className="w-16 h-16 text-red-500" />;
      case 'cancelled':
        return <AlertCircle className="w-16 h-16 text-yellow-500" />;
      default:
        return <div className="w-16 h-16 border-4 border-blue-500 border-t-transparent rounded-full animate-spin" />;
    }
  };

  const getStatusColor = () => {
    switch (status) {
      case 'success':
        return 'text-green-600';
      case 'failed':
        return 'text-red-600';
      case 'cancelled':
        return 'text-yellow-600';
      default:
        return 'text-blue-600';
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 flex items-center justify-center">
      <div className="bg-white p-8 rounded-lg shadow-lg text-center max-w-md w-full">
        <div className="flex justify-center mb-6">
          {getIcon()}
        </div>
        
        <h1 className={`text-2xl font-bold mb-4 ${getStatusColor()}`}>
          {status === 'success' && 'Payment Successful'}
          {status === 'failed' && 'Payment Failed'}
          {status === 'cancelled' && 'Payment Cancelled'}
          {status === 'loading' && 'Processing...'}
          {status === 'unknown' && 'Payment Status Unknown'}
        </h1>
        
        <p className="text-gray-600 mb-6">{message}</p>
        
        <div className="space-y-3">
          <button
            onClick={() => navigate('/donor-dashboard')}
            className="w-full bg-green-600 text-white py-2 px-4 rounded-lg hover:bg-green-700"
          >
            Return to Dashboard
          </button>
          
          {status === 'failed' && (
            <button
              onClick={() => window.history.back()}
              className="w-full bg-gray-600 text-white py-2 px-4 rounded-lg hover:bg-gray-700"
            >
              Try Again
            </button>
          )}
        </div>
        
        <p className="text-sm text-gray-500 mt-4">
          Redirecting to dashboard in 5 seconds...
        </p>
      </div>
    </div>
  );
}