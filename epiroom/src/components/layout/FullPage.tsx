export default function DefaultLayout(props: any) {
  return (
    <div style={{margin: "6px"}}>
      {props.children}
    </div>
  );
}