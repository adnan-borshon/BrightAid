import { useState } from "react";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";
import { Checkbox } from "@/components/ui/checkbox";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { useToast } from "@/hooks/use-toast";
import { useDonor } from "@/context/DonorContext";


export default function DonorDonationDialog({ 
  open, 
  onOpenChange, 
  donationType = "general",
  title,
  description,
  itemData = null // student or project data
}) {
  const { toast } = useToast();
  const [formData, setFormData] = useState({
    amount: "",
    message: "",
    isAnonymous: false,
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!formData.amount) {
      toast({
        title: "Error",
        description: "Please enter donation amount.",
        variant: "destructive"
      });
      return;
    }

    try {
      const donorId = localStorage.getItem('donorId');
      if (!donorId) {
        toast({
          title: "Error",
          description: "Please login to make a donation.",
          variant: "destructive"
        });
        return;
      }

      // Determine product name based on donation type
      let productName = "General Donation";
      let productCategory = "Donation";
      
      if (donationType === "student") {
        productName = "Student Sponsorship";
        productCategory = "Sponsorship";
      } else if (donationType === "project" && itemData) {
        productName = itemData.projectTitle || itemData.title || "Project Donation";
        productCategory = "Project";
      }

      // Initiate SSL Commerz payment
      console.log('Initiating payment with:', {
        donorId,
        amount: formData.amount,
        productName,
        productCategory
      });
      
      const url = `http://localhost:8081/api/payment-transactions/sslcommerz/initiate?donorId=${donorId}&amount=${formData.amount}&productName=${encodeURIComponent(productName)}&productCategory=${encodeURIComponent(productCategory)}`;
      console.log('Payment URL:', url);
      
      const response = await fetch(url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        }
      });

      console.log('Response status:', response.status);
      console.log('Response ok:', response.ok);
      
      if (!response.ok) {
        const errorText = await response.text();
        console.error('Response error:', errorText);
        throw new Error(`HTTP ${response.status}: ${errorText}`);
      }
      
      const result = await response.json();
      console.log('Payment result:', result);
      
      if (result.status === 'SUCCESS' && result.paymentUrl) {
        console.log('Opening payment URL:', result.paymentUrl);
        // Open SSL Commerz payment page
        const popup = window.open(result.paymentUrl, '_blank', 'width=800,height=600,scrollbars=yes,resizable=yes');
        
        if (!popup) {
          toast({
            title: "Popup Blocked",
            description: "Please allow popups for this site and try again.",
            variant: "destructive"
          });
          return;
        }
        
        toast({
          title: "Payment Initiated",
          description: "Please complete your payment in the new window.",
        });
        
        // Reset form and close dialog
        setFormData({
          amount: "",
          message: "",
          isAnonymous: false,
        });
        
        onOpenChange(false);
      } else {
        console.error('Payment initiation failed:', result);
        toast({
          title: "Payment Error",
          description: result.message || "Failed to initiate payment. Please try again.",
          variant: "destructive"
        });
      }
    } catch (error) {
      console.error('Payment error:', error);
      toast({
        title: "Error",
        description: "An error occurred while processing your payment. Please try again.",
        variant: "destructive"
      });
    }
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[500px]">
        <DialogHeader>
          <DialogTitle>
            {donationType === "project" ? "Project Donation" : title || "Make a Donation"}
          </DialogTitle>
          <DialogDescription>
            {description || "Support education in Bangladesh"}
          </DialogDescription>
        </DialogHeader>
        
        <form onSubmit={handleSubmit}>
          <div className="grid gap-4 py-4">
            {itemData && (
              <div className="bg-gray-50 p-4 rounded-lg">
                {donationType === "student" && (
                  <div>
                    <h4 className="font-semibold text-gray-900">{itemData.studentName || itemData.name}</h4>
                    <p className="text-sm text-gray-600">{itemData.schoolName || `School ID: ${itemData.schoolId}`}</p>
                    <p className="text-xs text-gray-500 mt-1">{itemData.classLevel || itemData.class}</p>
                  </div>
                )}
                {donationType === "project" && (
                  <div>
                    <h4 className="font-semibold text-gray-900">{itemData.projectTitle || itemData.title}</h4>
                    <p className="text-sm text-gray-600 mt-1">{itemData.projectDescription || itemData.description}</p>
                    <p className="text-sm text-gray-600 mt-1">School ID: {itemData.schoolId}</p>
                    <p className="text-sm font-medium text-green-600 mt-2">Fund Needed: ৳{(itemData.requiredAmount || 0).toLocaleString()}</p>
                  </div>
                )}
              </div>
            )}

            <div className="space-y-2">
              <Label htmlFor="amount">Donation Amount (৳) *</Label>
              <Input
                id="amount"
                type="number"
                placeholder="e.g., 5000"
                value={formData.amount}
                onChange={(e) => setFormData({ ...formData, amount: e.target.value })}
                required
                data-testid="input-donation-amount"
              />
              <div className="flex gap-2 mt-2">
                {[1000, 5000, 10000, 25000].map((preset) => (
                  <Button
                    key={preset}
                    type="button"
                    variant="outline"
                    size="sm"
                    onClick={() => setFormData({ ...formData, amount: preset.toString() })}
                    data-testid={`button-preset-${preset}`}
                  >
                    Tk {preset.toLocaleString()}
                  </Button>
                ))}
              </div>
            </div>



            <div className="space-y-2">
              <Label htmlFor="message">Message (Optional)</Label>
              <Textarea
                id="message"
                placeholder="Add a message of encouragement..."
                value={formData.message}
                onChange={(e) => setFormData({ ...formData, message: e.target.value })}
                data-testid="input-donation-message"
              />
            </div>

            <div className="flex items-center space-x-2">
              <Checkbox
                id="anonymous"
                checked={formData.isAnonymous}
                onCheckedChange={(checked) => 
                  setFormData({ ...formData, isAnonymous: checked })
                }
                data-testid="checkbox-anonymous"
              />
              <Label htmlFor="anonymous" className="font-normal">
                Make this donation anonymous
              </Label>
            </div>
          </div>
          
          <DialogFooter>
            <Button type="button" variant="outline" onClick={() => onOpenChange(false)}>
              Cancel
            </Button>
            <Button type="submit" data-testid="button-submit-donation">
              Donate Tk {formData.amount ? parseInt(formData.amount).toLocaleString() : '0'}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}
