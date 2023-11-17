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
  useEffect(() => {
    const background = document.getElementById(`background_${floor}`);
    if (!background) return;
    background.style.display = ""
    setXScale((width / background.getBoundingClientRect().width).toFixed(2) as unknown as number);
    setYScale((height / background.getBoundingClientRect().height).toFixed(2) as unknown as number);
    background.style.display = "none"
  })
  return (
    <div style={{height: height, width: width}}>
      <svg style={{height: "100%", width: "100%"}} id={"svg"}>
        {rooms.map((room) => {
          const key = room.intra_name.split('/').pop();
          if (room.floor !== floor) return null;
          return (<use href={`../rooms/${floor}/Z${floor}-Floor.svg#${key}`} fill={statuses[room.status]} stroke={statuses[room.status]} transform={`scale(${xScale}, ${yScale})`} key={key}/>)
        })}
        <use href={`../rooms/${floor}/Z${floor}-Floor.svg#floor`} transform={`scale(${xScale}, ${yScale})`}/>
        <use href={`../rooms/${floor}/Z${floor}-Floor.svg#background`} id={`background_${random}`} style={{display: "none"}}/>
      </svg>
    </div>
  );
};

export default Floor