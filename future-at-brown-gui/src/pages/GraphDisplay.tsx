import React, {createRef, useEffect, useState} from "react"
import { Card, Container, Grid, GridRow, Header, Segment, Sticky } from "semantic-ui-react"
import CourseInfo from "../modules/CourseInfo";
import CourseTile from "../modules/CourseTile";
import { ForceGraph2D, ForceGraph3D, ForceGraphVR, ForceGraphAR } from 'react-force-graph';
import { User } from "../modules/Data"
import axios from "axios";
import ClassGraph from "../modules/ClassGraph";

interface Params {
    user: User;
}

const GraphDisplay: React.FC<Params> = (props) => {
    const [popupSeen, setPopup] = useState(false);

    function togglePop() {
        console.log("togglepop");
        setPopup(!popupSeen);
    };


    return <div>
        <div>
            <ClassGraph clickClass={togglePop}/>
        </div>
    </div>;
}

export default GraphDisplay;