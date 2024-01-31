import {FC} from "react";
import { Room } from "@customTypes/room";
import {Card, CardContent, Typography} from "@mui/material";

interface RoomInformationsProps {
  room: Room;
  setOpen: (open: boolean) => void;
  setDialogRoom: (room: Room) => void;
}

function formatTime(time: number): string {
  const date = new Date(time);
  const hours = date.getHours();
  const minutes = date.getMinutes();
  return `${hours < 10 ? "0" + hours : hours}h${minutes < 10 ? "0" + minutes : minutes}`
}

const RoomInformations: FC<RoomInformationsProps> = ({room, setOpen, setDialogRoom}) => {
  return (
    <Card className={`room ${["occupied", "reserved", "free"][room.status]}`} style={{color: "black"}} sx={{ minWidth: 200}} onClick={() => {setOpen(true); setDialogRoom(room)}}>
      <CardContent>
        <Typography variant="h5" component="div" sx={{textAlign: "center"}}>
          {room.display_name}
        </Typography>
        {room.activities[0] != undefined && room.status != 2 ? (
          <>
            <Typography sx={{textAlign: "center"}}>
              {formatTime(room.activities[0].start.getTime())} - {formatTime(room.activities[0].end.getTime())}
            </Typography>
            <Typography sx={{ mb: 1.5 }}>
              {room.activities[0].title}
            </Typography>
          </>
        ) : null}
      </CardContent>
    </Card>
  );
};

export default RoomInformations