import {FC, useEffect, useState} from "react";
import { Room } from "@customTypes/room";
import { Activity } from "@customTypes/activity";
import {Skeleton} from "@mui/material";
import Image from 'next/image';

interface SingleRoomProps {
  roomData: Room;
  svg_path: string;
}

const SingleRoom: FC<SingleRoomProps> = ({roomData, svg_path}) => {
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
          <Skeleton variant={"rounded"} width={200} height={120}/>
          <br/>
          <Skeleton variant={"rounded"} width={200} height={25}/>
        </>
      ) : (
        <>
          <Image
            src={svg_path}
            alt={""}
            width={200}
            height={100}
            className={["occupied", "reserved", "free"][roomData.status]}
          />
          <p>
            {roomData.display_name} ({floors[roomData.floor]})
            {activity ? (
              <>
                <br/>
                {activity.title}
              </>
            ) : null}
          </p>
        </>
        )}
    </div>
  );
};

export default SingleRoom