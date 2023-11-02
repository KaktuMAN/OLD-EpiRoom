import {FC, useState} from "react";
import { Room } from "@customTypes/room";

interface FloorProps {
  rooms: Room[];
  floor: number;
  width: number;
  height: number;
}

const Floor: FC<FloorProps> = (props) => {
  const { rooms, floor, width, height } = props;
  const [scale, setScale] = useState(0.2);
  const statuses = ["#ff0000", "#ff8401", "#00ff75"]
  const wallColor = "#003dff";
  return (
    <>
      <svg width={width} height={height} preserveAspectRatio={"true"}>
        {rooms.map((room) => {
          const key = room.intra_name.split('/').pop();
          if (room.floor !== floor) return null;
          return (<use href={`../../rooms/${floor}/Z${floor}-Floor.svg#${key}`} fill={statuses[room.status]} stroke={statuses[room.status]} transform={`scale(${scale})`} key={key}/>)
        })}
        <use href={`../../rooms/${floor}/Z${floor}-Floor.svg#floorRect`} fill={wallColor} transform={`scale(${scale})`}/>
        <use href={`../../rooms/${floor}/Z${floor}-Floor.svg#floorLines`} stroke={wallColor} transform={`scale(${scale})`}/>
      </svg>
    </>
  );
};

export default Floor