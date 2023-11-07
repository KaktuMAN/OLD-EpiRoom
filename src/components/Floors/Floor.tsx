import {FC, useEffect, useState} from "react";
import { Room } from "@customTypes/room";

interface FloorProps {
  rooms: Room[];
  floor: number;
  width: number;
  height: number;
}

const Floor: FC<FloorProps> = (props) => {
  const { rooms, floor, width, height } = props;
  const [xScale, setXScale] = useState(1.0);
  const [yScale, setYScale] = useState(1.0);
  const statuses = ["#F0405B", "#EBCA43", "#55FF99"]
  const wallColor = "#4F62EB";
  useEffect(() => {
    const backgroundRectangle = document.getElementById(`background${floor}`)?.getBoundingClientRect();
    if (!backgroundRectangle) return;
    setXScale((width / backgroundRectangle.width).toFixed(2) as unknown as number);
    setYScale((height / backgroundRectangle.height).toFixed(2) as unknown as number);
  })
  return (
    <div style={{height: height, width: width}}>
      <svg style={{height: "100%", width: "100%"}} id={"svg"}>
        {rooms.map((room) => {
          const key = room.intra_name.split('/').pop();
          if (room.floor !== floor) return null;
          return (<use href={`../rooms/${floor}/Z${floor}-Floor.svg#${key}`} fill={statuses[room.status]} stroke={statuses[room.status]} transform={`scale(${xScale}, ${yScale})`} key={key}/>)
        })}
        <use href={`../rooms/${floor}/Z${floor}-Floor.svg#floorRect`} fill={wallColor} transform={`scale(${xScale}, ${yScale})`}/>
        <use href={`../rooms/${floor}/Z${floor}-Floor.svg#floorLines`} stroke={wallColor} transform={`scale(${xScale}, ${yScale})`}/>
        <use href={`../rooms/${floor}/Z${floor}-Floor.svg#background`} id={`background${floor}`}/>
      </svg>
    </div>
  );
};

export default Floor