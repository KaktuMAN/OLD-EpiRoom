import type { NextPageWithLayout } from "../_app";
import {ReactElement, useEffect} from "react";
import FullPage from "@components/layout/FullPage";
import {Box, Grid, Link, Stack} from "@mui/material";
import SingleRoom from "@components/Rooms/SingleRoom";
import fetchRooms from "@scripts/fetchRooms";
import Floor from "@components/Floors/Floor";
import {Room} from "@customTypes/room";
import {useRouter} from "next/router";
import fetchApiData from "@scripts/fetchApiData";

const FloorRender: NextPageWithLayout = () => {
  const router = useRouter();
  const floor = parseInt(router.asPath.split('/').pop() as string);
  let rooms: Room[] = fetchRooms();
  useEffect(() => {
    fetchApiData(rooms);
  }, []);
  return (
    <div style={{ background: "#000000" }}>
      <div style={{ display: "grid", gridTemplateColumns: "1fr 2fr", height: "100vh" }}>
        <Grid container spacing={0} columns={1}>
          {[0, 1, 2, 3].map((floorId) => {
            if (floorId === floor) return null;
            return (
              <Grid item xs={6} key={`Floor${floorId}`}>
                <Link href={`./${floorId}`}>
                  <Floor rooms={rooms} floor={floorId} width={550} height={650/3} key={`Floor${floorId}`}/>
                </Link>
              </Grid>
            )})}
        </Grid>
        <Floor rooms={rooms} floor={floor} width={1250} height={750}/>
      </div>
      <div className="scroll_container" style={{position: "fixed", bottom: 0, left: 0, width: "100%"}}>
        <Box className="scroll slow">
          <Stack direction={"row"} spacing={2}>
            {rooms.map((room) => {
              const svg_path = `../rooms/${room.floor}/${room.intra_name.split('/').pop()}.svg`;
              const key = room.intra_name;
              return <SingleRoom roomData={room} svg_path={svg_path} key={key}/>
            })}
            {rooms.map((room) => {
              const svg_path = `../rooms/${room.floor}/${room.intra_name.split('/').pop()}.svg`;
              const key = room.intra_name;
              return <SingleRoom roomData={room} svg_path={svg_path} key={key} aria-hidden={true}/>
            })}
          </Stack>
        </Box>
      </div>
    </div>
  )
}

FloorRender.getLayout = function getLayout(page: ReactElement) {
  return <FullPage>{page}</FullPage>;
}

export default FloorRender
