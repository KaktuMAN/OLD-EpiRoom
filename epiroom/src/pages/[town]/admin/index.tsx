import {ReactElement, useEffect, useState} from "react";
import FullPage from "@components/layout/FullPage";
import {NextPageWithLayout} from "../../_app";
import {AppBar, Box, Button, CircularProgress, Menu, Toolbar, Typography} from "@mui/material";
import {useRouter} from "next/router";
import {SnackbarProvider, enqueueSnackbar} from "notistack";
import RoomsTable from "@components/Admin/Rooms/RoomsTable";

const TownAdmin: NextPageWithLayout = () => {
  const router = useRouter();
  const {town} = router.query;
  const [floors, setFloors] = useState([]);
  const [mainContent, setMainContent] = useState<ReactElement>(<></>);
  const [loading, setLoading] = useState(true);
  useEffect(() => {
    if (!town) return;
    fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/campus/${town}/floors`)
    .then((res) => res.json())
    .then((data) => {
      setFloors(data)
      setLoading(false)
    })
    .catch((error) => {
      setLoading(false)
      enqueueSnackbar("An error occured while fetching floors (See console)", {variant: "error"});
      console.error(error);
    });
  }, [town])
  return (
    <main style={{width: "100%", height: "100%"}}>
      <AppBar position={"static"}>
        <Toolbar>
          <Typography variant={"h6"} component={"div"} sx={{flexGrow: 1}}>
            EpiRoom - {town}
          </Typography>
          <Box sx={{flexGrow: 0}}>
            <Button sx={{ color: '#fff' }}  onClick={() => setMainContent(<RoomsTable floors={floors} campusCode={town as string} enqueueSnackbar={enqueueSnackbar}/>)} disabled={!floors.length}>
              Rooms
            </Button>
          </Box>
        </Toolbar>
      </AppBar>
      <SnackbarProvider maxSnack={5}/>
      {loading ? <CircularProgress/> : mainContent}
    </main>
  )
}

TownAdmin.getLayout = function getLayout(page: ReactElement) {
  return <FullPage>{page}</FullPage>;
}

export default TownAdmin
