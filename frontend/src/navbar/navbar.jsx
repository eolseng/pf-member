import React from "react";
import {Link, useLocation} from "react-router-dom"
import axios from "axios";

import "./navbar.css"

function Navbar() {

    const links = <React.Fragment>
        <HeaderLink name={'Teknisk Ukeblad'} path={'/tu'}/>
    </React.Fragment>

    const downloadDropdown =
        <li className={'nav-item dropdown'}>
            <Link className={'nav-link dropdown-toggle'}
                  to={'#'}
                  id={'navbarDownloadDropdown'}
                  role={'button'}
                  data-toggle={'dropdown'}
                  aria-haspopup={'true'}>
                Last ned rapport
            </Link>

            <div className={'dropdown-menu'}
                 aria-labelledby={'navbarDownloadDropdown'}>
                <FileDownloader
                    buttonText={'TU Medlemskap'}
                    endpoint={'/download/tu'}/>
                <FileDownloader
                    buttonText={'CRM Feil'}
                    endpoint={'/download/member-error'}/>
                <FileDownloader
                    buttonText={'Repeterende Faktura Feil'}
                    endpoint={'/download/repeating-invoice-error'}/>
            </div>
        </li>

    const uploadDropdown =
        <li className={'nav-item dropdown'}>
            <Link className={'nav-link dropdown-toggle'}
                  to={'#'}
                  id={'navbarUploadDropdown'}
                  role={'button'}
                  data-toggle={'dropdown'}
                  aria-haspopup={'true'}>
                Last opp fil
            </Link>

            <div className={'dropdown-menu'}
                 aria-labelledby={'navbarUploadDropdown'}>
                <FileUploader
                    buttonText={'CRM Rapport (.xls)'}
                    filetypes={'.xls'}
                    endpoint={'/upload/crm'}/>
                <FileUploader
                    buttonText={'Repeterende Faktura (.csv)'}
                    filetypes={'.csv'}
                    endpoint={'/upload/repeterende-faktura'}/>
            </div>
        </li>

    return (
        <header className={'navbar navbar-expand-lg navbar-dark bg-primary'}>
            <Link className={'navbar-brand'} to={'/'}>Polyteknisk Forening</Link>
            <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavDropdown"
                    aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
                <span className="navbar-toggler-icon"/>
            </button>
            <div className={'collapse navbar-collapse'} id="navbarNavDropdown">
                <ul className={'navbar-nav'}>
                    {links}
                    {downloadDropdown}
                    {uploadDropdown}
                </ul>
            </div>
        </header>
    )
}


const HeaderLink = props => {

    const name = props.name
    const path = props.path
    const location = useLocation()

    function isActive(path) {
        return location.pathname.includes(path) ? " active" : ""
    }

    return (
        <li className={'nav-item'}>
            <Link className={'nav-link' + isActive(path)} to={path}>{name}</Link>
        </li>
    )
}

const FileDownloader = props => {

    const buttonText = props.buttonText
    const endpoint = props.endpoint

    const handleClick = () => {
        axios({
            url: endpoint,
            method: 'GET',
            responseType: 'blob'
        }).then(({data, headers}) => {
            const url = window.URL.createObjectURL(new Blob([data]))
            const link = document.createElement('a')
            link.href = url
            link.setAttribute('download', headers.filename)
            link.click();
        })
    }

    return (
        <React.Fragment>
            <button onClick={handleClick} className={'dropdown-item'}>{buttonText}</button>
        </React.Fragment>
    )

}

const FileUploader = props => {

    const buttonText = props.buttonText
    const filetypes = props.filetypes
    const endpoint = props.endpoint
    const inputId = 'file-uploader-' + endpoint.slice(1)

    const handleClick = () => {
        document.getElementById(inputId).click()
    }

    const uploadFile = event => {
        const data = new FormData()
        data.append('file', event.target.files[0])
        axios.post(endpoint, data, {
            headers: {'Content-Type': 'multipart/form-data'}
        }).then(() => {
            window.location.reload(false)
        })
    }

    return (
        <React.Fragment>
            <input type={'file'} accept={filetypes} style={{display: 'none'}} id={inputId}
                   onChange={uploadFile}/>
            <button onClick={handleClick} className={'dropdown-item'}>{buttonText}</button>
        </React.Fragment>
    )
}

export default Navbar
