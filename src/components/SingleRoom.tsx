import {FC, useEffect, useState} from "react";
import { Room } from "@customTypes/room";

interface SingleRoomProps {
  roomData: Room;
  svg_path: string;
}

const SingleRoom: FC<SingleRoomProps> = (props) => {
  const { roomData, svg_path } = props;
  const [status, setStatus] = useState(0);
  useEffect(() => {
    const interval = setInterval(() => {
      if (roomData.activities.length === 0) {
        setStatus(2);
      } else if (roomData.activities[0].start.valueOf() < Date.now() && roomData.activities[0].end.valueOf() > Date.now()) {
        setStatus(0);
      } else if (roomData.activities[0].start.valueOf() > Date.now()) {
        setStatus(1);
      } else {
        setStatus(2);
        roomData.activities.pop();
      }
    }, 1000);
    return () => {
      clearInterval(interval);
    };
  });
  return (
    <>
      <div>
        <svg height={100} width={150} y={0} x={0}>
          <use href={svg_path} fill={["#FF0000", "#FFFF00","#00FF00"][status]} x={0} y={0}/>
        </svg>
        <p>
          {roomData.display_name}
        </p>
      </div>
    </>
  );
};

export default SingleRoom