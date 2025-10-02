import { useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';

export default function PaymentClose() {
  const [searchParams] = useSearchParams();

  useEffect(() => {
    const status = searchParams.get('status');
    const transactionId = searchParams.get('tran_id');
    
    // Send message to parent window (donor dashboard)
    if (window.opener) {
      window.opener.postMessage({
        type: 'PAYMENT_COMPLETE',
        status: status,
        transactionId: transactionId
      }, window.location.origin);
    }
    
    // Close the popup window
    window.close();
  }, [searchParams]);

  return (
    <div className="min-h-screen bg-gray-50 flex items-center justify-center">
      <div className="bg-white p-8 rounded-lg shadow-lg text-center">
        <div className="w-16 h-16 border-4 border-green-500 border-t-transparent rounded-full animate-spin mx-auto mb-4"></div>
        <h1 className="text-xl font-semibold text-gray-800">Processing Payment...</h1>
        <p className="text-gray-600 mt-2">Please wait while we complete your transaction.</p>
      </div>
    </div>
  );
}