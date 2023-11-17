import {FC, useEffect, useState} from "react";
import { Room } from "@customTypes/room";
import { Activity } from "@customTypes/activity";
import {Skeleton} from "@mui/material";
import Image from 'next/image';

interface SingleRoomProps {
  roomData: Room;
  svg_path: string;
  maxHeight: number;
}

function formatTime(time: number): string {
  if (time / 60 > 1) {
    return `${Math.floor(time / 60)}h${time % 60}m`
  } else {
    return `${time % 60} minute${time % 60 > 1 ? "s" : ""}`
  }
}

const SingleRoom: FC<SingleRoomProps> = ({roomData, svg_path, maxHeight}) => {
  const [loaded, setLoaded] = useState(false);
  const [activity, setActivity] = useState(null as unknown as Activity);
  const floors = ["RDC", "1er Étage", "2e Étage", "3e Étage"];
  useEffect(() => {
    const interval = setInterval(() => {
      if (roomData.loaded) {
        setLoaded(true);
      }
      if (roomData.activities === undefined) {
        return
      }
      if (roomData.activities.length === 0) {
        roomData.setStatus(2);
      } else if (roomData.activities[0].start.valueOf() < Date.now() && roomData.activities[0].end.valueOf() > Date.now()) {
        setActivity(roomData.activities[0]);
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
    <div>
      {!loaded ? (
        <>
          <Skeleton variant={"rounded"} width={200} height={maxHeight / 100 * 50}/>
          <br/>
          <Skeleton variant={"rounded"} width={200} height={maxHeight / 100 * 20}/>
        </>
      ) : (
        <>
          <Image
            src={svg_path}
            alt={""}
            width={200}
            height={maxHeight / 100 * 50}
            className={["occupied", "reserved", "free"][roomData.status]}
          />
          <div style={{textAlign: "center"}}>
            {roomData.display_name} - {floors[roomData.floor]}
            {roomData.status < 2 ? (
              <>
                <br/>
                <div>{activity.title}</div>
                <br/>
                {roomData.status == 0 ? (
                  <>
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
        </>
        )}
    </div>
  );
};

export default SingleRoom