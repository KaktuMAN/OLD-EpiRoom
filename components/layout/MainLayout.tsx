import Footer from "@components/Footer";
import { Container, Grid } from "@mui/material";

//'xs' | 'sm' | 'md' | 'lg' | 'xl'

export default function DefaultLayout(props: any) {
  return (
    <>
      <Container maxWidth={"md"}>
        <Grid container spacing={2}>
          <Grid item xs={12} sm={12} md={12} lg={12}>
            {props.children}
          </Grid>
        </Grid>
      </Container>
      <Footer/>
    </>
  );
}