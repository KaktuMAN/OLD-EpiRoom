import {FC, useEffect} from "react";
import { Room } from "@customTypes/room";

interface FloorProps {
  rooms: Room[];
  town: string;
  floor: number;
}

const Floor: FC<FloorProps> = ({ rooms, town,  floor}) => {
  useEffect(() => {
    const embed = document.getElementById(`embedFloor${floor}`) as HTMLObjectElement;
    embed.addEventListener("load", function() {
      const svgDoc = embed.getSVGDocument();
      if (!svgDoc) return
      rooms.map((room) => {
        const roomElement = svgDoc.getElementById(room.name);
        if (roomElement === null) return
        roomElement.setAttribute("class", ["occupied", "reserved", "free"][room.status]);
        roomElement.addEventListener("click", () => {
          console.log(room)
        })
      })
    });
  })
  return (
    <div style={{width: "100%", height: "100%"}}>
      <object data={`/towns/${town}/svg/${floor}/Z${floor}-Floor.svg`} width="300" height="300" id={`embedFloor${floor}`}/>
    </div>
  );
};

export default Floor