import React from "react";

import MemberTable from "./memberTable/memberTable";

import "./home.css"

function Home() {

    return (
        <div className={'container p-5'}>
            <h1>Aktive medlemskap</h1>
            <MemberTable/>
        </div>
    )
}

export default Home