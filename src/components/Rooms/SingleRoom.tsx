import {FC, useEffect, useState} from "react";
import { Room } from "@customTypes/room";
import {Skeleton} from "@mui/material";
import Image from 'next/image';

interface SingleRoomProps {
  roomData: Room;
  svg_path: string;
}

const SingleRoom: FC<SingleRoomProps> = (props) => {
  const { roomData, svg_path } = props;
  const [loaded, setLoaded] = useState(false);
  useEffect(() => {
    const waitLoad = setInterval(() => {
      if (roomData.loaded) {
        clearInterval(waitLoad);
        setLoaded(true);
      }
    }, 1000);
    const interval = setInterval(() => {
      if (roomData.activities.length === 0) {
        roomData.setStatus(2);
      } else if (roomData.activities[0].start.valueOf() < Date.now() && roomData.activities[0].end.valueOf() > Date.now()) {
        roomData.setStatus(0);
      } else if (roomData.activities[0].start.valueOf() > Date.now()) {
        roomData.setStatus(1);
      } else {
        roomData.setStatus(2);
        roomData.activities.pop();
      }
    }, 1000);
    return () => {
      clearInterval(interval);
    };
  });
  return (
    <>
      <div className="backdrop-blur-md bg-white/30">
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
              {roomData.display_name}
            </p>
          </>
          )}
      </div>
    </>
  );
};

export default SingleRoom