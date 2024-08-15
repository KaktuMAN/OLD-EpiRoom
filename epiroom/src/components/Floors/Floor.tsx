import {FC, ReactElement, useEffect, useState} from "react";
import { Room } from "@customTypes/room";
import {Town} from "@customTypes/town";
import {generateRoomContent} from "@scripts/dialogGenerator";
import Image from "next/image";

interface FloorProps {
  townData: Town;
  floor: number;
  setDialogOpen: (open: boolean) => void;
  setDialogContent: (content: ReactElement) => void;
  sideDisplay: boolean;
}

const Floor: FC<FloorProps> = ({ townData,  floor, setDialogOpen, setDialogContent, sideDisplay}) => {
  const [svg, setSvg] = useState<Document | null>(null);
  useEffect(() => {
    fetch(`http://localhost:8080/floors/LIL/${floor}/svg`)
      .then((response) => response.text())
      .then((data) => {
        const parser = new DOMParser();
        const svgDoc = parser.parseFromString(data, "image/svg+xml");
        townData.rooms.map((room: Room) => {
          if (room.floor !== floor) return;
          const roomCode = room.intra_name.split('/').pop()
          const roomElement = svgDoc.getElementById(roomCode ? roomCode : "");
          if (roomElement) {
            roomElement.classList.add(["occupied", "reserved", "free"][room.status]);
            if (room.no_status === true) roomElement.classList.add("nostatus");
          }
          const textElement = svgDoc.getElementById(`${roomCode}-Text`);
          if (textElement) {
            if (room.no_status === true) textElement.classList.add("nostatus_text");
          }
        })
        setSvg(svgDoc);
      });
  }, [floor]);
  return (
    <div dangerouslySetInnerHTML={{__html: svg?.activeElement?.outerHTML || ""}} style={{width: "100%", height: "100%"}}>
    </div>
  )
  /*return (
    <>
      <svg id={`floorRender${floor}`}>
        {townData.rooms.map((room: Room, index: number) => {
          if (room.floor !== floor) return null;
          return (
            <>
              <use href={`/towns/${townData.code}/svg/Z${floor}-Floor.svg#${room.intra_name.split('/').pop()}`} className={`${["occupied", "reserved", "free"][room.status]} ${room.no_status === true ? "nostatus" : ""}`} key={`${room.intra_name}-${index}`} transform={`scale(${scale[0]}, ${scale[1]})`} onClick={() => {if (room.no_status !== true && !sideDisplay) {setDialogOpen(true);setDialogContent(generateRoomContent(room))}}}/>
              <use href={`/towns/${townData.code}/svg/Z${floor}-Floor.svg#${room.intra_name.split('/').pop()}-Text`} className={room.no_status === true ? "nostatus_text" : ""} key={`${room.intra_name}-text-${index}`} transform={`scale(${scale[0]}, ${scale[1]})`}/>
            </>
          )
        })}
        <use href={`/towns/${townData.code}/svg/Z${floor}-Floor.svg#floor`}
             transform={`scale(${scale[0]}, ${scale[1]})`}/>
        <use href={`/towns/${townData.code}/svg/Z${floor}-Floor.svg#background`} id={`background_${floor}`}
             style={{display: "none"}}/>
      </svg>
    </>
  );*/
};

export default Floor