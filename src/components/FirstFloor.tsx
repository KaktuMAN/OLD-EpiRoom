import {FC} from "react";
import { Room } from "@customTypes/room";

interface FirstFloorProps {
  roomData: Room[];
}

const FirstFloor: FC<FirstFloorProps> = (props) => {
  const { roomData } = props;
  const statuses = ["free", "occupied", "reserved"]
  return (
    <>
      <svg width="1679" height="920">
        <use href={"/rooms/1/WALLS.svg#VUE_3D-2"}/>
        <use href={"/rooms/1/0-11-Kanojedo.svg#ROOMS"} x={535} y={215}/>
      </svg>
    </>
  );
};

export default FirstFloor