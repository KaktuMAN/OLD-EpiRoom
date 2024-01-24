import {FC, useEffect, useState} from "react";
import { Room } from "@customTypes/room";
import {Town} from "@customTypes/town";

interface FloorProps {
  townData: Town;
  floor: number;
  setOpen: (open: boolean) => void;
  setDialogContent: (content: JSX.Element) => void;
}

const Floor: FC<FloorProps> = ({ townData,  floor, setOpen, setDialogContent}) => {
  const [scale, setScale] = useState([1, 1]);
  const generateDialog = (room: Room) => {
    return (
      <>{room.display_name}</>
    )
  }
  useEffect(() => {
    const background = document.getElementById(`background_${floor}`);
    const svg = document.getElementById(`floorRender${floor}`);
    if (!svg || !background) return;
    const {width: svgWidth, height: svgHeight} = svg.getBoundingClientRect();
    background.style.display = "block";
    const {width, height} = background.getBoundingClientRect();
    background.style.display = "none";
    setScale([svgWidth / width, svgHeight / height]);
  }, [floor]);
  return (
    <div style={{width: "100%", height: "100%"}}>
      <svg id={`floorRender${floor}`}>
        {townData.rooms.map((room: Room) => {
          if (room.floor !== floor) return null;
          return (
            <>
              <use href={`/towns/${townData.code}/svg/${floor}/Z${floor}-Floor.svg#${room.intra_name.split('/').pop()}`}
                   className={["occupied", "reserved", "free"][room.status]}
                   transform={`scale(${scale[0]}, ${scale[1]})`} onClick={() => {
                setOpen(true);
                setDialogContent(generateDialog(room))
              }}/>
            </>
          )
        })}
        <use href={`/towns/${townData.code}/svg/${floor}/Z${floor}-Floor.svg#floor`}
             transform={`scale(${scale[0]}, ${scale[1]})`}/>
        <use href={`/towns/${townData.code}/svg/${floor}/Z${floor}-Floor.svg#background`} id={`background_${floor}`}
             style={{display: "none"}}/>
      </svg>
    </div>
  );
};

export default Floor