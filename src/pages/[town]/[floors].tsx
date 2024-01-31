import {ReactElement, useEffect, useState} from "react";
import FullPage from "@components/layout/FullPage";
import {useRouter} from "next/router";
import Head from "next/head";
import Floor from "@components/Floors/Floor";
import fetchApiData from "@scripts/fetchApiData";
import {Avatar, Button, ButtonGroup, Dialog, DialogTitle, List, ListItem, ListItemAvatar, ListItemText, Stack, Grid} from "@mui/material";
import RoomInformations from "@components/Rooms/RoomInformations";
import { GetServerSideProps } from 'next';
import * as path from "path";
import * as fs from "fs";
import updateRoomsStatus from "@scripts/updateRoomsStatus";
import {Town, TypeFloor} from "@customTypes/town";
import {Room} from "@customTypes/room";
import { AccessTime, Dangerous } from '@mui/icons-material';

interface FloorRenderProps {
  townData: Town
}

export const getServerSideProps: GetServerSideProps = async (context) => {
  const townName = context.params?.town as string;
  const floorParam = parseInt(context.params?.floors as string);
  let floorFound = false;
  const townPath = path.join(process.cwd(), `./public/towns/${townName}/town.json`);
  let townData: Town;

  try {
    townData = JSON.parse(fs.readFileSync(townPath, 'utf8')) as Town;
  } catch (error) {
    console.error(`Unable to load town informations: ${error}`);
    return { notFound: true };
  }
  // Check if the current floor is in the floors of the config
  townData.floors.map((floor) => {
    if (floorParam === floor.floor)
      floorFound = true
  })
  if (!floorFound)
    return {notFound: true}
  return {
    props: { townData },
  };
};

function formatTime(time: number): string {
  const date = new Date(time);
  const hours = date.getHours();
  const minutes = date.getMinutes();
  return `${hours < 10 ? "0" + hours : hours}h${minutes < 10 ? "0" + minutes : minutes}`
}

export default function FloorRender ({ townData }: FloorRenderProps) {
  const router = useRouter();
  const [width, setWidth] = useState(0);
  const [currentFloor, setFloor] = useState(parseInt(router.query.floors as string) || 0);
  const [open, setOpen] = useState(false);
  const [dialogRoom, setDialogRoom] = useState({} as Room);
  const mobile = width < 900;
  // eslint-disable-next-line react-hooks/rules-of-hooks
  townData.rooms.map((room) => [room.status, room.setStatus] = useState(2))
  useEffect(() => {
    fetchApiData(townData);
    setWidth(window.innerWidth);
    const handleResize = () => {
      setWidth(window.innerWidth);
    };
    const updateStatus = setInterval(() => {
      townData.rooms.map((room) => {
        updateRoomsStatus(room);
      })
    }, 1000)
    const refreshData = setInterval(() => {
      fetchApiData(townData);
    }, 1000 * 60 * 10)
    window.addEventListener("resize", handleResize);
    return () => {
      window.removeEventListener("resize", handleResize);
      clearInterval(updateStatus);
      clearInterval(refreshData);
    };
  }, [townData]);

  return (
    <>
      <Head>
        <title>{townData.name != "" ? `EpiRooms - ${townData.name}` : 'EpiRooms'}</title>
      </Head>
      <Dialog open={open} onClose={() => {setOpen(false); setDialogRoom({} as Room)}}>
        {dialogRoom.status != undefined && (
          <>
          <DialogTitle>
            {dialogRoom.display_name}
          </DialogTitle>
          <List sx={{ pt: 0}}>
            {dialogRoom.activities.map((activity) => (
              <ListItem key={activity.id}>
                <ListItemAvatar>
                  <Avatar sx={{ bgcolor: 'transparent', transform: 'scale(1.4)'}}>
                    {activity.active ? <Dangerous color={"error"}/> : <AccessTime color={"warning"}/> }
                  </Avatar>
                </ListItemAvatar>
                {activity.active ?
                  <ListItemText primary={activity.title} secondary={`Termine à ${formatTime(activity.end.getTime())}`}/> :
                  <ListItemText primary={activity.title} secondary={`Démarre à ${formatTime(activity.start.getTime())}`}/>
                }
              </ListItem>
            ))}
          </List>
          </>
        )}
      </Dialog>
      <Grid container spacing={2} sx={{width: "100%", height: "100%"}}>
        <Grid item container xs={3} direction={"column"} style={{display: mobile ? "none" : ""}}>
          {townData.floors.map((floor: TypeFloor) => {
            if (floor.floor == currentFloor) return;
            return (
              <Grid key={`sideFloor${floor.floor}`} item>
                <Floor townData={townData} floor={floor.floor} setOpen={setOpen} setDialogRoom={setDialogRoom}/>
              </Grid>
            )
          })}
        </Grid>
        <Grid item xs={9} style={{display: mobile ? "none" : ""}}>
          <Floor townData={townData} floor={currentFloor} setOpen={setOpen} setDialogRoom={setDialogRoom}/>
        </Grid>
        <Grid item xs={12}>
          <Stack direction={mobile ? "column" : "row"} spacing={2}>
            <ButtonGroup orientation={"vertical"} variant={"contained"}>
              {townData.floors.map((floor) => {
                return (
                  <Button key={null} disabled={floor.floor == currentFloor} onClick={(e) => {e.preventDefault(); setFloor(floor.floor); history.replaceState({}, '', `/${townData.code}/${floor.floor}`)}}>
                    {floor.name}
                  </Button>
                )
              })}
            </ButtonGroup>
            <Stack direction={mobile ? "column" : "row"} spacing={2} className={mobile ? "" : "scroll_container"}>
              {townData.rooms.map((room) => {
                if (room.floor != currentFloor) return;
                return <RoomInformations room={room} key={room.intra_name} setOpen={setOpen} setDialogRoom={setDialogRoom}/>
              })}
            </Stack>
          </Stack>
        </Grid>
      </Grid>
    </>
  )
}

FloorRender.getLayout = function getLayout(page: ReactElement) {
  return <FullPage>{page}</FullPage>;
}
