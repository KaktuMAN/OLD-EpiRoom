import { Grid } from "@mui/material";
//'xs' | 'sm' | 'md' | 'lg' | 'xl'

export default function DefaultLayout(props: any) {
  return (
      <Grid container spacing={2}>
        <Grid item xs={12} sm={12} md={12} lg={12} style={{background: "#000000"}}>
          <main>{props.children}</main>
        </Grid>
      </Grid>
  );
}