import {FC, ReactElement} from "react";
import { Room } from "@customTypes/room";
import { Card, CardContent, Typography} from "@mui/material";
import {formatTime, generateRoomContent} from "@scripts/dialogGenerator";

interface RoomInformationsProps {
  room: Room;
  setDialogOpen: (open: boolean) => void;
  setDialogContent: (content: ReactElement) => void;
}

const RoomInformations: FC<RoomInformationsProps> = ({room, setDialogOpen, setDialogContent}) => {
  return (
    <Card className={["occupied", "reserved", "free"][room.status]} sx={{ minWidth: 200, color: "black"}} onClick={() => {setDialogOpen(true);setDialogContent(generateRoomContent(room))}}>
      <CardContent>
        <Typography variant="h5" component="div" sx={{textAlign: "center"}}>
          {room.display_name}
        </Typography>
        {room.activities[0] != undefined ? (
          <>
            <Typography sx={{textAlign: "center"}}>
              {formatTime(room.activities[0].start.getTime())} - {formatTime(room.activities[0].end.getTime())}
            </Typography>
            <Typography sx={{ mb: 1.5 }}>
              {room.activities[0].title.slice(0, 20) + (room.activities[0].title.length > 20 ? "..." : "")}
            </Typography>
          </>
        ) : null}
      </CardContent>
    </Card>
  );
};

export default RoomInformations