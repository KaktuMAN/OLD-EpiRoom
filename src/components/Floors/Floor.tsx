import {FC} from "react";
import { Room } from "@customTypes/room";

interface FloorProps {
  rooms: Room[];
  floor: number;
}

const Floor: FC<FloorProps> = (props) => {
  const { rooms, floor } = props;
  const statuses = ["#ff0000", "#ff8401", "#00ff75"]
  const wallColor = "#003dff";
  return (
    <>
      <svg width={1250} height={750}>
        {rooms.map((room) => {
          const key = room.intra_name.split('/').pop();
          if (room.floor !== floor) return null;
          return (<use href={`../../rooms/${floor}/Z${floor}-Floor.svg#${key}`} fill={statuses[room.status]} stroke={statuses[room.status]} transform="scale(0.7)" key={key}/>)
        })}
        <use href={`../../rooms/${floor}/Z${floor}-Floor.svg#floorRect`} fill={wallColor} transform="scale(0.7)"/>
        <use href={`../../rooms/${floor}/Z${floor}-Floor.svg#floorLines`} stroke={wallColor} transform="scale(0.7)"/>
      </svg>
    </>
  );
};

export default Floor