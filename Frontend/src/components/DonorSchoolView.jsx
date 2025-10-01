import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Separator } from "@/components/ui/separator";
import { ScrollArea } from "@/components/ui/scroll-area";
import { Card, CardContent } from "@/components/ui/card";
import { MapPin, Users, Building2, TrendingUp, DollarSign } from "lucide-react";


const needLevelColors = {
  low: "bg-chart-2/20 text-chart-2",
  medium: "bg-chart-5/20 text-chart-5",
  high: "bg-destructive/20 text-destructive",
  critical: "bg-destructive text-destructive-foreground",
};

//todo: remove mock functionality
const mockSchoolData = {
  '1': {
    teachers: 25,
    facilities: ['Library', 'Computer Lab', 'Sports Ground', 'Science Lab'],
    recentUpdates: [
      { date: '2025-01-10', title: 'New water filtration system installed', type: 'infrastructure' },
      { date: '2025-01-05', title: '50 students sponsored this month', type: 'sponsorship' },
    ],
    totalFunding: 450000,
    ongoingProjects: [
      { name: 'Clean Water Infrastructure', progress: 65, budget: 150000 },
      { name: 'Student Meal Program', progress: 40, budget: 80000 },
    ],
  },
  '2': {
    teachers: 35,
    facilities: ['Library', 'Computer Lab', 'Auditorium', 'Science Lab', 'Cafeteria'],
    recentUpdates: [
      { date: '2025-01-12', title: 'Digital classroom equipment delivered', type: 'education' },
    ],
    totalFunding: 780000,
    ongoingProjects: [
      { name: 'Digital Classroom Equipment', progress: 90, budget: 200000 },
    ],
  },
  '3': {
    teachers: 18,
    facilities: ['Library', 'Sports Ground'],
    recentUpdates: [
      { date: '2025-01-14', title: 'Emergency roof repair started', type: 'infrastructure' },
    ],
    totalFunding: 220000,
    ongoingProjects: [
      { name: 'Emergency Roof Repair', progress: 30, budget: 80000 },
      { name: 'Library Books Collection', progress: 55, budget: 45000 },
    ],
  },
};

export default function DonorSchoolView({ open, onOpenChange, school }) {
  if (!school) return null;

  const schoolData = mockSchoolData[school.id] || mockSchoolData['1'];

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[800px]">
        <DialogHeader>
          <div className="flex items-start justify-between gap-4">
            <div className="flex-1">
              <DialogTitle className="text-2xl">{school.name}</DialogTitle>
              <DialogDescription className="mt-2 flex items-center gap-2">
                <MapPin className="h-4 w-4" />
                {school.location}
              </DialogDescription>
            </div>
            <Badge className={needLevelColors[school.needLevel]}>
              {school.needLevel} need
            </Badge>
          </div>
        </DialogHeader>

        <ScrollArea className="max-h-[600px] pr-4">
          <div className="space-y-6">
            {/* Key Stats */}
            <div className="grid grid-cols-3 gap-4">
              <Card>
                <CardContent className="p-4 text-center">
                  <Users className="h-6 w-6 mx-auto mb-2 text-chart-1" />
                  <div className="text-2xl font-bold font-mono">{school.students}</div>
                  <p className="text-xs text-muted-foreground">Students</p>
                </CardContent>
              </Card>
              <Card>
                <CardContent className="p-4 text-center">
                  <Users className="h-6 w-6 mx-auto mb-2 text-chart-2" />
                  <div className="text-2xl font-bold font-mono">{schoolData.teachers}</div>
                  <p className="text-xs text-muted-foreground">Teachers</p>
                </CardContent>
              </Card>
              <Card>
                <CardContent className="p-4 text-center">
                  <TrendingUp className="h-6 w-6 mx-auto mb-2 text-chart-3" />
                  <div className="text-2xl font-bold font-mono">{school.activeProjects}</div>
                  <p className="text-xs text-muted-foreground">Active Projects</p>
                </CardContent>
              </Card>
            </div>

            <Separator />

            {/* Facilities */}
            <div className="space-y-3">
              <div className="flex items-center gap-2">
                <Building2 className="h-5 w-5 text-muted-foreground" />
                <h3 className="font-semibold">Facilities & Infrastructure</h3>
              </div>
              <div className="flex flex-wrap gap-2">
                {schoolData.facilities.map((facility) => (
                  <Badge key={facility} variant="secondary">
                    {facility}
                  </Badge>
                ))}
              </div>
            </div>

            <Separator />

            {/* Ongoing Projects */}
            <div className="space-y-3">
              <div className="flex items-center gap-2">
                <DollarSign className="h-5 w-5 text-muted-foreground" />
                <h3 className="font-semibold">Ongoing Projects</h3>
              </div>
              <div className="space-y-3">
                {schoolData.ongoingProjects.map((project, index) => (
                  <div key={index} className="p-4 rounded-lg border space-y-2">
                    <div className="flex justify-between items-start">
                      <p className="font-medium">{project.name}</p>
                      <span className="text-sm text-muted-foreground">
                        ৳{project.budget.toLocaleString()}
                      </span>
                    </div>
                    <div className="flex items-center gap-2">
                      <div className="flex-1 h-2 bg-secondary rounded-full overflow-hidden">
                        <div 
                          className="h-full bg-chart-1 transition-all" 
                          style={{ width: `${project.progress}%` }}
                        />
                      </div>
                      <span className="text-sm font-medium">{project.progress}%</span>
                    </div>
                  </div>
                ))}
              </div>
              <div className="p-3 rounded-lg bg-accent/50">
                <p className="text-sm font-medium">
                  Total Funding Received: ৳{schoolData.totalFunding.toLocaleString()}
                </p>
              </div>
            </div>

            <Separator />

            {/* Recent Updates */}
            <div className="space-y-3">
              <h3 className="font-semibold">Recent Updates</h3>
              <div className="space-y-3">
                {schoolData.recentUpdates.map((update, index) => (
                  <div key={index} className="flex gap-3">
                    <div className="flex-shrink-0 w-2 bg-primary rounded-full" />
                    <div className="flex-1">
                      <p className="text-sm font-medium">{update.title}</p>
                      <p className="text-xs text-muted-foreground">
                        {new Date(update.date).toLocaleDateString('en-BD')}
                      </p>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </ScrollArea>

        <div className="flex gap-2 pt-4">
          <Button variant="outline" onClick={() => onOpenChange(false)} className="flex-1">
            Close
          </Button>
          <Button className="flex-1" onClick={() => console.log('Support school')}>
            Support This School
          </Button>
        </div>
      </DialogContent>
    </Dialog>
  );
}
