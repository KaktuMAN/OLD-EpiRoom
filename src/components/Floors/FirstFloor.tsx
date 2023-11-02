import {FC} from "react";
import { Room } from "@customTypes/room";

interface FirstFloorProps {
  roomData: Room[];
}

const FirstFloor: FC<FirstFloorProps> = (props) => {
  const { roomData } = props;
  const statuses = ["#ff0000", "#ff8401", "#00ff75"]
  return (
    <>
      <svg width={1250} height={750}>
        <use href={"../../rooms/0/Z0-Floor.svg#B-01-Bulma"} width={1250} height={750} />
      </svg>
    </>
  );
};

export default FirstFloor