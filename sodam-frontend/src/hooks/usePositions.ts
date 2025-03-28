import {useEffect, useState} from "react";
import {PositionSimpleType} from "../types/position";
import {getSimplePositions} from "../api/position";

const usePositions = () => {
    const [ simplePositionsEntry, setSimplePositionsEntry ] = useState<PositionSimpleType[] | null>(null)
    useEffect(() => {
        const fetchSimplePositions = async () => {
            const simplePositions = await getSimplePositions()
            setSimplePositionsEntry(simplePositions.data.data.positions)
        }

        fetchSimplePositions()
    }, [])

    return simplePositionsEntry
}

export default usePositions