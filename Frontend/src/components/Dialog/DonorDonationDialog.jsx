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


export default function DonorDonationDialog({ 
  open, 
  onOpenChange, 
  projectTitle,
  projectId 
}) {
  const { toast } = useToast();
  const [formData, setFormData] = useState({
    amount: "",
    paymentMethod: "",
    donationType: projectId ? "project" : "general",
    message: "",
    isAnonymous: false,
  });

  const handleSubmit = (e) => {
    e.preventDefault();
    
    toast({
      title: "Donation Successful",
      description: `Your donation of ৳${parseInt(formData.amount).toLocaleString()} has been processed.`,
    });
    
    setFormData({
      amount: "",
      paymentMethod: "",
      donationType: projectId ? "project" : "general",
      message: "",
      isAnonymous: false,
    });
    
    onOpenChange(false);
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[500px]">
        <DialogHeader>
          <DialogTitle>Make a Donation</DialogTitle>
          <DialogDescription>
            {projectTitle 
              ? `Support: ${projectTitle}` 
              : "Choose how you want to support education in Bangladesh"}
          </DialogDescription>
        </DialogHeader>
        
        <form onSubmit={handleSubmit}>
          <div className="grid gap-4 py-4">
            {!projectId && (
              <div className="space-y-2">
                <Label>Donation Type</Label>
                <RadioGroup
                  value={formData.donationType}
                  onValueChange={(value) => setFormData({ ...formData, donationType: value })}
                >
                  <div className="flex items-center space-x-2">
                    <RadioGroupItem value="project" id="project" />
                    <Label htmlFor="project" className="font-normal">
                      Support a specific project
                    </Label>
                  </div>
                  <div className="flex items-center space-x-2">
                    <RadioGroupItem value="student" id="student" />
                    <Label htmlFor="student" className="font-normal">
                      Sponsor a student
                    </Label>
                  </div>
                  <div className="flex items-center space-x-2">
                    <RadioGroupItem value="general" id="general" />
                    <Label htmlFor="general" className="font-normal">
                      General fund (We'll allocate to high-priority needs)
                    </Label>
                  </div>
                </RadioGroup>
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
                    ৳{preset.toLocaleString()}
                  </Button>
                ))}
              </div>
            </div>

            <div className="space-y-2">
              <Label htmlFor="paymentMethod">Payment Method *</Label>
              <Select
                value={formData.paymentMethod}
                onValueChange={(value) => setFormData({ ...formData, paymentMethod: value })}
                required
              >
                <SelectTrigger data-testid="select-payment-method">
                  <SelectValue placeholder="Select payment method" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="bkash">bKash</SelectItem>
                  <SelectItem value="nagad">Nagad</SelectItem>
                  <SelectItem value="rocket">Rocket</SelectItem>
                  <SelectItem value="bank">Bank Transfer</SelectItem>
                  <SelectItem value="card">Credit/Debit Card</SelectItem>
                </SelectContent>
              </Select>
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
              Donate ৳{formData.amount ? parseInt(formData.amount).toLocaleString() : '0'}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}
