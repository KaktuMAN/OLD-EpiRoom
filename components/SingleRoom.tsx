import { FC } from "react";

interface SingleRoomProps {
  room_name: string;
  svg_path: string;
}

const SingleRoom: FC<SingleRoomProps> = (props) => {
  const {room_name, svg_path} = props;
  return (
    <>
      <img alt={room_name} src={svg_path}/>
      <p>{room_name}</p>
    </>
  );
};

export default SingleRoom