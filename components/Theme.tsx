import { createTheme } from "@mui/material/styles";

/**
 * Documentation
 * palette : https://mui.com/material-ui/customization/palette/
 * theme : https://mui.com/material-ui/customization/theming/
 */

const theme = createTheme({
  palette: {
    primary: {
      main: '#0c52a4',
    },
    secondary: {
      main: '#083871',
    },
    background: {
      default: '#000000'
    },
    text: {
      primary: '#ffffff',
    },
  },
  components: {
    MuiTooltip: {
      styleOverrides: {
        tooltip: {
          fontSize: '1rem',
        },
      },
    },
  }
});

export default theme;
