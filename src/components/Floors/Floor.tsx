import {FC, useEffect, useState} from "react";
import {Tooltip} from "@mui/material";
import { Room } from "@customTypes/room";

interface FloorProps {
  rooms: Room[];
  town: string;
  floor: number;
}

const Floor: FC<FloorProps> = ({ rooms, town,  floor}) => {
  return (
    <div style={{width: "100%", height: "100%"}}>
      <svg style={{position: "relative", left: 0, top: 0, width: "100%", height: "100%"}}>
        {rooms.map((room: Room) => {
          if (room.floor !== floor) return null;
          return (
            <>
              <Tooltip title={room.display_name} arrow style={{backgroundColor: "white", color: "black"}}>
                <use href={`/towns/${town}/svg/${floor}/Z${floor}-Floor.svg#${room.intra_name.split('/').pop()}`} className={["occupied", "reserved", "free"][room.status]} key={null}/>
              </Tooltip>
            </>
          )})}
        <use href={`/towns/${town}/svg/${floor}/Z${floor}-Floor.svg#floor`}/>
      </svg>
    </div>
  );
};

export default Floor