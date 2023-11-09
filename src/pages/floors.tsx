import type { NextPageWithLayout } from "./_app";
import {MouseEvent, ReactElement, useEffect, useState} from "react";
import FullPage from "@components/layout/FullPage";
import {Box, Grid, Stack} from "@mui/material";
import Link from "next/link";
import SingleRoom from "@components/Rooms/SingleRoom";
import fetchRooms from "@scripts/fetchRooms";
import Floor from "@components/Floors/Floor";
import {Room} from "@customTypes/room";
import {useRouter} from "next/router";
import fetchApiData from "@scripts/fetchApiData";
import Head from "next/head";

const FloorRender: NextPageWithLayout = () => {
  const router = useRouter();
  const [floor, setFloor] = useState(parseInt(router.query.floor ? router.query.floor as string : "0"));
  const linkClick = async (event: MouseEvent, floorId: number) => {
    event.preventDefault();
    setFloor(floorId)
    await router.push(`./floors?floor=${floorId}`, undefined, {shallow: true})
  };
  let rooms: Room[] = fetchRooms();
  useEffect(() => {
    fetchApiData(rooms);
  }, []);
  return (
    <div style={{ background: "#000000" }}>
      <Head>
        <title>EpiRooms - Lille</title>
      </Head>
      <div style={{ display: "grid", gridTemplateColumns: "1fr 2fr", height: "100vh" }}>
        <Grid container spacing={0} columns={1}>
          {[0, 1, 2, 3].map((floorId) => {
            return (
              <Grid item xs={6} key={`Floor${floorId}`} display={floorId == floor ? "none" : ""}>
                <Link href={"./floors"} passHref onClick={(e) => linkClick(e, floorId)}>
                  <Floor rooms={rooms} floor={floorId} width={550} height={650/3} key={`Floor${floorId}`}/>
                </Link>
              </Grid>
            )})}
        </Grid>
        <Floor rooms={rooms} floor={floor} width={1250} height={650}/>
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
