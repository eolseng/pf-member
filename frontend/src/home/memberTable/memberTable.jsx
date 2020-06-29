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

    const [members, setMembers] = useState([])
    useEffect(() => {
        axios.get('/member')
            .then((response) => {
                // Response also gives back "Bedrift"-type members
                const members = response.data.filter(member => member.membershipType !== "Bedrift")
                setMembers(members)
            })
    }, [])

    if (members.length <= 0) {
        return "Loading members..."
    }

    return (
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

                <td>{getMemberCount(members, pfTypeName, ordinaryTierName, false)}</td>
                <td>{getMemberCount(members, pfTypeName, ordinaryTierName, true)}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(members, pfTypeName, ordinaryTierName, null)}</td>

                <td>{getMemberCount(members, petTypeName, ordinaryTierName, false)}</td>
                <td>{getMemberCount(members, petTypeName, ordinaryTierName, true)}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(members, petTypeName, ordinaryTierName, null)}</td>

                <td>{getMemberCount(members, teknaTypeName, ordinaryTierName, false)}</td>
                <td>{getMemberCount(members, teknaTypeName, ordinaryTierName, true)}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(members, teknaTypeName, ordinaryTierName, null)}</td>

                <td>{getMemberCount(members, bedriftsmedlemTypeName, ordinaryTierName, false)}</td>
                <td>{getMemberCount(members, bedriftsmedlemTypeName, ordinaryTierName, true)}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(members, bedriftsmedlemTypeName, ordinaryTierName, null)}</td>

                <td>{getMemberCount(members, null, ordinaryTierName, false)}</td>
                <td>{getMemberCount(members, null, ordinaryTierName, true)}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(members, null, ordinaryTierName, null)}</td>
            </tr>
            <tr>
                <th scope="row">Senior</th>

                <td>{getMemberCount(members, pfTypeName, seniorTierName, false)}</td>
                <td>{getMemberCount(members, pfTypeName, seniorTierName, true)}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(members, pfTypeName, seniorTierName, null)}</td>

                <td>{getMemberCount(members, petTypeName, seniorTierName, false)}</td>
                <td>{getMemberCount(members, petTypeName, seniorTierName, true)}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(members, petTypeName, seniorTierName, null)}</td>

                <td>{getMemberCount(members, teknaTypeName, seniorTierName, false)}</td>
                <td>{getMemberCount(members, teknaTypeName, seniorTierName, true)}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(members, teknaTypeName, seniorTierName, null)}</td>

                <td>{getMemberCount(members, bedriftsmedlemTypeName, seniorTierName, false)}</td>
                <td>{getMemberCount(members, bedriftsmedlemTypeName, seniorTierName, true)}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(members, bedriftsmedlemTypeName, seniorTierName, null)}</td>

                <td>{getMemberCount(members, null, seniorTierName, false)}</td>
                <td>{getMemberCount(members, null, seniorTierName, true)}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(members, null, seniorTierName, null)}</td>
            </tr>
            <tr>
                <th scope="row">Student</th>

                <td>{getMemberCount(members, pfTypeName, studentTierName, false)}</td>
                <td>{getMemberCount(members, pfTypeName, studentTierName, true)}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(members, pfTypeName, studentTierName, null)}</td>

                <td>{getMemberCount(members, petTypeName, studentTierName, false)}</td>
                <td>{getMemberCount(members, petTypeName, studentTierName, true)}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(members, petTypeName, studentTierName, null)}</td>

                <td>{getMemberCount(members, teknaTypeName, studentTierName, false)}</td>
                <td>{getMemberCount(members, teknaTypeName, studentTierName, true)}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(members, teknaTypeName, studentTierName, null)}</td>

                <td>{getMemberCount(members, bedriftsmedlemTypeName, studentTierName, false)}</td>
                <td>{getMemberCount(members, bedriftsmedlemTypeName, studentTierName, true)}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(members, bedriftsmedlemTypeName, studentTierName, null)}</td>

                <td>{getMemberCount(members, null, studentTierName, false)}</td>
                <td>{getMemberCount(members, null, studentTierName, true)}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(members, null, studentTierName, null)}</td>
            </tr>
            <tr>
                <th scope="row">Totalt</th>

                <td>{getMemberCount(members, pfTypeName, null, false)}</td>
                <td>{getMemberCount(members, pfTypeName, null, true)}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(members, pfTypeName, null, null)}</td>

                <td>{getMemberCount(members, petTypeName, null, false)}</td>
                <td>{getMemberCount(members, petTypeName, null, true)}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(members, petTypeName, null, null)}</td>

                <td>{getMemberCount(members, teknaTypeName, null, false)}</td>
                <td>{getMemberCount(members, teknaTypeName, null, true)}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(members, teknaTypeName, null, null)}</td>

                <td>{getMemberCount(members, bedriftsmedlemTypeName, null, false)}</td>
                <td>{getMemberCount(members, bedriftsmedlemTypeName, null, true)}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(members, bedriftsmedlemTypeName, null, null)}</td>

                <td>{getMemberCount(members, null, null, false)}</td>
                <td>{getMemberCount(members, null, null, true)}</td>
                <td style={{fontWeight: 'bold'}}>{getMemberCount(members, null, null, null)}</td>
            </tr>
            </tbody>
        </table>
    )
}

function getMemberCount(members, type, tier, free) {

    if (type) {
        members = members.filter(member => member.membershipType === type)
    }
    if (tier) {
        members = members.filter(member => member.membershipTier === tier)
    }

    if (free === null) {
    } else if (!free){
        members = members.filter(member => member.freeMembership === free)
    } else if (free) {
        members = members.filter(member => member.freeMembership === free)
    }

    return members.length
}

export default MemberTable