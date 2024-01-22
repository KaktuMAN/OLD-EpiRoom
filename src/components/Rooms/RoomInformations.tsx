import {FC} from "react";
import { Room } from "@customTypes/room";

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
    <div className={["occupied", "reserved", "free"][room.status]}>
      {room.display_name}{room.activities[0] == undefined ? "" : ` - ${room.activities[0].title}`}
    </div>
  );
};

export default RoomInformations