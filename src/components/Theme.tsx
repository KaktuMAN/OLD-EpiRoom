import { createTheme } from "@mui/material/styles";

/**
 * Documentation
 * palette : https://mui.com/material-ui/customization/palette/
 * theme : https://mui.com/material-ui/customization/theming/
 */

const theme = createTheme({
  palette: {
    mode: 'dark',
    primary: {
      main: '#0c52a4',
    },
    secondary: {
      main: '#083871',
    }
  }
});

export default theme;
