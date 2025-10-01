import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Progress } from "@/components/ui/progress";
import { Award, Trophy, Star, Heart, Sparkles } from "lucide-react";


const levelColors = {
  Bronze: "bg-amber-700 text-white",
  Silver: "bg-gray-400 text-gray-900",
  Gold: "bg-yellow-500 text-yellow-900",
  Platinum: "bg-purple-500 text-white",
};

const badgeIcons = {
  "School Supporter": Trophy,
  "Child Guardian": Heart,
  "Top Contributor": Star,
  "Early Adopter": Sparkles,
};

export default function DonorGamificationCard({
  currentLevel,
  totalPoints,
  pointsToNextLevel,
  earnedBadges,
  rankingPosition,
}) {
  const progressPercentage = (totalPoints / (totalPoints + pointsToNextLevel)) * 100;

  return (
    <Card>
      <CardHeader>
        <CardTitle className="text-lg">Achievement Progress</CardTitle>
      </CardHeader>
      <CardContent className="space-y-6">
        <div className="text-center space-y-3">
          <div className="inline-flex items-center justify-center h-20 w-20 rounded-full bg-primary/10">
            <Award className="h-10 w-10 text-primary" />
          </div>
          <div>
            <Badge 
              className={`${levelColors[currentLevel] || levelColors.Bronze} text-base px-4 py-1`}
              data-testid="badge-current-level"
            >
              {currentLevel}
            </Badge>
          </div>
          <div className="space-y-1">
            <p className="text-2xl font-bold font-mono" data-testid="text-total-points">{totalPoints.toLocaleString()}</p>
            <p className="text-sm text-muted-foreground">Total Points</p>
          </div>
        </div>

        <div className="space-y-2">
          <div className="flex justify-between text-sm">
            <span className="text-muted-foreground">Progress to next level</span>
            <span className="font-medium">{pointsToNextLevel} pts needed</span>
          </div>
          <Progress value={progressPercentage} className="h-2" />
        </div>

        {rankingPosition && (
          <div className="text-center p-3 bg-accent rounded-lg">
            <p className="text-sm text-muted-foreground">Regional Ranking</p>
            <p className="text-xl font-bold" data-testid="text-ranking-position">#{rankingPosition}</p>
          </div>
        )}

        <div>
          <h4 className="text-sm font-medium mb-3">Earned Badges</h4>
          <div className="grid grid-cols-2 gap-2">
            {earnedBadges.map((badge) => {
              const BadgeIcon = badgeIcons[badge] || Star;
              return (
                <div
                  key={badge}
                  className="flex flex-col items-center gap-2 p-3 rounded-lg bg-card border hover-elevate"
                  data-testid={`badge-earned-${badge.toLowerCase().replace(/\s+/g, '-')}`}
                >
                  <div className="h-10 w-10 rounded-full bg-chart-4/20 flex items-center justify-center">
                    <BadgeIcon className="h-5 w-5 text-chart-4" />
                  </div>
                  <p className="text-xs text-center font-medium leading-tight">{badge}</p>
                </div>
              );
            })}
          </div>
        </div>
      </CardContent>
    </Card>
  );
}
