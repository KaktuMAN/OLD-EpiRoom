import { FC } from "react";
import AcUnitIcon from '@mui/icons-material/AcUnit';

const Snowflake: FC = () => {
  return <AcUnitIcon style={{top: "-5%", left: `${Math.floor(Math.random() * 100)}%`, animation: `${Math.floor(Math.random() * 30 + 30)}s linear -${Math.floor(Math.random() * 150)}s infinite snowflake`, rotate: `${Math.floor(Math.random() * 90)}deg`, opacity: `0.5`, zIndex: 42, position: "absolute", scale: `${Math.random() * (1.25 - 0.75) + 0.75}`}}/>;
}

export default Snowflake;