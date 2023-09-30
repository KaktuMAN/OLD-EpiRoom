import type { NextPageWithLayout } from "./_app";
import { ReactElement } from "react";
import FullPage from "@components/layout/FullPage";

const Home: NextPageWithLayout = () => {
  return (
    <div style={{background: "#000000"}}>
    </div>
  )
}

Home.getLayout = function getLayout(page: ReactElement) {
  return <FullPage>{page}</FullPage>;
}

export default Home
