import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { ScrollArea } from "@/components/ui/scroll-area";
import { DollarSign, TrendingUp, CheckCircle, Clock } from "lucide-react";



const activityIcons = {
  donation: DollarSign,
  project_update: TrendingUp,
  milestone: CheckCircle,
  verification: Clock,
};

const activityColors = {
  donation: "bg-chart-1/10 text-chart-1",
  project_update: "bg-chart-2/10 text-chart-2",
  milestone: "bg-chart-3/10 text-chart-3",
  verification: "bg-chart-5/10 text-chart-5",
};

export default function DonorRecentActivityFeed({ activities }) {
  return (
    <Card>
      <CardHeader>
        <CardTitle className="text-lg">Recent Activity</CardTitle>
      </CardHeader>
      <CardContent>
        <ScrollArea className="h-[400px] pr-4">
          <div className="space-y-4">
            {activities.map((activity) => {
              const Icon = activityIcons[activity.type];
              return (
                <div
                  key={activity.id}
                  className="flex gap-3 pb-4 border-b last:border-0"
                  data-testid={`activity-${activity.id}`}
                >
                  <div className={`flex h-10 w-10 flex-shrink-0 items-center justify-center rounded-lg ${activityColors[activity.type]}`}>
                    <Icon className="h-5 w-5" />
                  </div>
                  <div className="flex-1 space-y-1">
                    <p className="text-sm font-medium leading-tight">
                      {activity.title}
                    </p>
                    <p className="text-sm text-muted-foreground">
                      {activity.description}
                    </p>
                    <p className="text-xs text-muted-foreground">
                      {activity.timestamp}
                    </p>
                  </div>
                </div>
              );
            })}
          </div>
        </ScrollArea>
      </CardContent>
    </Card>
  );
}
