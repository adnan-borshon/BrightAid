import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { MapPin, Users, School, Phone, Mail } from "lucide-react";

export function SchoolDetailsDialog({ open, onOpenChange, school }) {
  if (!school) return null;

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[600px]">
        <DialogHeader>
          <DialogTitle className="text-2xl">{school.name}</DialogTitle>
          <DialogDescription>
            School Information and Current Needs
          </DialogDescription>
        </DialogHeader>

        <div className="space-y-6">
          <div className="flex items-start justify-between">
            <div className="space-y-2">
              <div className="flex items-center gap-2 text-sm">
                <MapPin className="h-4 w-4 text-muted-foreground" />
                <span>{school.location}</span>
              </div>
              <div className="flex items-center gap-2 text-sm">
                <Users className="h-4 w-4 text-muted-foreground" />
                <span>{school.students} students</span>
              </div>
              <div className="flex items-center gap-2 text-sm">
                <School className="h-4 w-4 text-muted-foreground" />
                <span>{school.activeProjects} active projects</span>
              </div>
            </div>
            <Badge variant={school.needLevel === 'critical' ? 'destructive' : 'secondary'}>
              {school.needLevel} priority
            </Badge>
          </div>

          <div className="space-y-3">
            <h3 className="font-semibold">Current Needs</h3>
            <p className="text-sm text-muted-foreground">
              This school is currently seeking support for infrastructure improvements, 
              educational resources, and student welfare programs.
            </p>
          </div>

          <div className="space-y-3">
            <h3 className="font-semibold">Contact Information</h3>
            <div className="space-y-2">
              <div className="flex items-center gap-2 text-sm">
                <Phone className="h-4 w-4 text-muted-foreground" />
                <span>+880-XXX-XXXX</span>
              </div>
              <div className="flex items-center gap-2 text-sm">
                <Mail className="h-4 w-4 text-muted-foreground" />
                <span>school@example.com</span>
              </div>
            </div>
          </div>
        </div>

        <div className="flex justify-end gap-2 pt-4">
          <Button variant="outline" onClick={() => onOpenChange(false)}>
            Close
          </Button>
          <Button>
            Support This School
          </Button>
        </div>
      </DialogContent>
    </Dialog>
  );
}