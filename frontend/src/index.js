import React from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter, Route, Switch, Link} from "react-router-dom";
import * as serviceWorker from './serviceWorker';

import './index.css';
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap'

import Home from './home/home';
import Navbar from "./navbar/navbar";


ReactDOM.render(
    <React.StrictMode>
        <BrowserRouter>
            <Navbar/>
            <Switch>
                <Route exact path={'/'}>
                    <Home/>
                </Route>

                <Route path={'/tu'}>
                    <OtherPage/>
                </Route>

                <Route>
                    <NotFound/>
                </Route>
            </Switch>
        </BrowserRouter>
    </React.StrictMode>,
    document.getElementById('root')
);

function OtherPage() {
    return (<h1>Other Page</h1>)
}

function NotFound() {
    return (
        <div className={'container'}>
            <h1>404: Not found!</h1>
            <p>We could not find the page you tried to access</p>
            <p><Link className={'btn btn-primary'} to={'/'}>Go home</Link></p>

        </div>
    )
}

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
