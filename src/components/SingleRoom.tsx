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
  return (
    <>
      <div className="backdrop-blur-md bg-white/30">
        {!isLoaded && (
          <>
            <Skeleton variant={"rounded"} width={100} height={75}/>
            <Skeleton variant={"text"} width={100} height={25} />
          </>
        )}

        <svg fill="red">
          <use href={`${svg_path}#ROOMS`} />
        </svg>
        <Image
          src={svg_path}
          alt={''}
          width={100}
          height={75}
          onLoadingComplete={() => setIsLoaded(true)}
        />
        {roomData.display_name}
      </div>
    </>
  );
};

export default SingleRoom