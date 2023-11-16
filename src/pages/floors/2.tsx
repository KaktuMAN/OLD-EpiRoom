import type { NextPageWithLayout } from "../_app";
import {ReactElement, useEffect, useState} from "react";
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
  const [width, setWidth] = useState(1920);
  const [height, setHeight] = useState(1080);
  const floor_param = parseInt(router.asPath.split('/').pop() as string) || 0;
  const floor = floor_param > 3 ? 0 : floor_param
  let rooms: Room[] = fetchRooms();
  useEffect(() => {
    fetchApiData(rooms);
    setWidth(window.innerWidth);
    setHeight(window.innerHeight);
    const handleResize = () => {
      setWidth(window.innerWidth);
      setHeight(window.innerHeight);
    };
    window.addEventListener('resize', handleResize);

    return () => {
      window.removeEventListener('resize', handleResize);
    };
  }, []);
  return (
    <div style={{ background: "#000000", border: "2px solid black" }}>
      <Head>
        <title>EpiRooms - Lille</title>
      </Head>
      <div style={{ display: "grid", gridTemplateColumns: "1fr 2fr", height: "100%", columnGap: width / 100 }}>
        <Grid container spacing={(width / 100) / 8} columns={1}>
          {[0, 1, 2, 3].map((floorId) => {
            if (floorId == floor) return;
            return (
              <Grid item height={height / 100 * 26} key={`Floor${floorId}`}>
                <Link href={`./${floorId}`} passHref>
                  <Floor rooms={rooms} floor={floorId} width={width / 100 * 30} height={height / 100 * (76 / 3)} key={`Floor${floorId}`}/>
                </Link>
              </Grid>
            )})}
        </Grid>
        <Floor rooms={rooms} floor={floor} width={width / 100 * 68} height={height / 100 * 78}/>
      </div>
      <div className="scroll_container" style={{position: "fixed", bottom: 0, left: 0, width: "100%", height: height / 100 * 21}}>
        <Box className="scroll slow" style={{}}>
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
