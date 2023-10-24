import {FC, useState} from "react";
import {Skeleton} from "@mui/material";
import {Room} from "../pages";

interface SingleRoomProps {
  roomData: Room;
  svg_path: string;
}

const SingleRoom: FC<SingleRoomProps> = (props) => {
  const {roomData, svg_path} = props;
  return (
    <>
      <div>
        <Skeleton variant={"rectangular"} width={100} height={75}/>
        <Skeleton variant={"text"} width={100} height={25}/>
        {roomData.display_name}
        {roomData.status}
      </div>
    </>
  );
};

export default SingleRoom