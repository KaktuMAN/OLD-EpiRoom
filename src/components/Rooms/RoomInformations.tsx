import {FC} from "react";
import { Room } from "@customTypes/room";
import {Paper} from "@mui/material";

interface RoomInformationsProps {
  room: Room;
}

function formatTime(time: number): string {
  if (time / 60 > 1) {
    return `${Math.floor(time / 60)}h${time % 60}m`
  } else {
    return `${time % 60} minute${time % 60 > 1 ? "s" : ""}`
  }
}

const RoomInformations: FC<RoomInformationsProps> = ({room}) => {
  return (
    <Paper className={["occupied", "reserved", "free"][room.status]}>
      {room.display_name}
    </Paper>
  );
};

export default RoomInformations