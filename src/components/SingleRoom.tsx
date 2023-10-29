import {FC, useState} from "react";
import {Skeleton} from "@mui/material";
import Image from 'next/image';
import { Room } from "@types/room";

interface SingleRoomProps {
  roomData: Room;
  svg_path: string;
}

const SingleRoom: FC<SingleRoomProps> = (props) => {
  const { roomData, svg_path } = props;
  const [isLoaded, setIsLoaded] = useState(false);
  const statuses = ["free", "occupied", "reserved"]
  return (
    <>
      <div className="backdrop-blur-md bg-white/30">
        {!isLoaded && (
          <>
            <Skeleton variant={"rounded"} width={100} height={75}/>
            <br/>
            <Skeleton variant={"text"} width={100} height={25} />
          </>
        )}
        <div style={{display: isLoaded ? "block" : "none"}}>
          <Image
            src={svg_path}
            alt={roomData.display_name}
            width={100}
            height={75}
            className={statuses[roomData.status]}
            onLoadingComplete={() => setIsLoaded(true)}
          />
          <p>
            {roomData.display_name}
          </p>
        </div>
      </div>
    </>
  );
};

export default SingleRoom