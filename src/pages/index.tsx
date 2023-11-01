import type { NextPageWithLayout } from "./_app";
import {ReactElement, useEffect} from "react";
import FullPage from "@components/layout/FullPage";
import {Box, Stack} from "@mui/material";
import SingleRoom from "@components/SingleRoom";
import fetchRooms from "@scripts//fetchRooms";
import FirstFloor from "@components/FirstFloor";
import {Room} from "@customTypes/room";

const Home: NextPageWithLayout = () => {
  let rooms: Room[] = fetchRooms();
  useEffect(() => {
    /**
     * Every 10 minutes, fetch the rooms again
     */
    const interval = setInterval(() => {
      rooms = fetchRooms();
    }, 10 * 60 * 1000);
    return () => {
      clearInterval(interval);
    };
  })
  return (
    <div style={{background: "#000000"}}>
      <FirstFloor roomData={rooms}/>
      <div className="scroll_container">
        <Box sx={{height: 50, width: '100%'}} className="scroll">
          <Stack direction={"row"} spacing={2}>
            {rooms.map((room) => {
              const svg_path = `./floors/${room.floor}.svg#${room.intra_name.split('/').pop()}`;
              const key = room.intra_name;
              return <SingleRoom roomData={room} svg_path={svg_path} key={key}/>
            })}
            {rooms.map((room) => {
              const svg_path = `./rooms/${room.floor}/${room.intra_name.split('/').pop()}.svg`;
              const key = room.intra_name;
              return <SingleRoom roomData={room} svg_path={svg_path} key={key} aria-hidden={true}/>
            })}
          </Stack>
        </Box>
      </div>
    </div>
  )
}

Home.getLayout = function getLayout(page: ReactElement) {
  return <FullPage>{page}</FullPage>;
}

export default Home
