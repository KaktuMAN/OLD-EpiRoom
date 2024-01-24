import {FC, useEffect, useState} from "react";
import {Tooltip} from "@mui/material";
import { Room } from "@customTypes/room";

interface FloorProps {
  rooms: Room[];
  town: string;
  floor: number;
}

const Floor: FC<FloorProps> = ({ rooms, town,  floor}) => {
  const [scale, setScale] = useState([1, 1]);
  useEffect(() => {
    const background = document.getElementById(`background_${floor}`);
    const svg = document.getElementById(`floorRender${floor}`);
    if (!svg || !background) return;
    const {width: svgWidth, height: svgHeight} = svg.getBoundingClientRect();
    background.style.display = "block";
    const {width, height} = background.getBoundingClientRect();
    background.style.display = "none";
    setScale([svgWidth / width, svgHeight / height]);
  }, [floor]);
  return (
    <div style={{width: "100%", height: "100%"}}>
      <svg id={`floorRender${floor}`}>
        {rooms.map((room: Room) => {
          if (room.floor !== floor) return null;
          return (
            <>
              <Tooltip title={room.display_name} arrow style={{backgroundColor: "white", color: "black"}}>
                <use href={`/towns/${town}/svg/${floor}/Z${floor}-Floor.svg#${room.intra_name.split('/').pop()}`} className={["occupied", "reserved", "free"][room.status]} transform={`scale(${scale[0]}, ${scale[1]})`}/>
              </Tooltip>
            </>
          )
        })}
        <use href={`/towns/${town}/svg/${floor}/Z${floor}-Floor.svg#floor`} transform={`scale(${scale[0]}, ${scale[1]})`}/>
        <use href={`/towns/${town}/svg/${floor}/Z${floor}-Floor.svg#background`} id={`background_${floor}`} style={{display: "none"}}/>
      </svg>
    </div>
  );
};

export default Floor