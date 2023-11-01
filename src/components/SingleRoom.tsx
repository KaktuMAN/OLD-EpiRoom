import {FC, useState} from "react";
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
  const statuses = ["free", "occupied", "reserved"]
  roomData.status = Math.floor(Math.random() * 3);
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
          className={statuses[roomData.status]}
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