import React, {useEffect, useState} from "react"
import axios from 'axios'

import "./memberTable.css"

function MemberTable() {

    const pfTypeName = "PolytekniskForening"
    const petTypeName = "PFPetroleumOgFornybar"
    const teknaTypeName = "Tekna"
    const bedriftsmedlemTypeName = "Bedriftsmedlem"

    const ordinaryTierName = "Ordinary"
    const seniorTierName = "Senior"
    const studentTierName = "Student"

    const [memberStats, setMemberStats] = useState([])
    useEffect(() => {
        axios.get('/stats/members')
            .then((response) => {
                // Response also gives back "Bedrift"-type members
                setMemberStats(response.data)
            })
    }, [])

    function getMemberCount(type, tier, paying) {

        if (!type && !tier) {
            return memberStats.types.reduce((acc, type) => {
                if (type.type === "Bedrift" || type.type === "ERROR") return acc
                return acc + type.tiers.reduce((acc, tier) => {
                    return acc + tier[paying]
                }, 0)
            }, 0)
        } else if (!tier) {
            return memberStats.types.find(e => e.type === type).tiers.reduce((acc, tier) => {
                return acc + tier[paying]
            }, 0)
        } else if (!type) {
            return memberStats.types.reduce((acc, type) => {
                if (type.type === "Bedrift" || type.type === "ERROR") return acc
                return acc + type.tiers.find(e => e.tier === tier)[paying]
            }, 0)
        } else {
            return memberStats.types.find(e => e.type === type).tiers.find(e => e.tier === tier)[paying]
        }

    }

    if (memberStats.length <= 0) {
        return "Loading members..."
    }

    return (

        // <div>
        //     {console.log(memberStats)}
        //     {memberStats.types.find(e => e.type === pfTypeName).tiers.find(e => e.tier === ordinaryTierName).paying}
        // </div>
        <table className="table table-striped table-bordered">
            <thead className="thead-dark">
            <tr>
                <th scope="col"/>
                <th scope="col" colSpan={3}>Polyteknisk Forening</th>
                <th scope="col" colSpan={3}>Petroleum og fornybar</th>
                <th scope="col" colSpan={3}>Tekna</th>
                <th scope="col" colSpan={3}>Bedriftsmedlem</th>
                <th scope="col" colSpan={3}>Totalt</th>
            </tr>
            <tr>
                <th scope="col"/>
                <th scope="col" className="tableSubheader">Betalende</th>
                <th scope="col" className="tableSubheader">Gratis</th>
                <th scope="col" className="tableSubheader">Totalt</th>
                <th scope="col" className="tableSubheader">Betalende</th>
                <th scope="col" className="tableSubheader">Gratis</th>
                <th scope="col" className="tableSubheader">Totalt</th>
                <th scope="col" className="tableSubheader">Betalende</th>
                <th scope="col" className="tableSubheader">Gratis</th>
                <th scope="col" className="tableSubheader">Totalt</th>
                <th scope="col" className="tableSubheader">Betalende</th>
                <th scope="col" className="tableSubheader">Gratis</th>
                <th scope="col" className="tableSubheader">Totalt</th>
                <th scope="col" className="tableSubheader">Betalende</th>
                <th scope="col" className="tableSubheader">Gratis</th>
                <th scope="col" className="tableSubheader">Totalt</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <th scope="row">Ordinær</th>

                <td>{getMemberCount(pfTypeName, ordinaryTierName, "paying")}</td>
                <td>{getMemberCount(pfTypeName, ordinaryTierName, "free")}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(pfTypeName, ordinaryTierName, "total")}</td>

                <td>{getMemberCount(petTypeName, ordinaryTierName, "paying")}</td>
                <td>{getMemberCount(petTypeName, ordinaryTierName, "free")}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(petTypeName, ordinaryTierName, "total")}</td>

                <td>{getMemberCount(teknaTypeName, ordinaryTierName, "paying")}</td>
                <td>{getMemberCount(teknaTypeName, ordinaryTierName, "free")}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(teknaTypeName, ordinaryTierName, "total")}</td>

                <td>{getMemberCount(bedriftsmedlemTypeName, ordinaryTierName, "paying")}</td>
                <td>{getMemberCount(bedriftsmedlemTypeName, ordinaryTierName, "free")}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(bedriftsmedlemTypeName, ordinaryTierName, "total")}</td>

                <td>{getMemberCount(null, ordinaryTierName, "paying")}</td>
                <td>{getMemberCount(null, ordinaryTierName, "free")}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(null, ordinaryTierName, "total")}</td>
            </tr>
            <tr>
                <th scope="row">Senior</th>

                <td>{getMemberCount(pfTypeName, seniorTierName, "paying")}</td>
                <td>{getMemberCount(pfTypeName, seniorTierName, "free")}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(pfTypeName, seniorTierName, "total")}</td>

                <td>{getMemberCount(petTypeName, seniorTierName, "paying")}</td>
                <td>{getMemberCount(petTypeName, seniorTierName, "free")}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(petTypeName, seniorTierName, "total")}</td>

                <td>{getMemberCount(teknaTypeName, seniorTierName, "paying")}</td>
                <td>{getMemberCount(teknaTypeName, seniorTierName, "free")}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(teknaTypeName, seniorTierName, "total")}</td>

                <td>{getMemberCount(bedriftsmedlemTypeName, seniorTierName, "paying")}</td>
                <td>{getMemberCount(bedriftsmedlemTypeName, seniorTierName, "free")}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(bedriftsmedlemTypeName, seniorTierName, "total")}</td>

                <td>{getMemberCount(null, seniorTierName, "paying")}</td>
                <td>{getMemberCount(null, seniorTierName, "free")}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(null, seniorTierName, "total")}</td>
            </tr>
            <tr>
                <th scope="row">Student</th>

                <td>{getMemberCount(pfTypeName, studentTierName, "paying")}</td>
                <td>{getMemberCount(pfTypeName, studentTierName, "free")}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(pfTypeName, studentTierName, "total")}</td>

                <td>{getMemberCount(petTypeName, studentTierName, "paying")}</td>
                <td>{getMemberCount(petTypeName, studentTierName, "free")}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(petTypeName, studentTierName, "total")}</td>

                <td>{getMemberCount(teknaTypeName, studentTierName, "paying")}</td>
                <td>{getMemberCount(teknaTypeName, studentTierName, "free")}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(teknaTypeName, studentTierName, "total")}</td>

                <td>{getMemberCount(bedriftsmedlemTypeName, studentTierName, "paying")}</td>
                <td>{getMemberCount(bedriftsmedlemTypeName, studentTierName, "free")}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(bedriftsmedlemTypeName, studentTierName, "total")}</td>

                <td>{getMemberCount(null, studentTierName, "paying")}</td>
                <td>{getMemberCount(null, studentTierName, "free")}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(null, studentTierName, "total")}</td>
            </tr>
            <tr>
                <th scope="row">Totalt</th>

                <td>{getMemberCount(pfTypeName, null, "paying")}</td>
                <td>{getMemberCount(pfTypeName, null, "free")}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(pfTypeName, null, "total")}</td>

                <td>{getMemberCount(petTypeName, null, "paying")}</td>
                <td>{getMemberCount(petTypeName, null, "free")}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(petTypeName, null, "total")}</td>

                <td>{getMemberCount(teknaTypeName, null, "paying")}</td>
                <td>{getMemberCount(teknaTypeName, null, "free")}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(teknaTypeName, null, "total")}</td>

                <td>{getMemberCount(bedriftsmedlemTypeName, null, "paying")}</td>
                <td>{getMemberCount(bedriftsmedlemTypeName, null, "free")}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(bedriftsmedlemTypeName, null, "total")}</td>

                <td>{getMemberCount(null, null, "paying")}</td>
                <td>{getMemberCount(null, null, "free")}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(null, null, "total")}</td>
            </tr>
            </tbody>
        </table>
    )
}


export default MemberTable