import {FC, useEffect, useState} from "react";
import {Skeleton} from "@mui/material";
import Image from 'next/image';
import { Room } from "@customTypes/room";

interface SingleRoomProps {
  roomData: Room;
  svg_path: string;
}

const SingleRoom: FC<SingleRoomProps> = (props) => {
  const { roomData, svg_path } = props;
  const [isLoaded, setIsLoaded] = useState(false);
  const [status, setStatus] = useState(0);
  useEffect(() => {
    const interval = setInterval(() => {
      if (roomData.activities.length === 0) {
        setStatus(0);
      } else if (roomData.activities[0].start.valueOf() < Date.now() && roomData.activities[0].end.valueOf() > Date.now()) {
        setStatus(2);
      } else if (roomData.activities[0].start.valueOf() > Date.now()) {
        setStatus(1);
      } else {
        setStatus(0);
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
        {!isLoaded && (
          <Skeleton variant={"rounded"} width={150} height={120}/>
        )}
        <Image
          src={svg_path}
          alt={""}
          width={150}
          height={100}
          className={["free", "occupied", "reserved"][status]}
          onLoadingComplete={() => setIsLoaded(true)}
        />
        <p>
          {roomData.display_name}
        </p>
      </div>
    </>
  );
};

export default SingleRoom