package bdbt_bada_project.SpringApplication.schedules;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class Schedule {
    private int scheduleNumber;
    private String day;
    private LocalTime startTime;
    private LocalTime endTime;
    int dayNumber;

    public Schedule(int scheduleNumber, String day, LocalTime startTime, LocalTime endTime) {
        this.scheduleNumber = scheduleNumber;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayNumber = getDayNumberByName(this.day);
    }
    public Schedule(){}

    public int getDayNumberByName(String dayName){
        if(dayName.equals("PN")){
            return 1;
        } else if (dayName.equals("WT")) {
            return 2;
        } else if (dayName.equals("SR")) {
            return 3;
        } else if (dayName.equals("CZW")) {
            return 4;
        } else if (dayName.equals("PT")) {
            return 5;
        } else if (dayName.equals("SB")) {
            return 6;
        } else if (dayName.equals("ND")) {
            return 7;
        } else{
            return 0;
        }
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "scheduleNumber=" + scheduleNumber +
                ", day='" + day + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
