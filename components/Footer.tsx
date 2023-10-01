import { FC } from "react";
import {Box, Skeleton, Stack} from "@mui/material";
import SingleRoom from "@components/SingleRoom";

interface Room {
  "intra_name": string,
  "name": string,
  "display_name": string,
  "seats": number,
  "floor": number,
}

const Footer: FC = (props) => {
  const rooms = require("../public/rooms.json").rooms;
  // API call to get rooms
  let loading = true;
  return (
    <Box sx={{height: 50, width: '100%'}}>
      <Stack direction={"row"} spacing={2} overflow={"hidden"}>
        {rooms.map((room: Room) =>
          loading ? (
            <div>
              <Skeleton variant={"rectangular"} width={100} height={75}/>
              <Skeleton variant={"text"} width={100} height={25}/>
            </div>
            ) : (
            <div>
              <SingleRoom room_name={room.display_name} svg_path={""}/>
            </div>
            )
        )}
      </Stack>
    </Box>
  );
};

export default Footer