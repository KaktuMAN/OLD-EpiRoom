import type { NextPageWithLayout } from "./_app";
import {ReactElement } from "react";
import FullPage from "@components/layout/FullPage";
import {Box, Stack} from "@mui/material";
import SingleRoom from "@components/SingleRoom";
import fetchRooms from "src/scripts/fetchRooms";
import FirstFloor from "@components/FirstFloor";

const Home: NextPageWithLayout = () => {
  const rooms = fetchRooms();
  return (
    <div style={{background: "#000000"}}>
      <div className="scroll_container">
        <Box sx={{height: 50, width: '100%'}} className="scroll">
          <Stack direction={"row"} spacing={2}>
            {rooms.map((room) => {
              const svg_path = `/rooms/${room.floor}/${room.intra_name.split('/').pop()}.svg`;
              const key = room.intra_name;
              return <SingleRoom roomData={room} svg_path={svg_path} key={key}/>
            })}
            {rooms.map((room) => {
              const svg_path = `/rooms/${room.floor}/${room.intra_name.split('/').pop()}.svg`;
              const key = room.intra_name;
              return <SingleRoom roomData={room} svg_path={svg_path} key={key} aria-hidden={true}/>
            })}
          </Stack>
        </Box>
      </div>
      <FirstFloor roomData={rooms}/>
    </div>
  )
}

Home.getLayout = function getLayout(page: ReactElement) {
  return <FullPage>{page}</FullPage>;
}

export default Home
