import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";

export default function DonorDonationChart({ data }) {
  const maxAmount = Math.max(...(data || []).map(d => d.amount || 0));
  
  return (
    <Card>
      <CardHeader>
        <CardTitle className="text-lg">Donation Trends</CardTitle>
      </CardHeader>
      <CardContent>
        <div className="h-[300px] flex items-end justify-between gap-2 p-4">
          {(data || []).map((item, index) => {
            const height = maxAmount > 0 ? (item.amount / maxAmount) * 250 : 0;
            return (
              <div key={index} className="flex flex-col items-center gap-2 flex-1">
                <div className="text-xs text-gray-600">৳{item.amount?.toLocaleString() || 0}</div>
                <div 
                  className="bg-green-600 w-full rounded-t transition-all duration-300"
                  style={{ height: `${height}px`, minHeight: '4px' }}
                />
                <div className="text-xs text-gray-500">{item.month}</div>
              </div>
            );
          })}
        </div>
        {(!data || data.length === 0) && (
          <div className="h-[300px] flex items-center justify-center text-gray-500">
            No donation data available
          </div>
        )}
      </CardContent>
    </Card>
  );
}
