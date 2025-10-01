import { useState } from "react";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { ScrollArea } from "@/components/ui/scroll-area";
import { SchoolDetailsDialog } from "./SchoolDetailsDialog";
import { Search, MapPin, Users } from "lucide-react";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { useDonor } from "@/context/DonorContext";


//todo: remove mock functionality
const mockSchools = [
  {
    id: "1",
    name: "Shibpur Primary School",
    location: "Rangpur Division, Rangpur District",
    students: 500,
    needLevel: "high",
    activeProjects: 2,
  },
  {
    id: "2",
    name: "Mirpur Girls High School",
    location: "Dhaka Division, Dhaka District",
    students: 800,
    needLevel: "medium",
    activeProjects: 1,
  },
  {
    id: "3",
    name: "Kushtia Community School",
    location: "Khulna Division, Kushtia District",
    students: 300,
    needLevel: "critical",
    activeProjects: 3,
  },
];

const needLevelColors = {
  low: "bg-chart-2/20 text-chart-2",
  medium: "bg-chart-5/20 text-chart-5",
  high: "bg-destructive/20 text-destructive",
  critical: "bg-destructive text-destructive-foreground",
};



export default function DonorBrowseSchoolDialog({ open, onOpenChange }) {
  const { schoolsData } = useDonor();
  const [searchTerm, setSearchTerm] = useState("");
  const [division, setDivision] = useState("");
  const [selectedSchool, setSelectedSchool] = useState(null);
  const [schoolDetailsOpen, setSchoolDetailsOpen] = useState(false);

  // Filter schools based on search and division
  const filteredSchools = schoolsData.filter(school => {
    const matchesSearch = school.schoolName?.toLowerCase().includes(searchTerm.toLowerCase()) || 
                         school.location?.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesDivision = !division || division === 'all' || 
                           school.location?.toLowerCase().includes(division.toLowerCase());
    return matchesSearch && matchesDivision;
  });

  const handleViewSchool = (school) => {
    setSelectedSchool(school);
    setSchoolDetailsOpen(true);
  };

  return (
    <>
      <SchoolDetailsDialog
        open={schoolDetailsOpen}
        onOpenChange={setSchoolDetailsOpen}
        school={selectedSchool}
      />
      <Dialog open={open} onOpenChange={onOpenChange}>
        <DialogContent className="sm:max-w-[700px]">
          <DialogHeader>
            <DialogTitle>Browse Schools</DialogTitle>
            <DialogDescription>
              Find schools in need of support across Bangladesh
            </DialogDescription>
          </DialogHeader>
          
          <div className="space-y-4">
            <div className="flex gap-2">
              <div className="relative flex-1">
                <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
                <Input
                  placeholder="Search schools..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-9"
                  data-testid="input-search-schools"
                />
              </div>
              <Select value={division} onValueChange={setDivision}>
                <SelectTrigger className="w-[200px]" data-testid="select-division">
                  <SelectValue placeholder="All Divisions" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="all">All Divisions</SelectItem>
                  <SelectItem value="dhaka">Dhaka</SelectItem>
                  <SelectItem value="chittagong">Chittagong</SelectItem>
                  <SelectItem value="rajshahi">Rajshahi</SelectItem>
                  <SelectItem value="khulna">Khulna</SelectItem>
                  <SelectItem value="sylhet">Sylhet</SelectItem>
                  <SelectItem value="rangpur">Rangpur</SelectItem>
                </SelectContent>
              </Select>
            </div>

            <ScrollArea className="h-[400px] pr-4">
              <div className="space-y-3">
                {filteredSchools.map((school) => (
                  <div
                    key={school.id}
                    className="flex items-start justify-between gap-4 rounded-lg border p-4 hover-elevate"
                    data-testid={`school-card-${school.id}`}
                  >
                    <div className="flex-1 space-y-2">
                      <div className="flex items-start justify-between gap-2">
                        <h4 className="font-semibold">{school.schoolName || school.name}</h4>
                        <Badge className="bg-green-100 text-green-700">
                          active
                        </Badge>
                      </div>
                      <div className="flex items-center gap-4 text-sm text-muted-foreground">
                        <div className="flex items-center gap-1">
                          <MapPin className="h-4 w-4" />
                          {school.location || 'Location not specified'}
                        </div>
                        <div className="flex items-center gap-1">
                          <Users className="h-4 w-4" />
                          {school.totalStudents || 0} students
                        </div>
                      </div>
                      <p className="text-sm text-muted-foreground">
                        School ID: {school.id}
                      </p>
                    </div>
                    <Button 
                      size="sm" 
                      onClick={() => handleViewSchool(school)}
                      data-testid={`button-view-school-${school.id}`}
                    >
                      View School
                    </Button>
                  </div>
                ))}
              </div>
            </ScrollArea>
          </div>
        </DialogContent>
      </Dialog>
    </>
  );
}
