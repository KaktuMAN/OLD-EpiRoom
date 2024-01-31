import {FC, useEffect, useState} from "react";
import { Room } from "@customTypes/room";
import { Activity } from "@customTypes/activity";
import {Slider} from "@mui/material";

interface SingleRoomProps {
  roomData: Room;
  maxHeight: number;
}

function formatTime(time: number): string {
  if (time / 60 > 1) {
    return `${Math.floor(time / 60)}h${time % 60}m`
  } else {
    return `${time % 60} minute${time % 60 > 1 ? "s" : ""}`
  }
}

const SingleRoom: FC<SingleRoomProps> = ({roomData, maxHeight}) => {
  const [activity, setActivity] = useState(null as unknown as Activity);
  const [remainingTime, setRemainingTime] = useState(0);
  const [activityTime, setActivityTime] = useState(0);
  const floors = ["RDC", "1er Étage", "2e Étage", "3e Étage"];
  useEffect(() => {
    const interval = setInterval(() => {
      if (roomData.activities === undefined) {
        return
      }
      if (roomData.activities.length === 0) {
        roomData.setStatus(2);
      } else if (roomData.activities[0].start.valueOf() < Date.now() && roomData.activities[0].end.valueOf() > Date.now()) {
        setActivity(roomData.activities[0]);
        setActivityTime(Math.floor((roomData.activities[0].end.valueOf() - roomData.activities[0].start.valueOf()) / 1000 / 60));
        setRemainingTime(Math.floor((roomData.activities[0].end.valueOf() - Date.now()) / 1000 / 60));
        roomData.setStatus(0);
      } else if (roomData.activities[0].start.valueOf() > Date.now()) {
        setActivity(roomData.activities[0]);
        roomData.setStatus(1);
      } else {
        setActivity(null as unknown as Activity)
        roomData.setStatus(2);
        roomData.activities.pop();
      }
    }, 1000);
    return () => clearInterval(interval);
  });
  return (
    <div style={{height: maxHeight - 25}}>
      <div style={{width: "250px", height: "100%", textAlign: "center", color: "#000000"}} className={["occupied", "reserved", "free"][roomData.status]}>
        {roomData.display_name} - {floors[roomData.floor]}
        {roomData.status < 2 ? (
          <>
            <br/>
            <div>{activity.title}</div>
            <br/>
            {roomData.status == 0 ? (
              <>
                <div style={{whiteSpace: "nowrap", alignContent: "center", height: "30px"}}>
                  {activity.start.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'})}
                  <Slider min={0} max={100} value={100 - 100 / activityTime * remainingTime} style={{width: "155px", height: "15px", padding: "0", margin: "-80 5px"}}/>
                  {activity.end.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'})}
                </div>
                Encore {formatTime(Math.floor((activity.end.valueOf() - Date.now()) / 1000 / 60))}
              </>
            ) : roomData.status == 1 ? (
              <>
                Début dans {formatTime(Math.floor((activity.start.valueOf() - Date.now()) / 1000 / 60))}
              </>
            ) : null}
          </>
        ) : null}
      </div>
    </div>
  );
};

export default SingleRoom