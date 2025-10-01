import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Download } from "lucide-react";



const statusStyles = {
  completed: "bg-chart-2/20 text-chart-2",
  pending: "bg-chart-5/20 text-chart-5",
  failed: "bg-destructive/20 text-destructive",
};

export default function DonorDonationHistoryTable({ donations }) {
  return (
    <div className="rounded-lg border">
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Date</TableHead>
            <TableHead>Project / Student</TableHead>
            <TableHead className="text-right">Amount</TableHead>
            <TableHead>Status</TableHead>
            <TableHead>Transaction ID</TableHead>
            <TableHead className="text-right">Receipt</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {donations.map((donation) => (
            <TableRow key={donation.id} data-testid={`row-donation-${donation.id}`}>
              <TableCell className="font-medium">
                {new Date(donation.date).toLocaleDateString('en-BD')}
              </TableCell>
              <TableCell>{donation.projectName}</TableCell>
              <TableCell className="text-right font-mono">
                ৳{donation.amount.toLocaleString()}
              </TableCell>
              <TableCell>
                <Badge className={statusStyles[donation.status]}>
                  {donation.status}
                </Badge>
              </TableCell>
              <TableCell className="font-mono text-sm text-muted-foreground">
                {donation.transactionRef}
              </TableCell>
              <TableCell className="text-right">
                <Button 
                  variant="ghost" 
                  size="sm"
                  data-testid={`button-download-receipt-${donation.id}`}
                >
                  <Download className="h-4 w-4" />
                </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  );
}
