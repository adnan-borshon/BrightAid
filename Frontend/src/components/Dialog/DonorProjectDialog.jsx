import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Progress } from "@/components/ui/progress";
import { Separator } from "@/components/ui/separator";
import { ScrollArea } from "@/components/ui/scroll-area";
import { MapPin, Calendar, DollarSign, Users, TrendingUp } from "lucide-react";


export default function DonorProjectDialog({ 
  open, 
  onOpenChange, 
  project,
  onDonate 
}) {
  if (!project) return null;

  const progressPercentage = (project.raisedAmount / project.requiredAmount) * 100;
  const remaining = project.requiredAmount - project.raisedAmount;

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[700px]">
        <DialogHeader>
          <div className="flex items-start justify-between gap-4">
            <div className="flex-1">
              <DialogTitle className="text-2xl">{project.title}</DialogTitle>
              <DialogDescription className="mt-2">
                {project.schoolName}
              </DialogDescription>
            </div>
            <div className="flex gap-2">
              <Badge variant="outline">{project.status}</Badge>
              <Badge variant="outline">{project.projectType}</Badge>
            </div>
          </div>
        </DialogHeader>

        <ScrollArea className="max-h-[500px] pr-4">
          <div className="space-y-6">
            {/* Funding Progress */}
            <div className="space-y-3">
              <div className="flex justify-between items-baseline">
                <h3 className="font-semibold">Funding Progress</h3>
                <span className="text-2xl font-bold font-mono">
                  {progressPercentage.toFixed(1)}%
                </span>
              </div>
              <Progress value={progressPercentage} className="h-3" />
              <div className="flex justify-between text-sm">
                <span className="font-medium">
                  ৳{project.raisedAmount.toLocaleString()} raised
                </span>
                <span className="text-muted-foreground">
                  ৳{remaining.toLocaleString()} remaining
                </span>
              </div>
            </div>

            <Separator />

            {/* Project Details */}
            <div className="space-y-4">
              <h3 className="font-semibold">Project Details</h3>
              <p className="text-muted-foreground leading-relaxed">
                {project.description}
              </p>
              
              <div className="grid grid-cols-2 gap-4">
                <div className="flex items-center gap-2 text-sm">
                  <MapPin className="h-4 w-4 text-muted-foreground" />
                  <span>{project.location}</span>
                </div>
                {project.deadline && (
                  <div className="flex items-center gap-2 text-sm">
                    <Calendar className="h-4 w-4 text-muted-foreground" />
                    <span>Due: {project.deadline}</span>
                  </div>
                )}
                <div className="flex items-center gap-2 text-sm">
                  <DollarSign className="h-4 w-4 text-muted-foreground" />
                  <span>Goal: ৳{project.requiredAmount.toLocaleString()}</span>
                </div>
                <div className="flex items-center gap-2 text-sm">
                  <Users className="h-4 w-4 text-muted-foreground" />
                  <span>42 donors supporting</span>
                </div>
              </div>
            </div>

            <Separator />

            {/* Impact */}
            <div className="space-y-3">
              <h3 className="font-semibold">Expected Impact</h3>
              <div className="grid gap-3">
                <div className="flex items-start gap-3 p-3 rounded-lg bg-accent/50">
                  <TrendingUp className="h-5 w-5 text-chart-2 mt-0.5" />
                  <div>
                    <p className="font-medium text-sm">500+ students will benefit</p>
                    <p className="text-xs text-muted-foreground">
                      Direct improvement in learning environment
                    </p>
                  </div>
                </div>
                <div className="flex items-start gap-3 p-3 rounded-lg bg-accent/50">
                  <TrendingUp className="h-5 w-5 text-chart-2 mt-0.5" />
                  <div>
                    <p className="font-medium text-sm">Reduce dropout risk by 30%</p>
                    <p className="text-xs text-muted-foreground">
                      Better facilities encourage continued education
                    </p>
                  </div>
                </div>
              </div>
            </div>

            <Separator />

            {/* Recent Updates */}
            <div className="space-y-3">
              <h3 className="font-semibold">Recent Updates</h3>
              <div className="space-y-3">
                <div className="border-l-2 border-primary pl-4">
                  <p className="text-sm font-medium">Construction phase started</p>
                  <p className="text-xs text-muted-foreground">2 days ago</p>
                </div>
                <div className="border-l-2 border-muted pl-4">
                  <p className="text-sm font-medium">Planning approved by school board</p>
                  <p className="text-xs text-muted-foreground">1 week ago</p>
                </div>
              </div>
            </div>
          </div>
        </ScrollArea>

        <DialogFooter className="gap-2">
          <Button variant="outline" onClick={() => onOpenChange(false)}>
            Close
          </Button>
          <Button onClick={onDonate} data-testid="button-donate-from-details">
            <DollarSign className="h-4 w-4 mr-2" />
            Donate to This Project
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
