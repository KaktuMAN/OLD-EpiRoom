import type { NextPageWithLayout } from "./_app";
import { ReactElement } from "react";
import MainLayout from "@components/layout/MainLayout";

const Home: NextPageWithLayout = () => {
  return (
    <div style={{background: "#000000"}}>
    </div>
  )
}

Home.getLayout = function getLayout(page: ReactElement) {
  return <MainLayout>{page}</MainLayout>;
}

export default Home
